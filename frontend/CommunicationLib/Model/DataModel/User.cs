using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Security;
using Newtonsoft.Json;

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
        [JsonIgnore]
        public string username { get { return id; } set { id = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public string password { get { return _passowrd; } set { _passowrd = value; } }
        private string _passowrd;

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public bool active { get { return _active; } set { _active = value; } }
        private bool _active;

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public HashSet<Role> roles { get { return _roles; } set { _roles = value; } }
        private HashSet<Role> _roles;

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public List<String> messagingSubs { get { return _messagingSubs; } set { _messagingSubs = value; } }
        private List<String> _messagingSubs;

        /// <summary>
        /// Constructor for a user.
        /// </summary>
        public User()
            : base()
        {
            active = true;
            _roles = new HashSet<Role>();
            _messagingSubs = new List<String>();
            password = "";
        }
    }
}
