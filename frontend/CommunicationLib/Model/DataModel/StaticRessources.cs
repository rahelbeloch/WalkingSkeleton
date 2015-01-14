using CommunicationLib.Exception;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CommunicationLib.Model;
using Newtonsoft.Json;

namespace CommunicationLib.Model
{

    public class Constants
    {
        public static String SERVER_URL = "http://localhost:18887";
        public static String MODEL_NAMESPACE = "CommunicationLib.Model";
        public static String BROKER_URL = "tcp://localhost:61616";

        public static JsonSerializerSettings JSON_SETTINGS = new JsonSerializerSettings
        {
            TypeNameHandling = TypeNameHandling.Auto,
            Formatting = Formatting.Indented,
            Binder = new CustomSerializationBinder()
        };
    }

    public class ErrorMessageMapper
    {
        public static Dictionary<int, Type> errorMessages
        {
            get
            {
                return _errorMessages;
            }
        }

        private static Dictionary<int, Type> _errorMessages = new Dictionary<int, Type>()
        {          
            // error if the error from server is not known/implemented
            {9999, typeof(UnknownException)},

            // Error codes
            {10000, typeof(BasicException)},
            
            //Error codes for logic errors
            {11000, typeof(LogicException)},
            
            //Error codes for logIn errors
            {11100, typeof(LogInException)},

            //Error code for persistence errors
            {11200, typeof(PersistenceException)},
                

            {11220, typeof(AlreadyExistsException)},
                {11221, typeof(UserAlreadyExistsException)},
                {11222, typeof(RoleAlreadyExistsException)},
                {11223, typeof(UserHasAlreadyRoleException)},
            {11240, typeof(ElementChangedException)},
            {11250, typeof(NotExistentException)},
                {11251, typeof(UserNotExistentException)},
                {11252, typeof(WorkflowNotExistentException)},
                {11253, typeof(ItemNotExistentException)},
                {11254, typeof(StepNotExistentException)},
                {11255, typeof(FormNotExistentException)},
                {11256, typeof(RoleNotExistentException)},
            {11300, typeof(NoPermissionException)},
                {11310, typeof(UserHasNoPermissionException)},
                {11320, typeof(ItemNotForwardableException)},
                {11330, typeof(AdminRoleDeletionException)},
                {11340, typeof(LastAdminDeletedException)},
            {11400, typeof(IncompleteEleException)},
                
            //Error codes for connection errors
            {12000,typeof(ConnectionException)},
                {12100,typeof(RestException)},
                    {12110,typeof(JacksonException)},

                 {12200,typeof(MessagingException)},
                    {12210,typeof(ServerPublisherBrokerException)},

                {12300,typeof(ServerNotRunningException)}
        };

        public static Type GetErrorType(int code)
        {
            if (!errorMessages.ContainsKey(code))
            {
                code = 9999;
            }
            return errorMessages[code];
        }
    }
}
