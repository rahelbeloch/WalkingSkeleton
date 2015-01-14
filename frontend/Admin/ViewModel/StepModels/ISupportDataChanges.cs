using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Input;

namespace Admin
{
    public interface ISupportDataChanges
    {
        ICommand ShowDataChangeWindowCommand { get; }
    }
}
