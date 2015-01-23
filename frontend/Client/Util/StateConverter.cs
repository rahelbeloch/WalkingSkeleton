using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;

namespace Client.Util
{
    /// <summary>
    /// BooleanToGermanStringConverter class.
    /// </summary>
    public class StateConverter : IValueConverter
    {
        /// <summary>
        /// Convert Method for turning a String into a String.
        /// </summary>
        /// <param name="value"></param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns></returns>
        public object Convert(object value, Type targetType,
        object parameter, CultureInfo culture)
        {
            String state = (String)value;
            switch (state)
            {
                case "OPEN":
                    return "offen";
                case "BUSY":
                    return "in Bearbeitung";
                case "CLOSED":
                    return "geschlossen";
                case "INACTIVE":
                    return "inaktiv";
                default:
                    return "fehler";
            }
        }

        /// <summary>
        /// ConvertBack Method for turning a string into a String
        /// </summary>
        /// <param name="value"></param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns></returns>
        public object ConvertBack(object value, Type targetType,
        object parameter, CultureInfo culture)
        {
            String state = (String)value;
            switch (state)
            {
                case "offen":
                    return "OPEN";
                case "in Bearbeitung":
                    return "BUSY";
                case "geschlossen":
                    return "CLOSED";
                case "inaktiv":
                    return "INACTIVE";
                default:
                    return "ERROR";
            }
        }
    }
}
