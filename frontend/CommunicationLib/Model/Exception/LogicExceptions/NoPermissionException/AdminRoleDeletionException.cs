using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class AdminRoleDeletionException : NoPermissionException
    {
        private int _number = 11330;
        new public int number { get { return _number; } }

        public AdminRoleDeletionException()
            : base("Die Rolle Admin darf nicht gelöscht werden.")
        {

        }

        public AdminRoleDeletionException(string message)
            : base(message)
        {
        }

        public AdminRoleDeletionException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
