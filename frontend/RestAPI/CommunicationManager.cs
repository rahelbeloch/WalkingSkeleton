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

namespace CommunicationLib
{

    class WorkflowTestKlasse
    {
    }

    class ItemTestKlasse
    {
    }

    class UserTestKlasse
    {
    }

    public class CommunicationManager
    {
        private static Dictionary<string, Type> dataStructure;
        private static Dictionary<string, string> funcMapping;
        //TODO: Client referenz
        //private Client myClient;

        //default topic for server information
        const string DEFAULT_TOPIC = "NEW_WORKFLOW_DEF";

        //jms attributes
        private IConnection connection;
        private IConnectionFactory connectionFactory;
        private ISession session;
        //TODO: Lists of consumers for multiple subscribtions
        private IMessageConsumer messageConsumer;
        //'localhost' is for testing only  
        private const string BROKER_URL = "tcp://localhost:61616";

        public CommunicationManager()
        {
            //Type Mapping
            dataStructure = new Dictionary<string, Type>();
            dataStructure.Add("workflow", typeof(WorkflowTestKlasse));
            dataStructure.Add("item", typeof(ItemTestKlasse));
            dataStructure.Add("user", typeof(UserTestKlasse));
            
            //Funktion Mapping
            funcMapping = new Dictionary<string, string>();
            funcMapping.Add("get", "getObject");
            funcMapping.Add("def", "postObject");
            funcMapping.Add("upd", "updateObject");
            funcMapping.Add("del", "deleteObject");

            //build connection to message broker
            connectionFactory = new ConnectionFactory();
            connection = connectionFactory.CreateConnection();
            session = connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
            //default topic subscription
            messageConsumer = session.CreateConsumer(new ActiveMQTopic(DEFAULT_TOPIC));
            messageConsumer.Listener += OnMessageReceived;
            connection.Start();
        }

        /* Call-back method
         * Is invoked when the messageConsumer receives a new Message.
         */
        public void OnMessageReceived(IMessage msg)
        {
            if (msg is ITextMessage)
            {
                ITextMessage tm = msg as ITextMessage;
                HandleRequest(tm.Text);
                //Logging on Console
                Console.WriteLine("TextMessage: ID=" + tm.GetType() + "\n" + tm.Text + "\n");
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

        private void HandleRequest(string requestMsg)
        {
            int id;
            Type genericType;
            Object resultType; 
            string methodName;
            IList<string> options = new List<string>();
            
            // options[0] --> requested Type; options[1] --> server operation; options[2] --> object identifier
            options = requestMsg.Split('=');
            genericType = dataStructure[options[0]];
            methodName = funcMapping[options[1]];
            Int32.TryParse(options[2], out id);
            
            // Reflection: generic method can not be called with dynamic generics (means deciding during runtime which generic is placed in)
            MethodInfo method = typeof( RestRequester ).GetMethod( methodName );
            MethodInfo generic = method.MakeGenericMethod( genericType );
            // Call the dynamic generic generated method with parameterlist (2. param); parent of called method is static, not an instance (1.param)
            resultType = generic.Invoke(null, new object[] { id });

            //TODO: resultType an Client senden
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
