using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace CommunicationLib.Model
{
    /// <summary>
    /// Backing bean implementation for Step.
    /// </summary>
    public partial class Step
    {
        [XmlIgnore]
        public string label { get; set; }
    }
}
