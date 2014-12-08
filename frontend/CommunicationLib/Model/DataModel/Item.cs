using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This class represents an item. An Item is a manifestation of a RootElement
    /// </summary>
    public class Item : RootElement
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private int _workflowId;
        public int workflowId { get { return _workflowId; } set { _workflowId = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private List<MetaEntry> _metadata;
        public List<MetaEntry> metadata { get { return _metadata; } set { _metadata = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private bool _finished;
        public bool finished { get { return _finished; } set { _finished = value; } }

        /// <summary>
        /// Constructor for Item
        /// </summary>
        public Item()
            : base()
        {
            _metadata = new List<MetaEntry>();
        }

        public int getActiveStepId()
        {
            foreach (MetaEntry me in metadata) 
            {
                if (me.group.Equals("step"))
                {
                    if (me.value.Equals("OPEN") || me.value.Equals("BUSY"))
                    {
                        return Convert.ToInt32(me.key);
                    }
                }
            }
            return -1;
        }
    }
}
