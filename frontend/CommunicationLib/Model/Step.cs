using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    public class Step
    {
        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private int _id;
        public int id { get { return _id; } set { _id = value; } }

        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private List<Step> _nextSteps;
        public List<Step> nextSteps { get { return _nextSteps; } set { _nextSteps = value; } }

        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private string _username;
        public string username { get { return _username; } set { _username = value; } }

        [JsonIgnore]
        public string label { get; set; }

        public Step()
            : base()
        {
            _nextSteps = new List<Step>();
        }
    }
}
