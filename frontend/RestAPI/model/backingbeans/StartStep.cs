using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// Backing bean implementation for StartStep.
    /// </summary>
    public class StartStep : AbstractStartStep
    {
        /// <summary>
        /// Property label to show in GUI.
        /// </summary>
        private string _label = "Startzustand";
        public string label { get { return _label; } set { } }
    }
}
