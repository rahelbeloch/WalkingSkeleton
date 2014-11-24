using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Action = CommunicationLib.Model.Action;

namespace CommunicationLib.model.backingbeans
{
    class BackingBeanFactory : IBackingBeanFactory
    {
        public Action createAction()
        {
            return new Action(new AbstractAction());
        }

        public Action createAction(AbstractAction abstractAction)
        {
            return new Action(abstractAction);
        }

        public FinalStep createFinalStep()
        {
            return new FinalStep(new AbstractFinalStep());
        }

        public FinalStep createFinalStep(AbstractFinalStep abstractFinalStep)
        {
            return new FinalStep(abstractFinalStep);
        }

        public StartStep createStartStep()
        {
            return new StartStep(new AbstractStartStep());
        }

        public StartStep createStartStep(AbstractStartStep abstractStartStep)
        {
            return new StartStep(abstractStartStep);
        }

        public Step createStep()
        {
            return new Step(new AbstractStep());
        }

        public Step createStep(AbstractStep abstractStep)
        {
            return new Step(abstractStep);
        }
    }
}
