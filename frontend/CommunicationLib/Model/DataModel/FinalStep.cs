using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This class represents a FinalStep and is a manifestation of a Step
    /// </summary>
    public class FinalStep : Step
    {
        /// <summary>
        /// Constructor for FinalStep
        /// </summary>
        public FinalStep()
            : base()
        {
            label = "Endzustand";
            
        }
    }
}
