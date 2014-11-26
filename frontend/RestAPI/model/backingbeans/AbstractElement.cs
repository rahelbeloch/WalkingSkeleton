using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// Backing bean implementation for Item.
    /// </summary>
    public partial class AbstractElement
    {
        private int _id;

        public AbstractElement()
        {

        }

        public AbstractElement(int id)
        {
            this._id = id;
        }

        public int id
        {
            get { 
                return _id; 
            }

            set { 
                _id = value;
            }
        }
    }
}
