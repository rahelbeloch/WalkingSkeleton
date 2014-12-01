using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class LogInException : LogicException
    {
        public LogInException()
        {

        }

        public LogInException(string message, int number)
         : base(message, number)
        {
        }

        public LogInException(string message, System.Exception inner, int number)
            : base(message, inner, number)
        {
        }
    }
}
