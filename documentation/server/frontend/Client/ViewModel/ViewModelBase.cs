using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.ViewModel
{
    /// <summary>
    /// Default base class for ViewModels in the MVVM pattern
    /// with boilerplate-clode for property changes.
    /// </summary>
    public class ViewModelBase : INotifyPropertyChanged
    {
        /// <summary>
        /// The event for a changed property.
        /// </summary>
        public event PropertyChangedEventHandler PropertyChanged;

        /// <summary>
        /// Is called if model changes; causes a change of the associated ViewModel.
        /// </summary>
        /// <param name="propertyName">the changed propterty</param>
        protected void OnChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }
    }
}