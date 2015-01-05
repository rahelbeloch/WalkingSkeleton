using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Security;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This class represents an User. An User is a manifestation of a RootElement.
    /// </summary>
    public class User : RootElement
    {
        
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private string _username;
        public string username { get { return id; } set { id = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private bool _active;
        public bool active { get { return _active; } set { _active = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private List<Role> _roles;
        public List<Role> roles { get { return _roles; } set { _roles = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private List<String> _messagingSubs;
        public List<String> messagingSubs { get { return _messagingSubs; } set { _messagingSubs = value; } }

        public User()
            : base()
        {
            active = true;
            _roles = new List<Role>();
            _messagingSubs = new List<String>();
        }
    }
}
