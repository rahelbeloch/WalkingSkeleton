using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of LogicException.
    /// </summary>
    public class NoPermissionException : LogicException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11230;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public NoPermissionException()
            : base("Kein Zugang für diese Operation.")
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public NoPermissionException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public NoPermissionException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
