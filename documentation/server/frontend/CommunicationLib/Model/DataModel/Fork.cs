using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// A fork has two next steps. 
    /// A python script can be used to determine which path will be executed.
    /// </summary>
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
