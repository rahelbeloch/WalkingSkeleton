using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This exception is a child of NoPermissionException and is used if an inactive user still has jobs.
    /// </summary>
    public class InactiveUserException : LogInException
    {
        /// <summary>
        /// Each Exception has an number, here it is 11130. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11130;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InactiveUserException()
            : base("Der User wurde deaktiviert. \nDas Einloggen ist nicht mehr moeglich.")
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InactiveUserException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InactiveUserException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
