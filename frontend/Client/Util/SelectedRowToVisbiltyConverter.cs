using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Data;

namespace Client.Util
{
    /// <summary>
    /// This class converts given objects two way; to Visibility or back to object.
    /// </summary>
    public class SelectedRowToVisbiltyConverter : IValueConverter
    {
        /// <summary>
        /// Converts the selected Row to Visbility.
        /// </summary>
        /// <param name="value">The selected Row</param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns>fitting Visibility</returns>
        public object Convert(object value, Type targetType,
        object parameter, CultureInfo culture)
        {
            if (value == null)
            {
                return Visibility.Hidden;
            }
            else
            {
                return Visibility.Visible;
            }
        }

        /// <summary>
        /// Converts the Visibility back to object.
        /// </summary>
        /// <param name="value"></param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns></returns>
        public object ConvertBack(object value, Type targetType,
        object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
