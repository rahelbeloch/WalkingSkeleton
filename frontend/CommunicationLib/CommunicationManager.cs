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

namespace CommunicationLib
{

    public class CommunicationManager
    {
        //type mapping
        private static Dictionary<string, Type> _dataStructures = new Dictionary<string, Type>
        {
            {"workflow", typeof(Workflow)}, 
            {"item", typeof(Item)},
            {"user", typeof(User)}
        };
        //funktion mapping 
        private static Dictionary<string, string> _funcMapping = new Dictionary<string, string>
        {
            {"def", "GetObject"},
            {"udp", "GetObject"},
            {"del", "DeleteObject"}
        };

        //client referenz
        private IDataReceiver _myClient;
        //default topic for workflow information
        const string WORKFLOW_TOPIC = "WORKFLOW_INFO";
        //default topic for user information (admin)
        const string USER_TOPIC = "USER_INFO";

        //jms attributes
        private IConnection _connection;
        private IConnectionFactory _connectionFactory;
        private ISession _session;
        //client subscriptions <topicName, consumer>
        private Dictionary<string ,IMessageConsumer> _messageSubs;

        public CommunicationManager()
        {
            _messageSubs = new Dictionary<string, IMessageConsumer>();

            //build connection to message broker (not started yet)
            _connectionFactory = new ConnectionFactory(Constants.BROKER_URL);
            _connection = _connectionFactory.CreateConnection();
            _session = _connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
        }

        /// <summary>
        ///  Register the client which uses the ComLib to CommunicationManager.
        ///  Starts a connection to the message broker.
        ///  Loads all message subscriptions for the client from the server.
        ///  If there are no subscriptions the default topics will be set.
        ///  Default topics are:
        ///     WORKFLOW_INFO -> for all workflow changes and definitions
        ///     USER_INFO     -> for all user changes and defintions
        ///  (client calls this method if login works)
        /// </summary>
        /// <param name="myClient">the client to register for</param>
        public void registerClient(IDataReceiver myClient)
        {
            // TODO: alle Subs, die er vorher schonmal hatte wieder default setzen bei Neuregistrierung
            // Beim abmelden (unregister) alle MessageSubs im User als Liste speichern (per Rest) und bei neuem Registrieren einfach holen und setzen
            this._myClient = myClient;

            //default topic subscriptions
            IMessageConsumer messageConsumer = _session.CreateConsumer(new ActiveMQTopic(WORKFLOW_TOPIC));
            messageConsumer.Listener += OnMessageReceived;
            _messageSubs.Add(WORKFLOW_TOPIC, messageConsumer);

            //TODO check if client is admin client
            if (true) 
            {
                messageConsumer = _session.CreateConsumer(new ActiveMQTopic(USER_TOPIC));
                messageConsumer.Listener += OnMessageReceived;
                _messageSubs.Add(USER_TOPIC, messageConsumer);
            }

            _connection.Start();
        }

        /// <summary>
        ///  Unregisters the client which uses the ComLib from CommunicationManager.
        ///  All message subscriptions will be deleted.
        ///  The broker connection will be stopped.
        ///  (Client calls this method with logout)
        /// </summary>
        public void unregisterClient()
        {
            this._myClient = null;
            _messageSubs.Clear();
            _connection.Stop();
        }

        /// <summary>
        /// Call-back method.
        /// Is invoked when the messageConsumer receives a Message.
        /// Provides ITextMessage and IMapMessages.
        /// Calls HandleRequest() the method.
        /// </summary>
        /// <param name="msg">received message</param>
        public void OnMessageReceived(IMessage msg)
        {
            if (msg is ITextMessage)
            {
                ITextMessage tm = msg as ITextMessage;
                //Logging on Console 
                System.Diagnostics.Trace.WriteLine("TextMessage: ID=" + tm.GetType() + "\n" + tm.Text + "\n");
                HandleRequest(tm.Text);  
            }
            else if (msg is IMapMessage)
            {
                // ...there is no need for this(not yet..?)
            }
            else
            {
                Console.WriteLine("\ndifferent message-Type: " + msg + "\n");
            }
        }

        /// <summary>
        /// Invoked by the OnMessageReceived method.
        /// Uses reflection for dynamic rest requests.
        /// The request information is retrieved from the given message string.
        /// </summary>
        /// <param name="requestMsg">information string for rest-request</param>
        private void HandleRequest(string requestMsg)
        {
            // object identifier
            Int32 id;
            string userName;

            // for reflection
            Type genericType;
            string methodName;
            object requestedObj;
            object [] args;

            /* msgParams[0] --> requested Type (workflow/item/user)
             * msgParams[1] --> server operation (def/upd/del)
             * msgParams[2] --> object identifier (id/username)
             */
            IList<string> msgParams = new List<string>();
            msgParams = requestMsg.Split('=');

            genericType = _dataStructures[ msgParams[0] ];
            methodName = _funcMapping[ msgParams[1] ];

            // which identifier is needed ?
            if (genericType == typeof(User))
            {
                userName = msgParams[2];
                args = new object [1] { userName };
            } 
            else
            {
                Int32.TryParse(msgParams[2], out id);
                args = new object [1] { id };
            }

            /* Problem: generic method can not be called with dynamic generics
             * -> Reflection: deciding during runtime which generic is placed in which method
             */
            MethodInfo method = typeof( RestRequester ).GetMethod( methodName );
            MethodInfo genericMethod = method.MakeGenericMethod( typeof(Workflow) );

            /* Invoke the dynamically generated method
             * 1. param is the method location
             * 2. param is a object list of params for this invocation 
             * If client has no access to requested resource,
             * server throws an exception --> abortion of method
             */
            RestRequester obj = new RestRequester();
            requestedObj = genericMethod.Invoke(obj, args);

            // Client update
            if (genericType == typeof(Workflow))
            {
                _myClient.WorkflowUpdate((Workflow)requestedObj);
                
                // register client for item updates from this new workflow
                if (methodName.Equals("def")) 
                {
                    Register((Workflow)requestedObj);
                }
            }
            else if (genericType == typeof(Item))
            {
                _myClient.ItemUpdate((Item)requestedObj);
            }
            else if (genericType == typeof(User))
            {
                _myClient.UserUpdate((User)requestedObj);
            }
        }

        /// <summary>
        /// Subscribes to an general item messaging topic for the given workflow.
        /// item activities from this workflow will be received by the CommunicationManager
        /// </summary>
        /// <param name="itemSource">the workflow form which the item info shall be noticed</param>
        public void Register(Workflow itemSource)
        {
            string topicName;
            
            // Create fitting topic
            topicName = "ITEMS_FROM_" + itemSource.id;
            IMessageConsumer messageConsumer = _session.CreateConsumer(new ActiveMQTopic(topicName));
            messageConsumer.Listener += OnMessageReceived;
            _messageSubs.Add(topicName, messageConsumer);
        }
    }
}