using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class ConnectionException : BasicException
    {
        private int _number = 12000;
        public int number { get { return _number; } }

        public ConnectionException()
        {

        }

        public ConnectionException(string message)
         : base(message)
        {
        }

        public ConnectionException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
