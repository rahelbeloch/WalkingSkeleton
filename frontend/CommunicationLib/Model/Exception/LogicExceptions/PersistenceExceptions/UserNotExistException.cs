using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{

    public class UserNotExistException : DoesntExistsException
    {
        private int _number = 11251;
        new public int number { get { return _number; } }

        public UserNotExistException()
        {

        }

        public UserNotExistException(string message)
         : base(message)
        {
        }

        public UserNotExistException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
