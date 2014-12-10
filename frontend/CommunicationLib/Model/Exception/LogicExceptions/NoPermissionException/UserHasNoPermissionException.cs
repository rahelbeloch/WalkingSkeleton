using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class UserHasNoPermissionException : LogInException
    {
        private int _number = 11321;
        new public int number { get { return _number; } }

        public UserHasNoPermissionException()
            : base("Der Nutzername war falsch.")
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
