using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class ElementChangedException : PersistenceException
    {
        private int _number = 11240;
        public int number { get { return _number; } }

        public ElementChangedException()
        {

        }

        public ElementChangedException(string message)
         : base(message)
        {
        }

        public ElementChangedException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
