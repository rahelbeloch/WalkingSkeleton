using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace CommunicationLib.Model
{
    /// <summary>
    /// Backing bean implementation for StartStep.
    /// </summary>
    public partial class AbstractStartStep
    {
        public AbstractStartStep() : base()
        {
            label = "Startzustand";
        }
    }
}
