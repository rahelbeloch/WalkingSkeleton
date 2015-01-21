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
    /// <summary>
    /// Class to hold some constant values often needed in the clients, or other parts of this system.
    /// </summary>
    public class Constants
    {
        /// <summary>
        /// The namespace of the data model needed for serialization.
        /// </summary>
        public static String MODEL_NAMESPACE = "CommunicationLib.Model";

        /// <summary>
        /// The pattern to validate server/broker address.
        /// </summary>
        public static String URLPATTERN = @"^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?";

        /// <summary>
        /// Name of the server address attribute in config files.
        /// </summary>
        public static String SERVER_ADDRESS_NAME = "ServerAddress";

        /// <summary>
        /// Name of the broker address attribute in config files.
        /// </summary>
        public static String BROKER_ADDRESS_NAME = "BrokerAddress";

        /// <summary>
        /// Some JSON settings for serialization and deseralization.
        /// </summary>
        public static JsonSerializerSettings JSON_SETTINGS = new JsonSerializerSettings
        {
            TypeNameHandling = TypeNameHandling.Auto,
            Formatting = Formatting.Indented,
            Binder = new CustomSerializationBinder()
        };
    }

    /// <summary>
    /// Class maps types of all exceptions in this program to its errorcodes.
    /// </summary>
    public class ErrorMessageMapper
    {
        /// <summary>
        /// Dictionary of errorcodes to types of exception.
        /// </summary>
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
            {11210, typeof(StorageFailedException)},
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
            {11500, typeof(InvalidWorkflowException)},
                {11510, typeof(WorkflowCyclesException)},
                {11520, typeof(WorkflowMustTerminateException)},
                {11530, typeof(InvalidFinalStepException​)},
                {11540, typeof(UnreachableStepException)},
                {11550, typeof(ExpectedOneStartStepException​)},
                {11560, typeof(ExpectedAtLeastOneFinalStepException)},
                {11570, typeof(ExpectedAtLeastOneActionException)},
                
            //Error codes for connection errors
            {12000,typeof(ConnectionException)},
                {12100,typeof(RestException)},
                    {12110,typeof(JacksonException)},

                 {12200,typeof(MessagingException)},
                    {12210,typeof(ServerPublisherBrokerException)},

                {12300,typeof(ServerNotRunningException)}
        };

        /// <summary>
        /// Delivers the data type of an exception code.
        /// </summary>
        /// <param name="code">the requested code</param>
        /// <returns>the type, fitting to the code</returns>
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