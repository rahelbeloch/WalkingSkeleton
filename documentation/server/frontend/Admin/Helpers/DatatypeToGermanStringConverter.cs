using CommunicationLib.Model;
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
    /// DatatypeToGermanStringConverter class.
    /// </summary>
    public class DatatypeToGermanStringConverter : IValueConverter
    {
        /// <summary>
        /// Convert Method for turning a datatype name into a german string.
        /// </summary>
        /// <param name="value"></param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns></returns>
        public object Convert(object value, Type targetType,
        object parameter, CultureInfo culture)
        {
            if (value == null)
            {
                return "";
            }

            string stringObject = (string)value;
            if (Constants.TYPE_MAP.ContainsKey(stringObject)) 
            {
                return Constants.TYPE_MAP[stringObject]; 
            }
            return stringObject;
        }

        /// <summary>
        /// ConvertBack Method for turning a german string into a datatype name.
        /// </summary>
        /// <param name="value"></param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns></returns>
        public object ConvertBack(object value, Type targetType,
        object parameter, CultureInfo culture)
        {
            if (value.Equals(""))
            {
                return null;
            }

            foreach (KeyValuePair<string, string> entry in Constants.TYPE_MAP)
            {
                if (entry.Value.Equals(value))
                {
                    return entry.Key;
                }
            }

            return value;
        }
    }
}
