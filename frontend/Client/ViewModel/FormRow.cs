using CommunicationLib.Model;
using NLog;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.ViewModel
{
    /// <summary>
    /// ViewModel class which holds all data of a row of a formular.
    /// </summary>
    public class FormRow
    {
        private readonly Logger logger = LogManager.GetCurrentClassLogger();

        private Item actItem;
        
        /// <summary>
        /// the key of the key-value-pair
        /// </summary>
        public String key { get { return _key; } }
        private String _key;

        private String _value;
        /// <summary>
        /// the value of the key-value-pair
        /// </summary>
        public String value {
            get { return _value; }
            set {
                _value = value;
                metaEntryItem.value = value;
            }
        }
        
        /// <summary>
        /// the datatype of the value
        /// </summary>
        public String datatype { get { return _datatype; } }
        private String _datatype;
        
        private List<MetaEntry> _metadata;
        private MetaEntry metaEntryItem;
        
        /// <summary>
        /// Constructor for a FormRow.
        /// </summary>
        /// <param name="actItem">the item for this row</param>
        /// <param name="key">the key (meaning of this field)</param>
        /// <param name="datatype">datatype of this field</param>
        public FormRow(Item actItem, String key, String datatype)
        {
            this.actItem = actItem;
            _metadata = actItem.metadata;
            _key = key;
            _datatype = datatype;
            foreach (MetaEntry metaEntry in actItem.metadata)
            {
                if (metaEntry.key.Equals(key))
                {
                    _value = metaEntry.value;
                    metaEntryItem = metaEntry;
                    break;
                }
            }
            if (_value == null)
            {
                _value = "";
                logger.Debug("kein Wert gefunden für den Schlüssel " + _key);
            }
        }
    }
}