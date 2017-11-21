using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Reflection;

using Apache.NMS;
using Apache.NMS.ActiveMQ;
using Apache.NMS.ActiveMQ.Commands;
using RestAPI;
using CommunicationLib.Model;

using System.Diagnostics;
using CommunicationLib.Exception;
using NLog;

namespace CommunicationLib
{
    /// <summary>
    ///  The CommunicationManager handles messages coming from the server.
    ///  It can register a client as data receiver <see cref="IDataReceiver"/> to update it via callback.
    ///  Invocation of the RegisterClient-Method makes the CommunicationManager start a connection to the message broker.
    ///  Client registration can be done for admins as well.
    ///  In this case the CommunicationManager will subribe to all admin related message topics.
    ///  It's up to the CommunicationManager to determine which data is relevant for its client.
    /// </summary>
    public class CommunicationManager
    {
        private static Logger logger = LogManager.GetCurrentClassLogger();
        // protocol operations
        private const string DEFINE_OPERATION = "def";
        private const string UPDATE_OPERATION = "upd";
        private const string DELETE_OPERATION = "del";

        // default protocol topics
        const string WORKFLOW_TOPIC = "WORKFLOW_INFO";
        // default admin protocol topics
        const string USER_TOPIC = "USER_INFO";
        const string ROLE_TOPIC = "ROLE_INFO";
        const string FORM_TOPIC = "FORM_INFO";

        // type mapping
        private static Dictionary<string, Type> _dataStructures = new Dictionary<string, Type>
        {
            {"workflow", typeof(Workflow)}, 
            {"item", typeof(Item)},
            {"user", typeof(User)},
            {"role", typeof(Role)},
            {"form", typeof(Form)}
        };

        // RestRequester for server connection
        private IRestRequester _sender;
        // client referenz
        private IDataReceiver _myClient;

        // jms attributes
        private IConnection _connection;
        private IConnectionFactory _connectionFactory;
        private ISession _session;
        // client subscriptions <topicName, consumer>
        private Dictionary<string ,IMessageConsumer> _messageSubs;

        /// <summary>
        ///  Constructor for CommunicationManager. 
        /// </summary>
        /// <param name="sender">the rest connector</param>
        /// <param name="myClient">the registeres client for callback</param>
        /// <param name="brokerAddress">the broker url</param>
        public CommunicationManager(IRestRequester sender, IDataReceiver myClient, string brokerAddress)
        {
            _sender = sender;
            _myClient = myClient;
            _messageSubs = new Dictionary<string, IMessageConsumer>();

            InitializeConnection(brokerAddress);
        }

        /// <summary>
        ///  Initializes connection with handed address.
        /// </summary>
        /// <param name="brokerAddress">the broker url</param>
        internal void InitializeConnection(string brokerAddress)
        {
            if (brokerAddress != null)
            {

                try
                {
                    // build connection to message broker (not started yet)
                    _connectionFactory = new ConnectionFactory(brokerAddress);
                    _connection = _connectionFactory.CreateConnection();
                    _session = _connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
                }
                catch (NMSConnectionException)
                {
                    throw new ServerNotRunningException();
                }
                catch (UriFormatException) 
                {
                    throw new ServerNotRunningException();
                }
            }
        }

        /// <summary>
        ///  Refreshes the connection with new broker address.
        /// </summary>
        /// <param name="brokerAddress">the broker url</param>
        internal void Refresh(string brokerAddress)
        {
            // build connection to message broker (not started yet)
            if (_connection != null) 
            {
                _connection.Stop();
            }
            InitializeConnection(brokerAddress);
        }

        /// <summary>
        ///  Register the client which uses the ComLib to CommunicationManager.
        ///  Starts a connection to the message broker.
        ///  Default topics are:
        ///     WORKFLOW_INFO -> for all workflow changes and definitions
        ///     USER_INFO     -> for all user changes and definitions
        ///     ROLE_INFO     -> for all role changes and definitions
        ///     FORM_INFO     -> for all form changes and definitions
        /// </summary>
        /// <param name="isAdmin">if true, client will be set as admin</param>
        public void RegisterClient(bool isAdmin)
        {
            // workflow topic
            IMessageConsumer messageConsumer = _session.CreateConsumer(new ActiveMQTopic(WORKFLOW_TOPIC));
            messageConsumer.Listener += OnMessageReceived;
            _messageSubs.Add(WORKFLOW_TOPIC, messageConsumer);

            if (isAdmin) 
            {
                // user topic
                messageConsumer = _session.CreateConsumer(new ActiveMQTopic(USER_TOPIC));
                messageConsumer.Listener += OnMessageReceived;
                _messageSubs.Add(USER_TOPIC, messageConsumer);
                // role topic
                messageConsumer = _session.CreateConsumer(new ActiveMQTopic(ROLE_TOPIC));
                messageConsumer.Listener += OnMessageReceived;
                _messageSubs.Add(ROLE_TOPIC, messageConsumer);
                // form topic
                messageConsumer = _session.CreateConsumer(new ActiveMQTopic(FORM_TOPIC));
                messageConsumer.Listener += OnMessageReceived;
                _messageSubs.Add(FORM_TOPIC, messageConsumer);
            }

            _connection.Start();
        }

