using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Action = CommunicationLib.Model.Action;

namespace CommunicationLib.Model
{
    abstract class AbstractBackingBeanFactory
    {
        static Action createAction();
        static Action createAction(AbstractAction abstractAction);

        static FinalStep createFinalStep();
        static FinalStep createFinalStep(AbstractFinalStep abstractFinalStep);

        static StartStep createStartStep();
        static StartStep createStartStep(AbstractStartStep abstractStartStep);

        static Step createStep();
        static Step createStep(AbstractStep abstractStep);
    }
}
