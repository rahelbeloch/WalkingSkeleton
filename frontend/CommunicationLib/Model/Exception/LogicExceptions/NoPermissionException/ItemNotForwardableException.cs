using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class ItemNotForwardableException : NoPermissionException
    {
        private int _number = 11320;
        new public int number { get { return _number; } }

        public ItemNotForwardableException()
            : base("Das Item ist nicht weiterschaltbar.")
        {

        }

        public ItemNotForwardableException(string message)
            : base(message)
        {
        }

        public ItemNotForwardableException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
