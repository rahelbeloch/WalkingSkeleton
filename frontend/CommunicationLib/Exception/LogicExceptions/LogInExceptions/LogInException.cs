using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class LogInException : LogicException
    {
        private int _number = 11100;
        public int number { get { return _number; } }

        public LogInException()
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
