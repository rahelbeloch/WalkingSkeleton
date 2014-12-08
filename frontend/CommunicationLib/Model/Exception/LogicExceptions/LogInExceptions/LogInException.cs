using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class LogInException : LogicException
    {
        private int _number = 11100;
        new public int number { get { return _number; } }

        public LogInException()
            :base("Der LogIn war nicht erfolgreich")
        {

        }

        public LogInException(string message)
         : base(message)
        {
        }

        public LogInException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
