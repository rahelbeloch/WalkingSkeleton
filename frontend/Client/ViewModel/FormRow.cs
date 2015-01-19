using CommunicationLib.Model;
using NLog;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.ViewModel
{
    public class FormRow
    {
        private readonly Logger logger = LogManager.GetCurrentClassLogger();
        private Form form;
        private Item actItem;
        private String _key;
        public String key { get { return _key; } }
        private String _value;
        public String value {
            get { return _value; }
            set {
                _value = value;
                metaEntryItem.value = value;
            }
        }
        private String _datatype;
        public String datatype { get { return _datatype; } }
        private List<MetaEntry> _metadata;
        private Type _type;
        public Type type { get { return _type; } }
        private MetaEntry metaEntryItem;
        public FormRow(Item actItem, String key, String datatype)
        {
            this.actItem = actItem;
            _metadata = actItem.metadata;
            _key = key;
            _datatype = datatype;
            _type = Type.GetType(datatype);
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
