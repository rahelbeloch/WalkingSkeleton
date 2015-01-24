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

                // TODO: wenn typ fork: an anfang der liste bei true output und ans ende bei false output
                if (referenceMapping[startItem].GetType() == typeof(Fork))
                {
                    if ((((ConnectorViewModel)designerConnection).SourceConnectorInfo).Orientation == ConnectorOrientation.TrueOutput)
                    {
                        referenceMapping[startItem].nextStepIds.Insert(0, referenceMapping[endItem].id);
                    }
                    else if ((((ConnectorViewModel)designerConnection).SourceConnectorInfo).Orientation == ConnectorOrientation.FalseOutput) 
                    {
                        referenceMapping[startItem].nextStepIds.Add(referenceMapping[endItem].id);
                    }
                }
                else
                {
                    // connect next step ids
                    referenceMapping[startItem].nextStepIds.Add(referenceMapping[endItem].id);
                }
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
                if (((StartStepViewModel)designerItem).selectedRole != null)
                {
                    startStep.roleIds.Add(((StartStepViewModel)designerItem).selectedRole.id);
                }

                return startStep;
            }
            else if (designerItem.GetType() == typeof(ActionViewModel))
            {
                Action action = step.Clone<Action>();

                action.id = getUniqueId();
                if (((ActionViewModel)designerItem).selectedRole != null)
                {
                    action.roleIds.Add(((ActionViewModel)designerItem).selectedRole.id);
                }

                if (((ActionViewModel)designerItem).description != null)
                {
                    action.description = ((ActionViewModel)designerItem).description;
                }

                return action;
            }
            else if (designerItem.GetType() == typeof(FinalStepViewModel))
            {
                FinalStep finalStep = step.Clone<FinalStep>();
                finalStep.id = getUniqueId();
                return finalStep;
            }
            else if (designerItem.GetType() == typeof(ForkViewModel))
            {
                Fork fork = step.Clone<Fork>();
                fork.script = ((ForkViewModel)designerItem).description;
                fork.id = getUniqueId();
                return fork;
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
            // clear any previous designer items
            diagramViewModel.Items.Clear();

            // temporary list of designer items
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

                if (s.GetType() == typeof(StartStep) || s.GetType() == typeof(Action))
                {
                    self.enableRightConnector = false;

                    FullyCreatedConnectorInfo sourceConnectorInfo = new FullyCreatedConnectorInfo(self, ConnectorOrientation.Output);
                    FullyCreatedConnectorInfo sinkConnectorInfo = GetSinkConnector(s, designerItems);
                    ConnectorViewModel connector = new ConnectorViewModel("", diagramViewModel, sourceConnectorInfo, sinkConnectorInfo);
                    diagramViewModel.AddItemCommand.Execute(connector);
                    sinkConnectorInfo.DataItem.enableInputConnector = false;
                }
                else if (s.GetType() == typeof(Fork))
                {
                    self.enableTopConnector = false;
                    self.enableBottomConnector = false;

                    // true case
                    FullyCreatedConnectorInfo sourceConnectorInfo = new FullyCreatedConnectorInfo(self, ConnectorOrientation.TrueOutput);
                    FullyCreatedConnectorInfo sinkConnectorInfo = GetTrueSinkConnector(s, designerItems);
                    ConnectorViewModel connector = new ConnectorViewModel("", diagramViewModel, sourceConnectorInfo, sinkConnectorInfo);
                    diagramViewModel.AddItemCommand.Execute(connector);
                    sinkConnectorInfo.DataItem.enableInputConnector = false;

                    // false case
                    sourceConnectorInfo = new FullyCreatedConnectorInfo(self, ConnectorOrientation.FalseOutput);
                    sinkConnectorInfo = GetFalseSinkConnector(s, designerItems);
                    connector = new ConnectorViewModel("", diagramViewModel, sourceConnectorInfo, sinkConnectorInfo);
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
                actionViewModel.description = action.description;
                return actionViewModel;
            }
            else if (step.GetType() == typeof(FinalStep))
            {
                FinalStep finalStep = step.Clone<FinalStep>();
                FinalStepViewModel finalStepViewModel = new FinalStepViewModel(step.id, diagramViewModel, step.left, step.top);
                return finalStepViewModel;
            }
            else if (step.GetType() == typeof(Fork))
            {
                Fork fork = step.Clone<Fork>();
                ForkViewModel forkViewModel = new ForkViewModel(step.id, diagramViewModel, step.left, step.top, fork.script);
                return forkViewModel;
            }

            return null;
        }

        /// <summary>
        /// Returns outgoing sink connector.
        /// SinkConnectors are the "input connectors" of all next steps.
        /// </summary>
        /// <param name="step"></param>
        /// <param name="designerItems"></param>
        /// <returns></returns>
        private static FullyCreatedConnectorInfo GetSinkConnector(Step step, List<SelectableDesignerItemViewModelBase> designerItems)
        {
            FullyCreatedConnectorInfo connector = null;
            connector = new FullyCreatedConnectorInfo((DesignerItemViewModelBase)designerItems.First(x => x.Id == step.nextStepIds[0]), ConnectorOrientation.Input);
            return connector;
        }

        /// <summary>
        /// Returns outgoing sink connector for true case at a fork.
        /// SinkConnectors are the "input connectors" of all next steps.
        /// </summary>
        /// <param name="step"></param>
        /// <param name="designerItems"></param>
        /// <returns></returns>
        private static FullyCreatedConnectorInfo GetTrueSinkConnector(Step step, List<SelectableDesignerItemViewModelBase> designerItems)
        {
            FullyCreatedConnectorInfo connector = null;
            connector = new FullyCreatedConnectorInfo((DesignerItemViewModelBase)designerItems.First(x => x.Id == step.nextStepIds[0]), ConnectorOrientation.Input);
            return connector;
        }

        /// <summary>
        /// Returns outgoing sink connector for false case at a fork.
        /// SinkConnectors are the "input connectors" of all next steps.
        /// </summary>
        /// <param name="step"></param>
        /// <param name="designerItems"></param>
        /// <returns></returns>
        private static FullyCreatedConnectorInfo GetFalseSinkConnector(Step step, List<SelectableDesignerItemViewModelBase> designerItems)
        {
            FullyCreatedConnectorInfo connector = null;
            connector = new FullyCreatedConnectorInfo((DesignerItemViewModelBase)designerItems.First(x => x.Id == step.nextStepIds[1]), ConnectorOrientation.Input);
            return connector;
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
