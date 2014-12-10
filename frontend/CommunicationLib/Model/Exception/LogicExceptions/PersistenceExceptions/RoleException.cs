using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class RoleException : PersistenceException
    {
        private int _number = 11260;
        new public int number { get { return _number; } }

        public RoleException()
            : base("Es sind Rollen Probleme aufgetreten")
        {

        }

        public RoleException(string message)
            : base(message)
        {
        }

        public RoleException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
