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
    public class Step : RootElement
    {

        private List<Step> _nextSteps;
        [JsonIgnore]
        public List<Step> nextSteps { get { return _nextSteps; } set { _nextSteps = value; } }

        /// <summary>
        /// value y axis - Used for visualization in workflow designer
        /// </summary>
        private double _top;
        public double top { get { return _top; } set { _top = value; } }

        /// <summary>
        /// value x axis - Used for visualization in workflow designer
        /// </summary>
        private double _left;
        public double left { get { return _left; } set { _left = value;  } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private HashSet<String> _nextStepIds;
        public HashSet<String> nextStepIds { get { return _nextStepIds; } set { _nextStepIds = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private List<String> _roleIds;
        public List<String> roleIds { get { return _roleIds; } set { _roleIds = value; } }

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
            _nextStepIds = new HashSet<String>();
            _roleIds = new List<String>();
        }
    }
}
