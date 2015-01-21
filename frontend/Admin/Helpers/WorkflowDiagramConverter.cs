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
                Console.WriteLine("designer id: " + designerItem.Id);
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
        /// This method converts a given workflow to designer items. The created items will be shown instantly.
        /// </summary>
        /// <param name="workflow">Workflow to convert</param>
        /// <param name="diagramViewModel">Corresponding diagram view model</param>
        public static void WorkflowToDesignerItems(Workflow workflow, DiagramViewModel diagramViewModel)
        {
            // clear designer items
            diagramViewModel.Items.Clear();

            List<SelectableDesignerItemViewModelBase> designerItems = new List<SelectableDesignerItemViewModelBase>();

            // convert steps to designer items
            foreach(Step s in workflow.steps) 
            {
                SelectableDesignerItemViewModelBase designerItem = StepToDesignerItem(s, diagramViewModel);
                if (designerItem != null)
                {
                    designerItem.IsSelected = false;
                    diagramViewModel.AddItemCommand.Execute(designerItem);
                    designerItems.Add(designerItem);
                }
            }
            
            // iterate through steps and add connections and disable connectors
            foreach(Step s in workflow.steps)
            {
                List<String> nextStepIds = s.nextStepIds;

                DesignerItemViewModelBase self = (DesignerItemViewModelBase) designerItems.First(x => x.Id == s.id);
                self.enableRightConnector = false;
                
                FullyCreatedConnectorInfo sourceConnectorInfo = new FullyCreatedConnectorInfo(self, ConnectorOrientation.Output);
                List<FullyCreatedConnectorInfo> sinkConnectorInfos = GetSinkConnectors(s, designerItems);
                foreach (FullyCreatedConnectorInfo sinkConnectorInfo in sinkConnectorInfos)
                {
                    ConnectorViewModel connector = new ConnectorViewModel("", diagramViewModel, sourceConnectorInfo, sinkConnectorInfo);
                    diagramViewModel.AddItemCommand.Execute(connector);
                    sinkConnectorInfo.DataItem.enableInputConnector = false;
                } 
            }
        }

        /// <summary>
        /// Converts a single step to a designer item.
        /// </summary>
        /// <param name="step">The step to convert.</param>
        /// <param name="diagramViewModel">Corresponding diagram view model</param>
        /// <returns>The new designer item</returns>
        private static SelectableDesignerItemViewModelBase StepToDesignerItem(Step step, DiagramViewModel diagramViewModel)
        {
            if(step.GetType() == typeof(StartStep))
            {
                StartStep startStep = step.Clone<StartStep>();
                Role selectedRole = new Role() 
                {
                    id = startStep.roleIds.First()
                };
                StartStepViewModel startStepViewModel = new StartStepViewModel(step.id, diagramViewModel, step.left, step.top, selectedRole);
                Console.WriteLine("return start step view model");
                return startStepViewModel;
            }
            else if (step.GetType() == typeof(Action))
            {
                Action action = step.Clone<Action>();
                Role selectedRole = new Role()
                {
                    id = action.roleIds.First()
                };
                ActionViewModel actionViewModel = new ActionViewModel(step.id, diagramViewModel, step.left, step.top, selectedRole);
                return actionViewModel;
            }
            else if (step.GetType() == typeof(FinalStep))
            {
                FinalStep finalStep = step.Clone<FinalStep>();
                FinalStepViewModel finalStepViewModel = new FinalStepViewModel(step.id, diagramViewModel, step.left, step.top);
                return finalStepViewModel;
            }

            return null;
        }

        /// <summary>
        /// Returns a list of all outgoing sink connectors..
        /// SinkConnectors are the "input connectors" of all next steps.
        /// Mostly there will be only one sink connector, but possible future conditions-steps may have multiple next steps.
        /// </summary>
        /// <param name="step"></param>
        /// <param name="designerItems"></param>
        /// <returns></returns>
        private static List<FullyCreatedConnectorInfo> GetSinkConnectors(Step step, List<SelectableDesignerItemViewModelBase> designerItems)
        {
            List<FullyCreatedConnectorInfo> connectors = new List<FullyCreatedConnectorInfo>();

            foreach (String nextId in step.nextStepIds)
            {
                if (step.GetType() == typeof(StartStep) || step.GetType() == typeof(Action))
                {
                    connectors.Add(new FullyCreatedConnectorInfo((DesignerItemViewModelBase)designerItems.First(x => x.Id == nextId), ConnectorOrientation.Input));
                } 
                // TODO: check if type is a branch
            }

            return connectors;
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
