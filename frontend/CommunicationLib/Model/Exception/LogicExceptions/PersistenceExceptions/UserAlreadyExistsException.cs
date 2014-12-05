using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class UserAlreadyExistsException : AlreadyExistsException
    {
        private int _number = 11222;
        public int number { get { return _number; } }

        public UserAlreadyExistsException()
        {

        }

        public UserAlreadyExistsException(string message)
            : base(message)
        {
        }

        public UserAlreadyExistsException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