        /// <summary>
        ///  Unregisters the client which uses the ComLib from CommunicationManager.
        ///  All message subscriptions will be deleted.
        ///  The broker connection will be stopped.
        /// </summary>
        public void UnregisterClient()
        {
            _messageSubs.Clear();
            _connection.Stop();
        }

        /// <summary>
        ///  Call-back method.
        ///  Is invoked when the messageConsumer receives a Message.
        ///  Provides ITextMessage and IMapMessages.
        ///  Calls the HandleRequest() method.
        /// </summary>
        /// <param name="msg">received message</param>
        public void OnMessageReceived(IMessage msg)
        {
            if (msg is ITextMessage)
            {
                ITextMessage tm = msg as ITextMessage;
                logger.Info(" Received Message='" + tm.Text + "'");
                HandleRequest(tm.Text);  
            }
            else if (msg is IMapMessage)
            {
                // ...there is no need for this(not yet..?)
            }
            else
            {
                logger.Warn("Received different message-Type: " + msg + "\n");
            }
        }

        /// <summary>
        ///  Invoked by the OnMessageReceived method.
        ///  Calls the rest requester.
        ///  The request information is retrieved from the given message string.
        ///  The restrequest returns the updated source.
        ///  This source will be sent to the client(IDataReceiver) by invoking a callback method.
        ///
        ///  Define and update operations result a GET-restrequest for the given source information.
        ///  Deletion operations obviously skip the restrequest, because there is no source for 'GET'.
        ///  The client(IDataReceiver) receives the necessary delete information via callback method(DataDeletion()).
        /// </summary>
        /// <param name="requestMsg">information string for rest-request</param>
        private void HandleRequest(string requestMsg)
        {
            string objId;
            //Type genericType;
            string operation;
            object requestedObj = null;

            /* msgParams[0] --> requested Type (workflow/item/user/role/form)
             * msgParams[1] --> server operation (def/upd/del)
             * msgParams[2] --> object identifier (id/username/...)
             */
            IList<string> msgParams = new List<string>();
            msgParams = requestMsg.Split('=');

            var genericType = _dataStructures[ msgParams[0] ];
            operation = msgParams[1];
            objId = msgParams[2];

            // deletion, update or definition
            if (operation.Equals(DELETE_OPERATION))
            {
                _myClient.DataDeletion(genericType, objId);
            }
            else if (operation.Equals(DEFINE_OPERATION) || operation.Equals(UPDATE_OPERATION))
            {
                try
                {
                    // determine type and do rest request
                    if (genericType == typeof(Workflow))
                    {
                        requestedObj = _sender.GetObject<Workflow>(objId);
                        _myClient.WorkflowUpdate((Workflow)requestedObj);

                        // register client for item updates from this new workflow
                        if (operation.Equals(DEFINE_OPERATION))
                        {
                            RegisterItemSource((Workflow)requestedObj);
                        }
                    }
                    else if (genericType == typeof(Item))
                    {
                        requestedObj = _sender.GetObject<Item>(objId);
                        _myClient.ItemUpdate((Item)requestedObj);
                    }
                    else if (genericType == typeof(User))
                    {
                        requestedObj = _sender.GetObject<User>(objId);
                        _myClient.UserUpdate((User)requestedObj);
                    }
                    else if (genericType == typeof(Role))
                    {
                        requestedObj = _sender.GetObject<Role>(objId);
                        _myClient.RoleUpdate((Role)requestedObj);
                    }
                    else if (genericType == typeof(Form))
                    {
                        requestedObj = _sender.GetObject<Form>(objId);
                        _myClient.FormUpdate((Form)requestedObj);
                    }
                }
                catch (BasicException e)
                {
                    // check actual exception
                    if (e is NoPermissionException)
                    {
                        _myClient.DataDeletion(genericType, objId);
                    }

                    if (e is LogInException)
                    {
                        UnregisterClient();
                    }
                    _myClient.HandleError(e);
                }
            }
            else
            {
                logger.Warn("Message protocol violation! Unknown operation: " + operation);
            }
        }

        /// <summary>
        ///  Subscribes to an general item messaging topic for the given workflow.
        ///  Item activities from this workflow will be received by the CommunicationManager.
        /// </summary>
        /// <param name="itemSource">the workflow form which the item info shall be noticed</param>
        public void RegisterItemSource(Workflow itemSource)
        {
            string topicName;

            // Create fitting topic
            topicName = "ITEMS_FROM_" + itemSource.id;
            if (!_messageSubs.ContainsKey(topicName))
            {
                logger.Info("Registration ItemSource='" + topicName + "'");
                IMessageConsumer messageConsumer = _session.CreateConsumer(new ActiveMQTopic(topicName));
                messageConsumer.Listener += OnMessageReceived;
                _messageSubs.Add(topicName, messageConsumer);
            }
        }
    }
}