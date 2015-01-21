using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This class represents a MetaEntry for the MetaEntryList of an Item
    /// </summary>
    public class MetaEntry : RootElement
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public string key { get { return id; } set { id = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public string value { get { return _value; } set { _value = value; } }
        private string _value;

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public string group { get { return _group; } set { _group = value; } }
        private string _group;
    }
}