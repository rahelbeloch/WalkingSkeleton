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

        /// <summary>
        /// Converts a list of SelectableDesignerItemViewModels to a workflow object.
        /// </summary>
        /// <param name="items">The designer items</param>
        /// <returns>Workflow object</returns>
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

            // add remaining designer items which are not connected
            foreach (DesignerItemViewModelBase designerItem in stepDesignerItems)
            {
                if (referenceMapping[designerItem] == null)
                {
                    referenceMapping[designerItem] = DesignerItemToStep(designerItem);
                }
            }

            // add steps to workflow
            workflow.steps.AddRange(referenceMapping.Values.Select(x => x).ToList());

            return workflow;
        }

        /// <summary>
        /// Converts a single DesignerItem into a StartStep, Action or FinalStep.
        /// </summary>
        /// <param name="designerItem"></param>
        /// <returns></returns>
        private static Step DesignerItemToStep(SelectableDesignerItemViewModelBase designerItem)
        {
            Step step = new Step();
            step.left = ((DesignerItemViewModelBase)designerItem).Left;
            step.top = ((DesignerItemViewModelBase)designerItem).Top;

            if (designerItem.GetType() == typeof(StartStepViewModel))
            {
                StartStep startStep = step.Clone<StartStep>();
                startStep.id = getUniqueId();
                startStep.roleIds.Add(((StartStepViewModel)designerItem).selectedRole.id);
                return startStep;
            }
            else if (designerItem.GetType() == typeof(ActionViewModel))
            {
                Action action = step.Clone<Action>();
                action.id = getUniqueId();
                action.roleIds.Add(((ActionViewModel)designerItem).selectedRole.id);
                return action;
            }
            else if (designerItem.GetType() == typeof(FinalStepViewModel))
            {
                FinalStep finalStep = step.Clone<FinalStep>();
                finalStep.id = getUniqueId();
                return finalStep;
            }

            return null;
        }

        /// <summary>
        /// This method returns a unique id (during runtime).
        /// It is used to connect steps by ids.
        /// </summary>
        /// <returns>Unique id</returns>
        private static string getUniqueId()
        {
            uniqueId += 1;
            return uniqueId.ToString();
        }
    }
}
