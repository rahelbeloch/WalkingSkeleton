using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class ServerNotRuningException : RestException
    {
        private int _number = 12210;
        new public int number { get { return _number; } }

        public ServerNotRuningException()
        {

        }

        public ServerNotRuningException(string message)
         : base(message)
        {
        }

        public ServerNotRuningException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
