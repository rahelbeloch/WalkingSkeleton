using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class RestException : ConnectionException
    {
        private int _number = 12200;
        new public int number { get { return _number; } }

        public RestException()
            : base("Es ist ein Fehler in der Restschnittstelle passiert.")
        {

        }

        public RestException(string message)
         : base(message)
        {
        }

        public RestException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
