using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class PersistenceException : LogicException
    {
        private int _number = 11200;
        new public int number { get { return _number; } }

        public PersistenceException()
            : base("Fehler in der Datenbank.")
        {

        }

        public PersistenceException(string message)
         : base(message)
        {
        }

        public PersistenceException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
