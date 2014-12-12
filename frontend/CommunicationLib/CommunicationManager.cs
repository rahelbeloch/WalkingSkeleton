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
        //default topic for server information
        const string DEFAULT_TOPIC = "WORKFLOW_INFO";
        //jms attributes
        private IConnection _connection;
        private IConnectionFactory _connectionFactory;
        private ISession _session;
        //client subscriptions <topicName, consumer>
        private Dictionary<string ,IMessageConsumer> _messageSubs;

        public CommunicationManager()
        {
            _messageSubs = new Dictionary<string, IMessageConsumer>();

            //build connection to message broker
            _connectionFactory = new ConnectionFactory(Constants.BROKER_URL);
            _connection = _connectionFactory.CreateConnection();
            _session = _connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
        }

        /// <summary>
        ///  Register the client, which uses the ComLib to CommunicationManager. 
        ///  Client calls this method if login works.
        /// </summary>
        /// <param name="myClient">The client to register</param>
        public void registerClient(IDataReceiver myClient)
        {
            // TODO: alle Subs, die er vorher schonmal hatte wieder default setzen bei Neuregistrierung
            // Beim abmelden (unregister) alle MessageSubs im User als Liste speichern (per Rest) und bei neuem Registrieren einfach holen und setzen
            this._myClient = myClient;

            //default topic subscription
            IMessageConsumer messageConsumer = _session.CreateConsumer(new ActiveMQTopic(DEFAULT_TOPIC));
            messageConsumer.Listener += OnMessageReceived;
            _messageSubs.Add("WORKFLOW_INFO", messageConsumer);

            _connection.Start();
        }

        /// <summary>
        ///  Unregisters the client, which uses the ComLib, from CommunicationManager.
        ///  Client calls this method with logout.
        /// </summary>
        public void unregisterClient()
        {
            this._myClient = null;

            // remove all Topics/Consumer from messageSubs
            _messageSubs.Clear();
            _connection.Stop();
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
                System.Diagnostics.Trace.WriteLine("TextMessage: ID=" + tm.GetType() + "\n" + tm.Text + "\n");
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
            Int32 id;
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
            MethodInfo genericMethod = method.MakeGenericMethod( typeof(Workflow) );
            // Call the dynamic generic generated method with parameterlist (2. param); parent of called method is static, not an instance (1.param)

            RestRequester obj = new RestRequester();
            object[] args = new object[] { id };

            resultType = genericMethod.Invoke(obj, args);
            // if client has no access to requested resource, exception is thrown --> abortion of method

            //wrapping
            rWrapInstance = Wrap(genericType, resultType);

            //send wrapper-Instance to Client
            if (genericType == typeof(Workflow))
            {
                RegistrationWrapper<Workflow> workflowWrap = (RegistrationWrapper<Workflow>)rWrapInstance;
                _myClient.WorkflowUpdate(workflowWrap);
                
                // register client for items from new workflows
                if (methodName.Equals("def")) 
                {
                    Register(workflowWrap.myObject);
                }
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
            Type[] typeArgs = { genericType };
            var makeme = wrap.MakeGenericType(typeArgs);

            return Activator.CreateInstance(makeme, new object[] { o, this }); 
        }

        /// <summary>
        /// Subscribes to messaging topic which affects the given object/instance.
        /// </summary>
        /// <param name="rw">object of interest</param>
        /// <param name="callback">function in the client to call by update of rw</param>
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