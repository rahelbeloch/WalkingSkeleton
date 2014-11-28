using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    public class Action : Step
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private string _description;
        public string description { get { return _description; } set { _description = value; } }

        public Action()
            : base()
        {
            label = "Aktion";
        }
    }
}
