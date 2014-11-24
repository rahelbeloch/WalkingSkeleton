using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// Backing bean implementation for FinalStep.
    /// </summary>
    public class FinalStep : Step
    {
        public FinalStep(AbstractFinalStep abstractFinalStep) : base(abstractFinalStep)
        {
            label = "Endzustand";
        }
    }
}
