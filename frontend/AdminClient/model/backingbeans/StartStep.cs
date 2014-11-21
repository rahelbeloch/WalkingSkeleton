using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AdminClient.model
{
    /// <summary>
    /// Backing bean implementation for StartStep.
    /// </summary>
    class StartStep : AbstractStartStep
    {
        private string _label = "Startzustand";
        public string label { get { return _label; } set { } }
    }
}
