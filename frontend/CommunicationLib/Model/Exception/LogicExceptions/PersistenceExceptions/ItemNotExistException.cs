using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class ItemNotExistException : DoesntExistsException
    {
        private int _number = 11253;
        new public int number { get { return _number; } }

        public ItemNotExistException()
        {

        }

        public ItemNotExistException(string message)
            : base(message)
        {
        }

        public ItemNotExistException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
