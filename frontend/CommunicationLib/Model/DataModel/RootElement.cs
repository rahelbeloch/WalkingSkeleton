using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This class represents a RootElement
    /// </summary>
    public class RootElement : ViewModelBase
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private String _id;
        public String id { get { return _id; } set { _id = value; } }
    }
}
