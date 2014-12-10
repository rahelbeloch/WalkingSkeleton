using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class WrongUsernameException : LogInException
    {
        private int _number = 11120;
        new public int number { get { return _number; } }

        public WrongUsernameException()
            : base("Der Nutzername war falsch.")
        {

        }

        public WrongUsernameException(string message)
            : base(message)
        {
        }

        public WrongUsernameException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
