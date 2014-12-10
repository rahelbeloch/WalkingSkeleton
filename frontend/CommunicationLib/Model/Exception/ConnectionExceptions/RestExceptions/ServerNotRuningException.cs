using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class ServerNotRunningException : RestException
    {
        private int _number = 12210;
        new public int number { get { return _number; } }

        public ServerNotRunningException()
            : base("Die Verbindung zum Server ist fehlgeschlagen")
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
