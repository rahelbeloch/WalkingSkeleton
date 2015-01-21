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
    public class SelectedRowToVisbiltyConverter : IValueConverter
    {
        /// <summary>
        /// converts the selected Row to Visbility
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
        public object ConvertBack(object value, Type targetType,
        object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
