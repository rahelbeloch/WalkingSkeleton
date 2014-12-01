using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class WrongPwException : LogInException
    {
        public WrongPwException()
        {

        }

        public WrongPwException(string message, int number)
         : base(message, number)
        {
        }

        public WrongPwException(string message, System.Exception inner, int number)
            : base(message, inner, number)
        {
        }
    }
}
