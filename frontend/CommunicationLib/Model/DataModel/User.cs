using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

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
        public string username { get { return _username; } set { _username = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private List<Role> _roles;
        public List<Role> roles { get { return _roles; } set { _roles = value; } }

        public User()
            : base()
        {
            _roles = new List<Role>();
        }
    }
}
