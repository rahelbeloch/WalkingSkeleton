using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class MessagingException : ConnectionException
    {
        private int _number = 12100;
        public int number { get { return _number; } }

        public MessagingException()
        {

        }

        public MessagingException(string message)
         : base(message)
        {
        }

        public MessagingException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
