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
        /// Used for (de)serialization!
        /// </summary>
        private string _name;
        public string name { get { return _name; } set { _name = value; } }

        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private int _userId;
        public int userId { get { return _userId; } set { _userId = value; } }

        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private string _username;
        public string username { get { return _username; } set { _username = value; } }

        public Action()
            : base()
        {
            label = "Aktion";
        }
    }
}
