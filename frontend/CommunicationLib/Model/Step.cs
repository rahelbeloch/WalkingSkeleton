using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This class represents a Step
    /// </summary>
    public class Step
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public string type { get { return this.GetType().Name; } set { } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private int _id;
        public int id { get { return _id; } set { _id = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private List<Step> _nextSteps;
        public List<Step> nextSteps { get { return _nextSteps; } set { _nextSteps = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private string _username;
        public string username { get { return _username; } set { _username = value; } }

        /// <summary>
        /// The label represents the type of a Step as a string (e.g. "Startzustand", "Aktion", ...).
        /// When (de)serializated, the label will be ignored (because in the Server Step model, there is no label)
        /// </summary>
        [JsonIgnore]
        public string label { get; set; }

        /// <summary>
        /// Constructor for Step
        /// </summary>
        public Step()
            : base()
        {
            _nextSteps = new List<Step>();
        }
    }
}
