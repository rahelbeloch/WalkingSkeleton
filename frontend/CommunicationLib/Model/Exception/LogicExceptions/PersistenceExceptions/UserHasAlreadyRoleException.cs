﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class UserHasAlreadyRoleException : RoleException
    {
        private int _number = 11263;
        new public int number { get { return _number; } }

        public UserHasAlreadyRoleException()
        {

        }

        public UserHasAlreadyRoleException(string message)
            : base(message)
        {
        }

        public UserHasAlreadyRoleException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
