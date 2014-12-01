using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class LogicException : BasicException
    {
        private int _number = 11000;
        public int number { get { return _number; } }

         public LogicException()
        {

        }

        public LogicException(string message)
         : base(message)
        {
        }

        public LogicException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
