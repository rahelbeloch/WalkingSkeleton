using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Action = CommunicationLib.Model.Action;

namespace CommunicationLib.Model
{
    class BackingBeanFactory : AbstractBackingBeanFactory
    {
        public static Action createAction()
        {
            return new Action(new AbstractAction());
        }

        public static Action createAction(AbstractAction abstractAction)
        {
            return new Action(abstractAction);
        }

        public static FinalStep createFinalStep()
        {
            return new FinalStep(new AbstractFinalStep());
        }

        public static FinalStep createFinalStep(AbstractFinalStep abstractFinalStep)
        {
            return new FinalStep(abstractFinalStep);
        }

        public static StartStep createStartStep()
        {
            return new StartStep(new AbstractStartStep());
        }

        public static StartStep createStartStep(AbstractStartStep abstractStartStep)
        {
            return new StartStep(abstractStartStep);
        }

        public static Step createStep()
        {
            return new Step(new AbstractStep());
        }

        public static Step createStep(AbstractStep abstractStep)
        {
            return new Step(abstractStep);
        }
    }
}
