using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Admin.ViewModel
{
    /// <summary>
    /// Default base class for ViewModels in the MVVM pattern
    /// with boilerplate-clode for property changes.
    /// </summary>
    public class ViewModelBase : INotifyPropertyChanged
    {
        /// <summary>
        /// Event to notify observer of observable events
        /// </summary>
        public event PropertyChangedEventHandler PropertyChanged;

        /// <summary>
        /// OnChanged property
        /// </summary>
        /// <param name="propertyName"></param>
        protected void OnChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }
    }
}
