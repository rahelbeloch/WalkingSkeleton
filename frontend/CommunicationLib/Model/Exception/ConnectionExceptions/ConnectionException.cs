using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class ConnectionException : BasicException
    {
        private int _number = 12000;
        new public int number { get { return _number; } }

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
