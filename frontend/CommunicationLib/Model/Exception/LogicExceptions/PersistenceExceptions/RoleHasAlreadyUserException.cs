using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class RoleHasAlreadyUserException : AlreadyExistsException
    {
        private int _number = 1124;
        new public int number { get { return _number; } }

        public RoleHasAlreadyUserException()
        {

        }

        public RoleHasAlreadyUserException(string message)
            : base(message)
        {
        }

        public RoleHasAlreadyUserException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
