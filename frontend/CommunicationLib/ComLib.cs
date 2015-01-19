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
        private IRestRequester _sender;
        public IRestRequester sender
        { 
            get 
            { 
                return _sender; 
            }
        }

        /// <summary>
        /// Messanger-object.
        /// </summary>
        private CommunicationManager _listener;
        public CommunicationManager listener
        { 
            get 
            { 
                return _listener; 
            }
        }

        /// <summary>
        ///  Constructor to set the associated client.
        /// </summary>
        /// <param name="myClient"></param>
        /// <param name="clientId"></param>
        public ComLib(IDataReceiver myClient, String clientId, String serverAdress)
        {
            _myClient = myClient;
            _sender = new RestRequester(clientId, serverAdress);
            _listener = new CommunicationManager(_sender, myClient);
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
    }
}