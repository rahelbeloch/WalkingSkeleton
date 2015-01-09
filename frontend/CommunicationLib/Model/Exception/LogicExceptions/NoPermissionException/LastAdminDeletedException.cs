using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class LastAdminDeletedException : NoPermissionException
    {
        private int _number = 11340;
        new public int number { get { return _number; } }

        public LastAdminDeletedException()
            : base("Der letzte Admin darf nicht gelöscht werden.")
        {

        }

        public LastAdminDeletedException(string message)
            : base(message)
        {
        }

        public LastAdminDeletedException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
