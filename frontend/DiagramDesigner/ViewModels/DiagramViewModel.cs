using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections.ObjectModel;
using System.Collections.Specialized;
using System.ComponentModel;



namespace DiagramDesigner
{
    public class DiagramViewModel : INPCBase, IDiagramViewModel
    {
        private ObservableCollection<SelectableDesignerItemViewModelBase> items = new ObservableCollection<SelectableDesignerItemViewModelBase>();

        private ObservableCollection<DesignerItemViewModelBase> _selectesItemsCollection = new ObservableCollection<DesignerItemViewModelBase>();
        
        /// <summary>
        /// Property to lock the canvas. Moving and deleting items will be disabled if true.
        /// </summary>
        private bool _locked;
        public bool locked
        {
            get 
            { 
                return _locked; 
            } 
            set 
            { 
                _locked = value; 
                NotifyChanged("locked"); 
            } 
        }

        public DiagramViewModel()
        {
            AddItemCommand = new SimpleCommand(ExecuteAddItemCommand);
            RemoveItemCommand = new SimpleCommand(ExecuteRemoveItemCommand);
            ClearSelectedItemsCommand = new SimpleCommand(ExecuteClearSelectedItemsCommand);
            CreateNewDiagramCommand = new SimpleCommand(ExecuteCreateNewDiagramCommand);
            
            
            Mediator.Instance.Register(this);

            items.CollectionChanged += this.OnCollectionItemChanged;
        }

        private void OnCollectionItemChanged(object sender, NotifyCollectionChangedEventArgs e)
        {
            if (e.NewItems != null)
            {
                foreach (SelectableDesignerItemViewModelBase newItem in e.NewItems)
                {
                    newItem.PropertyChanged += this.OnItemChanged;
                }
            }
            if (e.OldItems != null)
            {
                foreach (SelectableDesignerItemViewModelBase oldItem in e.OldItems)
                {
                    oldItem.PropertyChanged -= this.OnItemChanged;
                }
            }
        }
        private void OnItemChanged(object sender, PropertyChangedEventArgs e)
        {
            DesignerItemViewModelBase step = sender as DesignerItemViewModelBase;
            if (step != null)
            {
                if(step.IsSelected == true)
                {
                    if (!_selectesItemsCollection.Contains(step))
                    {
                        _selectesItemsCollection.Add(step);
                    }
                }
                if (step.IsSelected == false)
                {
                    if (_selectesItemsCollection.Contains(step))
                    {
                        _selectesItemsCollection.Remove(step);
                    }
                }

            }
            
        }

        [MediatorMessageSink("DoneDrawingMessage")]
        public void OnDoneDrawingMessage(bool dummy)
        {
            foreach (var item in Items.OfType<DesignerItemViewModelBase>())
            {
                item.ShowConnectors = false;
            }
        }

        
        public SimpleCommand AddItemCommand { get; private set; }
        public SimpleCommand RemoveItemCommand { get; private set; }
        public SimpleCommand ClearSelectedItemsCommand { get; private set; }
        public SimpleCommand CreateNewDiagramCommand { get; private set; }

        public ObservableCollection<SelectableDesignerItemViewModelBase> Items
        {
            get { return items; }
        }

        public ObservableCollection<DesignerItemViewModelBase> SelectedItemsCollection
        {
            get { return _selectesItemsCollection; }
        }

        public List<SelectableDesignerItemViewModelBase> SelectedItems
        {
            get { return Items.Where(x => x.IsSelected).ToList(); }
        }

        private void ExecuteAddItemCommand(object parameter)
        {
            if (parameter is SelectableDesignerItemViewModelBase)
            {
                
                //gehoert zu DiagrammDesigner
                SelectableDesignerItemViewModelBase item = (SelectableDesignerItemViewModelBase)parameter;
                item.Parent = this;
                items.Add(item);
            }
        }

        private void ExecuteRemoveItemCommand(object parameter)
        {
            if (!locked)
            {
                if (parameter is SelectableDesignerItemViewModelBase)
                {
                    SelectableDesignerItemViewModelBase item = (SelectableDesignerItemViewModelBase)parameter;
                    items.Remove(item);
                }
            }
        }

        private void ExecuteClearSelectedItemsCommand(object parameter)
        {
            foreach (SelectableDesignerItemViewModelBase item in Items)
            {
                item.IsSelected = false;
            }
        }

        private void ExecuteCreateNewDiagramCommand(object parameter)
        {
            Items.Clear();
        }
    }
}
