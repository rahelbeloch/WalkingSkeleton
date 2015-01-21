using CommunicationLib.Model.DataModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This class represents a Form for workflows.
    /// </summary>
    public class Form : RootElement
    {
        /// <summary>
        /// List of form definitions/form entries.
        /// </summary>
        public List<FormEntry> formDef { get { return _formDef; } set { _formDef = value; } }
        private List<FormEntry> _formDef;
        
        /// <summary>
        /// Description of this form.
        /// </summary>
        public string description { get { return _description; } set { _description = value; } }
        private string _description;
        
        /// <summary>
        /// Default constructor.
        /// </summary>
        public Form()
            : base()
        {
            _formDef = new List<FormEntry>();
            _description = "";
        }

        /// <summary>
        /// Constructor with description handed over.
        /// </summary>
        /// <param name="description">description string</param>
        public Form(string description)
            : base()
        {
            _formDef = new List<FormEntry>();
            _description = description;
        }
    }
}