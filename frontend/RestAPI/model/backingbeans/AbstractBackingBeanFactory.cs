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
        abstract static Action createAction();
        abstract static Action createAction(AbstractAction abstractAction);

        abstract static FinalStep createFinalStep();
        abstract static FinalStep createFinalStep(AbstractFinalStep abstractFinalStep);

        abstract static StartStep createStartStep();
        abstract static StartStep createStartStep(AbstractStartStep abstractStartStep);

        abstract static Step createStep();
        abstract static Step createStep(AbstractStep abstractStep);
    }
}
