using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class WrongPwException : LogInException
    {
        private int _number = 11110;
        new public int number { get { return _number; } }

        public WrongPwException()
            : base("Das Passwort war falsch.")
        {

        }

        public WrongPwException(string message)
         : base(message)
        {
        }

        public WrongPwException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
