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
            {"workflow", typeof(AbstractWorkflow)}, 
            {"item", typeof(AbstractItem)},
            {"user", typeof(AbstractUser)}
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
        IDataReceiver myClient;
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

        public CommunicationManager()
        {   
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

        // Testing does not work, if the message doesn't follow the protocol definition
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
            MethodInfo method = typeof( RestRequester ).GetMethod( methodName );
            MethodInfo genericMethod = method.MakeGenericMethod( genericType );
            // Call the dynamic generic generated method with parameterlist (2. param); parent of called method is static, not an instance (1.param)
            resultType = genericMethod.Invoke(null, new object[] { id });

            //wrapping
            rWrapInstance = wrap(genericType, resultType);

            //send wrapper-Instance to Client
            if(genericType == typeof(AbstractWorkflow))
            {
                RegistrationWrapper<AbstractWorkflow> workflowWrap = (RegistrationWrapper<AbstractWorkflow>)rWrapInstance;
                myClient.WorkflowUpdate(workflowWrap);
            }
            else if (genericType == typeof(AbstractItem))
            {
                RegistrationWrapper<AbstractItem> itemWrap = (RegistrationWrapper<AbstractItem>)rWrapInstance;
                myClient.ItemUpdate(itemWrap);
            }
            else if (genericType == typeof(AbstractUser))
            {
                RegistrationWrapper<AbstractUser> userWrap = (RegistrationWrapper<AbstractUser>)rWrapInstance;
                myClient.UserUpdate(userWrap);
            }
        }

        /// <summary>
        /// Creates an instance of RegistrationWrapper with dynamic generic type
        /// </summary>
        /// <param name="genericType">generic type for the RegistrationWrapper</param>
        /// <param name="o">object to pack</param>
        /// <returns>Wrapped object</returns>
        private object wrap(Type genericType, object o)
        {
            var wrap = typeof(RegistrationWrapper<>);
            Type[] typeArgs = { genericType, typeof(CommunicationManager) };
            var makeme = wrap.MakeGenericType(typeArgs);
            object rWrapInstance = Activator.CreateInstance(makeme);

            return rWrapInstance;
        }

        /*
         * Task to subscribe to messaging topic which affects the given object/instance.
         *  @ rw - object of interest
         *  @ callback - function in the client to call by update of rw
         */
        public void Register(Object rw, Func<Object> callback)
        {

        }

        /*
         * for testing
         */
        class Program
        {
            static void Main(string[] args)
            {
                CommunicationManager manager = new CommunicationManager();
                Console.WriteLine("Communication-Manager waiting for messages...\n-- press ENTER to stop --\n");
                Console.ReadKey();
            }
        }
    }
}
