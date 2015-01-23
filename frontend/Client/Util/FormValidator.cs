using Client.ViewModel;
using NLog;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Controls;
using System.Windows.Data;

namespace Client.Util
{
    /// <summary>
    /// This class validates a handed over form returns the result to calller.
    /// </summary>
    public class FormValidator : ValidationRule
    {
        private readonly Logger logger = LogManager.GetCurrentClassLogger();
        
        /// <summary>
        /// Validates the edited value in the formular.
        /// </summary>
        /// <param name="value">the BindingGroup for the edited value</param>
        /// <param name="cultureInfo"></param>
        /// <returns></returns>
        public override ValidationResult Validate
          (object value, System.Globalization.CultureInfo cultureInfo)
        {
            var bindingGroup = value as BindingGroup;
            if (bindingGroup != null)
            {
                StringConverter stringConverter = new StringConverter();
                FormRow entry = bindingGroup.Items[0] as FormRow;
                String datatype = FormRow._typeMap.FirstOrDefault(x => x.Value == entry.datatype).Key; ;
                logger.Debug(datatype);
                if (!entry.value.Equals(""))
                {
                    logger.Debug("validation for" + entry.value);
                   
                    Regex regex = null;
                    switch (datatype)
                    {
                        case "String":
                            regex = new Regex("[a-zA-Z]+");
                            break;
                        case "int":
                            regex = new Regex("[0-9]+");
                            break;
                        case "double":
                            regex = new Regex("([0-9]|[1-9]+)\\.[0-9]+$");
                            break;
                    }

                    Match match = regex.Match(entry.value);
                    if (match.Success && match.Value.Equals(entry.value))
                    {
                        logger.Debug("match successfull");
                        logger.Debug(match.Value);
                        entry.value = match.Value;
                    }
                    else
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