using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception.ConnectionExceptions.RestExceptions
{
    class ServerNotRunningException : RestException
    {
        private int _number = 12210;
        public int number { get { return _number; } }

        public ServerNotRunningException()
        {

        }

        public ServerNotRunningException(string message)
         : base(message)
        {
        }

        public ServerNotRunningException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
