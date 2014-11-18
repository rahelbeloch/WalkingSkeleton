using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AdminClient.model
{
    class DummyStartStep : DummyStep
    {
        public DummyStartStep(string user)
        {
            type = "Start";
            this.user = user;
        }
    }
}
