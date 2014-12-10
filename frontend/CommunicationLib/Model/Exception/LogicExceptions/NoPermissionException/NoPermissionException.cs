using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class NoPermissionException : LogicException
    {
        private int _number = 11230;
        new public int number { get { return _number; } }

        public NoPermissionException()
            : base("Kein Zugang für diese Operation.")
        {

        }

        public NoPermissionException(string message)
         : base(message)
        {
        }

        public NoPermissionException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
