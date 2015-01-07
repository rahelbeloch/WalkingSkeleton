using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This class represents a RootElement
    /// </summary>
    public class RootElement
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private String _id;
        public String id { get { return _id; } set { _id = value; } }

        public override bool Equals(object obj)
        {
            if(obj.GetType() == this.GetType())
            {
                return (obj as RootElement).id == this.id;
            }
            return false;
        }

        public override int GetHashCode()
        {
            return this.id.GetHashCode();
        }
    }
}
