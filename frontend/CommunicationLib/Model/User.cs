using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    public class User : RootElement
    {
        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private string _username;
        public string username { get { return _username; } set { _username = value; } }
    }
}
