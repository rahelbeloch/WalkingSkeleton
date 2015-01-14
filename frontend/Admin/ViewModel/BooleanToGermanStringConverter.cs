using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;

namespace Admin.ViewModel
{
    public class BooleanToGermanStringConverter :IValueConverter
    {
        public object Convert(object value, Type targetType,
        object parameter, CultureInfo culture)
        {
            Boolean booleanObject = (Boolean)value;
            if (booleanObject)
            {
                return "ja";
            } else 
            {
                return "nein";
            }
        }
        public object ConvertBack(object value, Type targetType,
        object parameter, CultureInfo culture)
        {
            String stringObject = (String)value;
            if(stringObject.Equals("ja")) {
                return true;
            } else {
                return false;
            }
        }
    }
}
