using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This class represents a Workflow and is a manifestation of a RootElement
    /// </summary>
    public class Role : RootElement
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private string _rolename;
        public string rolename { get { return _id; } set { _id = value; } }

        /// <summary>
        /// Constructor for Workflow
        /// </summary>
        public Role()
            : base()
        {

        }

    }
}