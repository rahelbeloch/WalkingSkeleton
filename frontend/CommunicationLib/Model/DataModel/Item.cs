using Newtonsoft.Json;
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
        public String WorkflowId { get { return _workflowId; } set { _workflowId = value; } }
        private String _workflowId;

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public List<MetaEntry> Metadata { get { return _metadata; } set { _metadata = value; } }
        private List<MetaEntry> _metadata;
        
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public bool Finished { get { return _finished; } set { _finished = value; } }
        private bool _finished;

        /// <summary>
        /// Constructor for Item
        /// </summary>
        public Item()
            : base()
        {
            _metadata = new List<MetaEntry>();
        }

        /// <summary>
        /// Getter for the active step.
        /// </summary>
        /// <returns>nr of active step</returns>
        public int GetActiveStepId()
        {
            foreach (MetaEntry me in Metadata) 
            {
                if (me.key.Equals("status"))
                {
                    if (me.Value.Equals("OPEN") || me.Value.Equals("BUSY"))
                    {
                        return Convert.ToInt32(me.Group);
                    }
                }
            }
            return -1;
        }

        /// <summary>
        /// State of this item. Is one of enum MetaEntry.
        /// </summary>
        [JsonIgnore]
        public string State
        {
            get
            {
                foreach (MetaEntry me in Metadata)
                {
                    if (me.key.Equals("status"))
                    {
                        if (me.Value.Equals("OPEN"))
                        {
                            return "OPEN";
                        }
                        if (me.Value.Equals("BUSY"))
                        {
                            return "BUSY";
                        }
                    }
                }
                return "";
            }
        }
    }
}