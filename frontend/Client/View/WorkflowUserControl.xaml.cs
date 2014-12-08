using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Client.ViewModel;
using System.ComponentModel;
using System.Collections.ObjectModel;
using CommunicationLib.Model;
using RestAPI;

namespace Client.View
{   
    /// <summary>
    /// Interaktionslogik für WorkflowUserControl.xaml
    /// </summary>
    public partial class WorkflowUserControl : UserControl
    {
        private FlowDocument flowDoc;
        private Table table1;
        private WorkflowViewModel _workflowViewModel;
        private String userName;
        private ObservableCollection<Workflow> _workflows;
        private ObservableCollection<DashboardWorkflow> _dashboardWorkflows;
        public WorkflowUserControl()
        {
            InitializeComponent();
        }
        private void pageLoaded(object sender, RoutedEventArgs e)
        {
            Console.WriteLine(this.DataContext.ToString());
            _workflowViewModel = (WorkflowViewModel)(this.DataContext);
            _workflowViewModel.PropertyChanged += new PropertyChangedEventHandler(SubPropertyChanged);
            _workflowViewModel.workflows.CollectionChanged += OnWorkflowsChanged;
            userName = _workflowViewModel.userName;
            _workflows = _workflowViewModel.workflows;
            _dashboardWorkflows = _workflowViewModel.dashboardWorkflows;
            InitializeDashboard();
        }
        private void OnWorkflowsChanged(object sender, System.Collections.Specialized.NotifyCollectionChangedEventArgs e)
        {
            Console.WriteLine("onworkflowschanged");
            InitializeDashboard();
        }
        private void SubPropertyChanged(object sender, PropertyChangedEventArgs e)
        {
            if (e.PropertyName == "workflows")
            {
                _workflows = _workflowViewModel.workflows;
                Console.WriteLine("workflows changed");
                showWorkflows();
            }
        }
        private void showWorkflows()
        {
            try
            {
                Console.WriteLine(userName);
                Console.WriteLine("test workflows");
                foreach (Workflow workflow in _workflows)
                {
                    Console.WriteLine(workflow.id);
                    foreach (Step step in workflow.steps)
                    {
                        Console.WriteLine(step.label);
                    }
                }
                String test = _workflows[0].ToString();
                Console.WriteLine(test);
            }
            catch (Exception exe)
            {
                Console.WriteLine("Exception " + exe.Message);
            }
        }
        private void InitializeDashboard()
        {
            dashboard.Blocks.Clear();
            // Create the parent FlowDocument...
            flowDoc = dashboard;

            // Create the Table...
            table1 = new Table();
            // ...and add it to the FlowDocument Blocks collection.
            flowDoc.Blocks.Add(table1);


            // Set some global formatting properties for the table.
            table1.CellSpacing = 10;
            table1.Background = Brushes.White;

            draw_Dashboard();
        }
        private void draw_Dashboard()
        {
            foreach (DashboardWorkflow dashboardWorkflow in _dashboardWorkflows)
            {
                addworkflowasrow("Workflow mit der id:" + dashboardWorkflow.actWorkflow.id, dashboardWorkflow);
            }
        }

        private void addworkflowasrow(String title, DashboardWorkflow dashboardWorkflow)
        {
            // Create and add an empty TableRowGroup to hold the table's Rows.
            TableRowGroup newRowGroup = new TableRowGroup();
            table1.RowGroups.Add(newRowGroup);

            // Add the first (title) row.
            TableRow currentRow = new TableRow();
            newRowGroup.Rows.Add(currentRow);

            // Global formatting for the title row.
            currentRow.Background = Brushes.Silver;
            currentRow.FontSize = 40;
            currentRow.FontWeight = FontWeights.Bold;

            // Add the header row with content, 
            currentRow.Cells.Add(new TableCell(new Paragraph(new Run(title))));
            // and set the row to span all 2 columns.
            currentRow.Cells[0].ColumnSpan = 2;
            if (dashboardWorkflow.startPermission)
            {
                var button = new Button();
                button.Content = "Neu erstellen";
                button.Tag = dashboardWorkflow.actWorkflow.id;

                button.Click += new RoutedEventHandler(createWorkflow);
                button.FontSize = 18;
                var block = new BlockUIContainer(button);
                currentRow.Cells.Add(new TableCell(block));
            }
            // Add the second (header) row.
            currentRow = new TableRow();
            newRowGroup.Rows.Add(currentRow);

            // Global formatting for the header row.
            currentRow.FontSize = 18;
            currentRow.FontWeight = FontWeights.Bold;

            // Add cells with content to the second row.
            currentRow.Cells.Add(new TableCell(new Paragraph(new Run("Name"))));
            currentRow.Cells.Add(new TableCell(new Paragraph(new Run("Nr."))));


            foreach (DashboardRow dashboardRow in dashboardWorkflow.dashboardRows)
            {
                additemrow(newRowGroup, dashboardRow);
            }
            
        }
        private void createWorkflow(object sender, RoutedEventArgs e)
        {
            try
            {
                Button button = sender as Button;
                int id = (int)button.Tag;
                _workflowViewModel.createWorkflow(id, userName);
            }
            catch (Exception exc)
            {
                Console.WriteLine("Fehler beim erstellen");
                Console.WriteLine(exc.ToString());
            }

        }
        private void additemrow(TableRowGroup newRowGroup, DashboardRow dashboardRow)
        {
            TableRow currentRow = new TableRow();
            newRowGroup.Rows.Add(currentRow);

            // Global formatting for the header row.
            currentRow.FontSize = 18;
            currentRow.FontWeight = FontWeights.Normal;

            // Add cells with content to the second row.
            String nr = ""+dashboardRow.actStep.id;
            String name = "" + dashboardRow.actStep.label;
            currentRow.Cells.Add(new TableCell(new Paragraph(new Run(nr))));
            currentRow.Cells.Add(new TableCell(new Paragraph(new Run(name))));
            var button = new System.Windows.Controls.Primitives.ToggleButton();
            MyToggleButton toggle = dashboardRow.toggleButton;
            toggle.Click += new RoutedEventHandler(stepForward);
            toggle.Content = "Abschließen";
            var block = new BlockUIContainer(toggle);
            currentRow.Cells.Add(new TableCell(block));
        }
        private void stepForward(object sender, RoutedEventArgs e)
        {
            MyToggleButton toggle = sender as MyToggleButton;
            _workflowViewModel.stepForward(toggle.stepId, toggle.itemId, toggle.username);
        }
    }
}
