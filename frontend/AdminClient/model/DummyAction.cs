using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AdminClient.model
{
    class DummyAction : DummyStep
    {
        public DummyAction() : this("", "") { }

        public DummyAction(string description, string user)
        {
            this.type = "Aktion";
            this.description = description;
            this.user = user;
        }
    }
}
