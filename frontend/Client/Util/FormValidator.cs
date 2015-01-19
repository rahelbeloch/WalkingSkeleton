using Client.ViewModel;
using NLog;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;
using System.Windows.Data;

namespace Client.Util
{
    public class FormValidator : ValidationRule
    {
        private readonly Logger logger = LogManager.GetCurrentClassLogger();
        public override ValidationResult Validate
          (object value, System.Globalization.CultureInfo cultureInfo)
        {
            var bindingGroup = value as BindingGroup;
            if (bindingGroup != null)
            {
                StringConverter stringConverter = new StringConverter();
                FormRow entry = bindingGroup.Items[0] as FormRow;
                String datatype = entry.datatype;
                if (!entry.value.Equals(""))
                {
                    try
                    {
                        logger.Debug("validation for" + entry.value);
                        logger.Debug("datatye: " + datatype);
                        switch (datatype)
                        {
                            case "String":
                                break;
                            case "int":
                                logger.Debug(Int32.Parse(entry.value));
                                break;
                            case "float":
                                logger.Debug(float.Parse(entry.value, CultureInfo.InvariantCulture.NumberFormat));
                                break;
                        }
                    }
                    catch (Exception e)
                    {
                        String message = entry.value + " entspricht nicht dem richtigen Datentyp!";
                        return new ValidationResult(false,message);
                    }
                }
            }
            return ValidationResult.ValidResult;
        }
    }
}
