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

namespace UserClient
{
    /// <summary>
    /// Interaktionslogik für MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private FlowDocument flowDoc;
        private Table table1;

        public MainWindow()
        {
            InitializeComponent();
            init_Dashboard();
        }
        private void Login_Click(object sender, RoutedEventArgs e)
        {
            LoginLayer.Visibility = Model.Authentication.Authenticate1(txtName.Text, txtPassword.SecurePassword) ? Visibility.Collapsed : Visibility.Visible;
            //init_Dashboard();
        }
        private void Log_Out(object sender, RoutedEventArgs e)
        {
            LoginLayer.Visibility = Visibility.Visible;
        }
        private void init_Dashboard()
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
            add_Workflow_as_row("Workflow1");
            add_Workflow_as_row("Workflow2");
           
        }

        private void add_Workflow_as_row(String title)
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

            // Add the second (header) row.
            currentRow = new TableRow();
            newRowGroup.Rows.Add(currentRow);

            // Global formatting for the header row.
            currentRow.FontSize = 18;
            currentRow.FontWeight = FontWeights.Bold;

            // Add cells with content to the second row.
            currentRow.Cells.Add(new TableCell(new Paragraph(new Run("Name"))));
            currentRow.Cells.Add(new TableCell(new Paragraph(new Run("Nr."))));

            // Add the third row.
            currentRow = new TableRow();
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
