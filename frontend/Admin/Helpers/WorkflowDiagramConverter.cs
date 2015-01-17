using CommunicationLib.Model;
using DiagramDesigner;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Action = CommunicationLib.Model.Action;

namespace Admin.Helpers
{
    class WorkflowDiagramConverter
    {
        private static int uniqueId = 0;

        public static Workflow DiagramItemsToWorkflow(List<SelectableDesignerItemViewModelBase> items) 
        {
            Workflow workflow = new Workflow();
            IList<Step> steps = new List<Step>();
            Dictionary<DesignerItemViewModelBase, Step> referenceMapping = new Dictionary<DesignerItemViewModelBase,Step>();

            // get all Start-, Action-, and FinalStep-ViewModels and fill dictionary
            List<SelectableDesignerItemViewModelBase> stepDesignerItems = items.Where(x => x.GetType() != typeof(ConnectorViewModel)).ToList();
            foreach (SelectableDesignerItemViewModelBase designerItem in stepDesignerItems)
            {
                referenceMapping.Add((DesignerItemViewModelBase)designerItem, null);
            }

            // get all Connections and create step models if neccessary
            List<SelectableDesignerItemViewModelBase> designerConnections = items.Where(x => x.GetType() == typeof(ConnectorViewModel)).ToList();
            foreach (SelectableDesignerItemViewModelBase designerConnection in designerConnections)
            {
                DesignerItemViewModelBase startItem = ((FullyCreatedConnectorInfo)((ConnectorViewModel)designerConnection).SourceConnectorInfo).DataItem;
                DesignerItemViewModelBase endItem = ((FullyCreatedConnectorInfo)((ConnectorViewModel)designerConnection).SinkConnectorInfo).DataItem;

                if (referenceMapping[startItem] == null)
                {
                    referenceMapping[startItem] = DesignerItemToStep(startItem);
                }

                if (referenceMapping[endItem] == null)
                {
                    referenceMapping[endItem] = DesignerItemToStep(endItem);
                }

                // connect next step ids
                referenceMapping[startItem].nextStepIds.Add(referenceMapping[endItem].id);

            }

            // add steps to workflow
            workflow.steps.AddRange(referenceMapping.Values.Select(x => x).ToList());

            // TODO: validate workflow

            return workflow;
        }

        private static Step DesignerItemToStep(SelectableDesignerItemViewModelBase designerItem)
        {
            if (designerItem.GetType() == typeof(StartStepViewModel))
            {
                StartStep startStep = new StartStep();
                startStep.id = getUniqueId();
                startStep.roleIds.Add("Manager");
                return startStep;
            }
            else if (designerItem.GetType() == typeof(ActionViewModel))
            {
                Action action = new Action();
                action.id = getUniqueId();
                action.roleIds.Add("Manager");
                return action;
            }
            else if (designerItem.GetType() == typeof(FinalStepViewModel))
            {
                return new FinalStep()
                {
                    id = getUniqueId()
                };
            }

            return null;
        }

        private static string getUniqueId()
        {
            uniqueId += 1;
            return uniqueId.ToString();
        }
    }
}
