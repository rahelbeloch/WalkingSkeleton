using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AdminClient.model
{
    class DummyFinalStep : DummyStep
    {
        public DummyFinalStep()
        {
            type = "Endzustand";
        }
    }
}
