using Newtonsoft.Json;
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
        public String Id { get { return _id; } set { _id = value; } }
        private String _id = "";

        /// <summary>
        /// Compares the handed over object to this RootElement.
        /// </summary>
        /// <param name="obj">the object to compare</param>
        /// <returns>true if it is equal, else false</returns>
        public override bool Equals(object obj)
        {
            if(obj.GetType() == this.GetType())
            {
                return (obj as RootElement).Id == this.Id;
            }
            return false;
        }

        /// <summary>
        /// Getter for the hashcode of the root element.
        /// </summary>
        /// <returns>the hashcode as int</returns>
        public override int GetHashCode()
        {
            return this.Id.GetHashCode();
        }

        /// <summary>
        /// Clones the RootElement.
        /// </summary>
        /// <typeparam name="T">the type to clone</typeparam>
        /// <returns>the cloned RootElement</returns>
        public T Clone<T>()
        {
            var serialized = JsonConvert.SerializeObject(this, Constants.JSON_SETTINGS);
            return JsonConvert.DeserializeObject<T>(serialized, Constants.JSON_SETTINGS);
        }
    }
}
