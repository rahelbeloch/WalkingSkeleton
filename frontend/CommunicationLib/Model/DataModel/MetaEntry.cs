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
    public class MetaEntry
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private string _key;
        public string key { get { return _key; } set { _key = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private string _value;
        public string value { get { return _value; } set { _value = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private string _group;
        public string group { get { return _group; } set { _group = value; } }
    }
}
