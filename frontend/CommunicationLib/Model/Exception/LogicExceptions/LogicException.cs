using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class LogicException : BasicException
    {
        private int _number = 11000;
        new public int number { get { return _number; } }

         public LogicException()
             :base("Es ist ein Fehler in der Logik aufgetreten.")
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
