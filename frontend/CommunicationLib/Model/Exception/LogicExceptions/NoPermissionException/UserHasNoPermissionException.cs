using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class UserHasNoPermissionException : NoPermissionException
    {
        private int _number = 11310;
        new public int number { get { return _number; } }

        public UserHasNoPermissionException()
            : base("Der Nutzer hat keine Erlaubnis.")
        {

        }

        public UserHasNoPermissionException(string message)
            : base(message)
        {
        }

        public UserHasNoPermissionException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
