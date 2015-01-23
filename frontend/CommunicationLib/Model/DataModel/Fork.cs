using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    public class Fork : Step
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public string script { get { return _script; } set { _script = value; } }
        private string _script;

        /// <summary>
        /// Constructor for Fork.
        /// </summary>
        public Fork()
            : base()
        {
            script = "";
        }
    }
}
