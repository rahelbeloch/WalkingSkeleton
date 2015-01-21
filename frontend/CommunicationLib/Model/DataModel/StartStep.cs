using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This class represents a StartStep and is a manifestation of Step
    /// </summary>
    public class StartStep : Step
    {
        /// <summary>
        /// Constructor for a StartStep
        /// </summary>
        public StartStep()
            : base()
        {
            Label = "Startzustand";
        }
    }
}