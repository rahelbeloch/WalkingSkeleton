using CommunicationLib.Exception;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{

    public class Constants
    {
        public static String serverUrl = "http://172.26.38.101:8080";
    }

    public class ErrorMessageMapper
    {
        private static Dictionary<int, Type> errorMessages = new Dictionary<int, Type>()
        {   
                       
            // Error codes
            {10000, typeof(BasicException)},
            
            //Error codes for logic errors
            {11000, typeof(LogicException)},
            
            //Error codes for logIn errors
            {11100, typeof(LogInException)},

            {11110, typeof(WrongPwException)},
            {11120, typeof(WrongUsernameException)},

            //Error code for persistence errors
            {11200, typeof(PersistenceException)},
            
            {11210, typeof(IncompleteEleException)},
            {11220, typeof(EleAlreadyExistsException)},
            {11230, typeof(NoPermissionException)},
            {11240, typeof(ElementChangedException)},
            {11250, typeof(DoesntExistsException)},
            {11260, typeof(UserNotExistException)},
            
            //Error codes for connection errors
            {12000,typeof(ConnectionException)},

            //Error codes for messaging errors
            {12100,typeof(MessagingException)},
            
            //Error codes for rest errors
            {12200,typeof(RestException)},

            {12210,typeof(ServerNotRunningException)}
            
        };
    }


}
