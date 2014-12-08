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
            {"get", "getObject"},
            {"def", "postObject"},
            {"udp", "updateObject"},
            {"del", "deleteObject"}
        };

        //client referenz
        private IDataReceiver _myClient;
        //default topic for server information
        const string DEFAULT_TOPIC = "WORKFLOW_INFO";
        //jms attributes
        private IConnection _connection;
        private IConnectionFactory _connectionFactory;
        private ISession _session;
        //client subscriptions <topicName, consumer>
        private Dictionary<string ,IMessageConsumer> _messageSubs;
        //'localhost' is for testing only  
        private const string BROKER_URL = "tcp://localhost:61616";

        public CommunicationManager(IDataReceiver myClient)
        {
            this._myClient = myClient;

            _messageSubs = new Dictionary<string, IMessageConsumer>();

            //build connection to message broker
            _connectionFactory = new ConnectionFactory(BROKER_URL);
            _connection = _connectionFactory.CreateConnection();
            _session = _connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
            //default topic subscription
            IMessageConsumer messageConsumer = _session.CreateConsumer(new ActiveMQTopic(DEFAULT_TOPIC));
            messageConsumer.Listener += OnMessageReceived;
            _messageSubs.Add("WORKFLOW_INFO", messageConsumer);
            
            _connection.Start();
        }

        /* Call-back method
         * Is invoked when the messageConsumer receives a new Message.
         */
        public void OnMessageReceived(IMessage msg)
        {
            if (msg is ITextMessage)
            {
                ITextMessage tm = msg as ITextMessage;
                //Logging on Console 
                Console.WriteLine("TextMessage: ID=" + tm.GetType() + "\n" + tm.Text + "\n");
                HandleRequest(tm.Text);  
            }
            else if (msg is IMapMessage)
            {
                // ...there is no need for this(not yet)
            }
            else
            {
                Console.WriteLine("\ndifferent message-Type: " + msg + "\n");
            }
        }

        /// <summary>
        /// Uses reflection for dynamic rest requests.
        /// </summary>
        /// <param name="requestMsg">information string for rest-request</param>
        private void HandleRequest(string requestMsg)
        {
            int id;
            Type genericType;
            object resultType; 
            string methodName;
            IList<string> options = new List<string>();
            object rWrapInstance;
            
            // options[0] --> requested Type; options[1] --> server operation; options[2] --> object identifier
            options = requestMsg.Split('=');
            genericType = _dataStructures[options[0]];
            methodName = _funcMapping[options[1]];
            Int32.TryParse(options[2], out id);

            // Reflection: generic method can not be called with dynamic generics (means deciding during runtime which generic is placed in)
            MethodInfo method = typeof( InternalRequester ).GetMethod( methodName );
            MethodInfo genericMethod = method.MakeGenericMethod( genericType );
            // Call the dynamic generic generated method with parameterlist (2. param); parent of called method is static, not an instance (1.param)
            resultType = genericMethod.Invoke(null, new object[] { id });

            //wrapping
            rWrapInstance = Wrap(genericType, resultType);

            //send wrapper-Instance to Client
            if (genericType == typeof(Workflow))
            {
                RegistrationWrapper<Workflow> workflowWrap = (RegistrationWrapper<Workflow>)rWrapInstance;
                _myClient.WorkflowUpdate(workflowWrap);
            }
            else if (genericType == typeof(Item))
            {
                RegistrationWrapper<Item> itemWrap = (RegistrationWrapper<Item>)rWrapInstance;
                _myClient.ItemUpdate(itemWrap);
            }
            else if (genericType == typeof(User))
            {
                RegistrationWrapper<User> userWrap = (RegistrationWrapper<User>)rWrapInstance;
                _myClient.UserUpdate(userWrap);
            }
        }

        /// <summary>
        /// Creates an instance of RegistrationWrapper with dynamic generic type
        /// </summary>
        /// <param name="genericType">generic type for the RegistrationWrapper</param>
        /// <param name="o">object to pack</param>
        /// <returns>Wrapped object</returns>
        private object Wrap(Type genericType, object o)
        {
            var wrap = typeof(RegistrationWrapper<>);
            Type[] typeArgs = { genericType, typeof(CommunicationManager) };
            var makeme = wrap.MakeGenericType(typeArgs);
            object rWrapInstance = Activator.CreateInstance(makeme);

            return rWrapInstance;
        }

        /// <summary>
        /// Subscribes to messaging topic which affects the given object/instance.
        /// </summary>
        /// <param name="rw">object of interest</param>
        /// <param name="callback">function in the client to call by update of rw</param>
        public void Register(Object rw, Func<Object> callback)
        {
            // gibt es nur den Fall, dass Register auf einem Workflow aufgerufen wird?
            // --> andernfalls viel mehr Topics; für jede Änderung eigene Topics --> viel mehr Threads

            int id;
            string topicName;
            Workflow currWorkflow;

            // find out which object type DataModel is
            if (rw.GetType() == typeof(Workflow))
            {
                currWorkflow = (Workflow)rw;
                id = currWorkflow.id;
                // Create fitting topic
                topicName = "ITEMS_FROM_" + id;
                IMessageConsumer messageConsumer = _session.CreateConsumer(new ActiveMQTopic(topicName));
                messageConsumer.Listener += OnMessageReceived;
                _messageSubs.Add(topicName, messageConsumer);
            }
        }
    }
}