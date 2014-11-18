using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AdminClient.model
{
    class DummyStep
    {
        DummyStep next { get; set; }
        public string type { get; set; }
        public string description { get; set; }
        public string user { get; set; }

        public DummyStep()
        {
            next = null;
            type = "-";
            description = "-";
            user = "-";
        }
    }
}
