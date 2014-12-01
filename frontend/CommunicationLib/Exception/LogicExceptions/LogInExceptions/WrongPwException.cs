using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class WrongPwException : LogInException
    {
        private int _number = 11110;
        public int number { get { return _number; } }

        public WrongPwException()
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
