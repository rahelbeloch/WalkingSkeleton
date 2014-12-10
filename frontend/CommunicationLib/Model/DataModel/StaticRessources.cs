﻿using CommunicationLib.Exception;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{

    public class Constants
    {
        public static String serverUrl = "http://localhost:18887";
        public static String MODEL_NAMESPACE = "CommunicationLib.Model";
    }

    public class ErrorMessageMapper
    {
        private static Dictionary<int, Type> errorMessages = new Dictionary<int, Type>()
        {          
            // error if the error from server is not known/implemented
            {9999, typeof(UnknownException)},

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
            {11220, typeof(AlreadyExistsException)},
                {11222, typeof(UserAlreadyExistsException)},
            {11230, typeof(NoPermissionException)},
            {11240, typeof(ElementChangedException)},
            {11250, typeof(DoesntExistsException)},
                {11251, typeof(UserNotExistException)},
                {11252, typeof(WorkflowNotExistException)},
                {11253, typeof(ItemNotExistException)},
            {11260, typeof(RoleException)},
                {11261, typeof(RoleHasAlreadyUserException)},
                {11262, typeof(RoleNotExistentException)},
                {11263, typeof(UserHasAlreadyRoleException)},
            
                //Error codes for connection errors
            {12000,typeof(ConnectionException)},

            //Error codes for messaging errors
            {12100,typeof(MessagingException)},
            
            //Error codes for rest errors
            {12200,typeof(RestException)}

            //{12210,typeof(ServerNotRunningException)}
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
