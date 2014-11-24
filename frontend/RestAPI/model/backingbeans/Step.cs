using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// Backing bean implementation for Step.
    /// </summary>
    public class Step
    {
        public AbstractStep Bean { get; set; }
        public string label { get; set; }

        public Step(AbstractStep abstractStep)
        {
            Bean = abstractStep;
            label = "";
        }
    }
}
