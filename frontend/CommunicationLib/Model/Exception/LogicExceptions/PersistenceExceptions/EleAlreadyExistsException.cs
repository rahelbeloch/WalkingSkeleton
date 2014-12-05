using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class EleAlreadyExistsException : PersistenceException
    {
        private int _number = 11220;
        new public int number { get { return _number; } }

        public EleAlreadyExistsException()
        {

        }

        public EleAlreadyExistsException(string message)
         : base(message)
        {
        }

        public EleAlreadyExistsException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
