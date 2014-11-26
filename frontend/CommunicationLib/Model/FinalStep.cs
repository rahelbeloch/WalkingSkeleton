using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    public class FinalStep : Step
    {
        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private string _name;
        public string name { get { return _name; } set { _name = value; } }

        public FinalStep()
            : base()
        {
            label = "Endzustand";
        }
    }
}
