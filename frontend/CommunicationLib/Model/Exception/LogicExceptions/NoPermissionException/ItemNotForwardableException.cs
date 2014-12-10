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

        public ItemNotForwardable()
            : base("Das Item ist nicht weiterschaltbar.")
        {

        }

        public ItemNotForwardable(string message)
            : base(message)
        {
        }

        public ItemNotForwardable(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
