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
using UserClient.ViewModel;
using UserClient.Model;
using CommunicationLib.Model;
using CommunicationLib;
using RestAPI;

namespace UserClient
{
    /// <summary>
    /// Interaktionslogik für MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private FlowDocument flowDoc;
        private Table table1;
        private IList<Workflow> workflows;
        private String userName;
        public MainWindow()
        {
            InitializeComponent();
            //InitializeViewModel();
        }
        private void InitializeViewModel()
        {
            //sollte nicht alles über bindings erledigt werden können.
        }
        private void Login_Click(object sender, RoutedEventArgs e)
        {
            InitializeDashboard();
            try
            {
                RestRequester.checkUser(txtName.Text, txtPassword.SecurePassword);
                userName = txtName.Text;
                //User user = RestRequester.GetObject<User>(); methode wird noch abgeändert
                //LoginLayer.Visibility = Model.Authentication.Authenticate1(txtName.Text, txtPassword.SecurePassword) ? Visibility.Collapsed : Visibility.Visible;
                //init_Dashboard();
            }
            catch (Exception exc)
            {

            }
            finally
            {
                LoginLayer.Visibility = Visibility.Collapsed;
            }
        }
        private void Log_Out(object sender, RoutedEventArgs e)
        {
            LoginLayer.Visibility = Visibility.Visible;
        }
        private void InitializeDashboard()
        {
            try
            {

                workflows = RestRequester.GetAllObjects<Workflow>(userName);
                Console.WriteLine("test workflows");
                foreach (Workflow workflow in workflows)
                {
                    Console.WriteLine(workflow.id);
                    foreach (Step step in workflow.steps)
                    {
                        Console.WriteLine(step.label);
                    }
                }
                String test = workflows[0].ToString();
                Console.WriteLine(test);
            }
            catch (Exception exe)
            {
                Console.WriteLine("Exception " + exe.Message);
            }
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
            // Create 2 columns and add them to the table's Columns collection.
            /* wird noch nicht verwendet
            int numberOfColumns = 2;
            for (int x = 0; x < numberOfColumns; x++)
            {
                table1.Columns.Add(new TableColumn());

                // Set alternating background colors for the middle colums. 
                if (x % 2 == 0)
                    table1.Columns[x].Background = Brushes.Beige;
                else
                    table1.Columns[x].Background = Brushes.LightSteelBlue;
            }
            */
            addworkflowasrow("Workflow1");
            addworkflowasrow("Workflow2");
           
        }

        private void addworkflowasrow(String title)
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

            var button = new Button();
            button.Content = "Neu erstellen";
            button.FontSize = 18;
            var block = new BlockUIContainer(button);
            currentRow.Cells.Add(new TableCell(block));

            // Add the second (header) row.
            currentRow = new TableRow();
            newRowGroup.Rows.Add(currentRow);

            // Global formatting for the header row.
            currentRow.FontSize = 18;
            currentRow.FontWeight = FontWeights.Bold;

            // Add cells with content to the second row.
            currentRow.Cells.Add(new TableCell(new Paragraph(new Run("Name"))));
            currentRow.Cells.Add(new TableCell(new Paragraph(new Run("Nr."))));

            additemrow(newRowGroup);
            additemrow(newRowGroup);
        }
        private void additemrow(TableRowGroup newRowGroup)
        {
            TableRow currentRow = new TableRow();
            newRowGroup.Rows.Add(currentRow);

            // Global formatting for the header row.
            currentRow.FontSize = 18;
            currentRow.FontWeight = FontWeights.Normal;

            // Add cells with content to the second row.
            currentRow.Cells.Add(new TableCell(new Paragraph(new Run("foo"))));
            currentRow.Cells.Add(new TableCell(new Paragraph(new Run("bar"))));
            var button = new System.Windows.Controls.Primitives.ToggleButton();
            button.Content = "Abschließen/Annehmen";
            var block = new BlockUIContainer(button);
            currentRow.Cells.Add(new TableCell(block));
        }
    }
}
