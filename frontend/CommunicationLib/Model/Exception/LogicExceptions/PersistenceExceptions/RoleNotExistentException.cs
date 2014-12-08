using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class RoleNotExistentException : DoesntExistsException
    {
        private int _number = 11253;
        new public int number { get { return _number; } }

        public RoleNotExistentException()
        {

        }

        public RoleNotExistentException(string message)
         : base(message)
        {
        }

        public RoleNotExistentException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
