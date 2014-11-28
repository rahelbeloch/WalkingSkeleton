using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    public class RootElement
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private int _id;
        public int id { get { return _id; } set { _id = value; } }
    }
}
