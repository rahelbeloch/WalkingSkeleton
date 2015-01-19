using NLog;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;

namespace Client.ViewModel
{
    public class FormValidator : ValidationRule
    {
        private readonly Logger logger = LogManager.GetCurrentClassLogger();
        private Type _type;
        public Type type { get { return _type; } set { _type = value; } }
        public override ValidationResult Validate
          (object value, System.Globalization.CultureInfo cultureInfo)
        {
            logger.Debug(type.ToString);
            StringConverter stringConverter = new StringConverter();
            try
            {
                stringConverter.ConvertTo(value, type);
            }
            catch (Exception e)
            {
                return new ValidationResult(false, value.ToString()+" ist nicht vom Typ "+_type.ToString());
            }
            return ValidationResult.ValidResult;
        }
    }
}
