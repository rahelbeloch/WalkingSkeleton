using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;

namespace Admin.Helpers
{
    /// <summary>
    /// BooleanToGermanStringConverter class
    /// </summary>
    public class BooleanToGermanStringConverter :IValueConverter
    {
        /// <summary>
        /// Convert Method for turning a Boolean into a String
        /// </summary>
        /// <param name="value"></param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns></returns>
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

        /// <summary>
        /// ConvertBack Method for turning a string into a Boolean
        /// </summary>
        /// <param name="value"></param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns></returns>
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
