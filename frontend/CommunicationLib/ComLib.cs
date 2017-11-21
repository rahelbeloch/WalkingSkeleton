using RestAPI;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib
{
    /// <summary>
    ///  Class represents the CommunicationLib for clients. They keep an instance themselves and can Register/Unregister or Login/Logout in the library.
    /// </summary>
    public class ComLib
    {
        // the associated client
        private IDataReceiver _myClient;

        /// <summary>
        /// REST interface.
        /// </summary>
        public IRestRequester Sender
        { 
            get 
            { 
                return _sender; 
            }
        }
        private IRestRequester _sender;

        /// <summary>
        /// Messanger-object.
        /// </summary>
        public CommunicationManager Listener
        { 
            get 
            { 
                return _listener; 
            }
        }
        private CommunicationManager _listener;

        /// <summary>
        /// Constructor to set the associated client.
        /// </summary>
        /// <param name="myClient">the associated client for callback</param>
        /// <param name="clientId">admin or user</param>
        /// <param name="serverAdress">the address of the server</param>
        /// <param name="brokerAdress">the address of the broker</param>
        public ComLib(IDataReceiver myClient, String clientId, String serverAdress, String brokerAdress)
        {
            _myClient = myClient;
            _sender = new RestRequester(clientId, serverAdress);
            _listener = new CommunicationManager(_sender, myClient, brokerAdress);
        }

        /// <summary>
        ///  Register or login to ComLib. 
        /// </summary>
        /// <param name="username">Name of the user logged in client</param>
        /// <param name="password">Password of the user logged in client</param>
        public void Login(string username, String password)
        {
            _sender.InitializeClientProperties(username, password);
            _sender.CheckUser();
            _listener.RegisterClient(true);
        }

        /// <summary>
        ///  Unregister or logout from ComLib.
        /// </summary>
        public void Logout()
        {
            _sender.DeleteClientProperties();
            _listener.UnregisterClient();
        }

        /// <summary>
        /// Method to call refresh in the sender/restRequester.
        /// </summary>
        /// <param name="serverAddress">the new server address</param>
        public void RefreshServer(string serverAddress){
            _sender.Refresh(serverAddress);
        }

        /// <summary>
        /// Method to call refresh in the listener/CommunicationManager.
        /// </summary>
        /// <param name="brokerAddress">the new broker address</param>
        public void RefreshBroker(string brokerAddress){
            _listener.Refresh(brokerAddress);
        }
    }
}