using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model.DataModel
{
    /// <summary>
    /// This class represents a FormEntry. Can be used in the context of creating forms.
    /// </summary>
    public class FormEntry: RootElement
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public string key { get { return id; } set { id = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public string datatype { get { return _datatype; } set { _datatype = value; } }
        private string _datatype;
        
        /// <summary>
        /// Used for client view. Will not be (de)serialized.
        /// </summary>
        [JsonIgnore]
        public List<String> datatypes { get { return _datatypes; } set { _datatypes = value; } }
        private List<String> _datatypes;
        
        /// <summary>
        /// Constructor for a form entry.
        /// </summary>
        public FormEntry(): base() 
        {
            _datatypes = new List<String>();
            _datatypes.Add("String");
            _datatypes.Add("Int");
            _datatypes.Add("Double");
        }
    }
}