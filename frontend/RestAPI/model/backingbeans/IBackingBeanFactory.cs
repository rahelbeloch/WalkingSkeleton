using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Action = CommunicationLib.Model.Action;

namespace CommunicationLib.Model
{
    interface IBackingBeanFactory
    {
        Action createAction();
        Action createAction(AbstractAction abstractAction);

        FinalStep createFinalStep();
        FinalStep createFinalStep(AbstractFinalStep abstractFinalStep);

        StartStep createStartStep();
        StartStep createStartStep(AbstractStartStep abstractStartStep);

        Step createStep();
        Step createStep(AbstractStep abstractStep);
    }
}
