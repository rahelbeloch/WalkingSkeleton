﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    public class Form : RootElement
    {
        private Dictionary<string, string> _formDef;
        public Dictionary<string, string> formDef { get { return _formDef; } set { _formDef = value; } }
        private string _description;
        public string description { get { return _description; } set { _description = value; } }

        public Form()
            : base()
        {
            _formDef = new Dictionary<string, string>();
            _formDef.Add("key", "value");
            _description = "";
        }

        public Form(string description)
            : base()
        {
            _formDef = new Dictionary<string, string>();
            _formDef.Add("key", "value");
            _description = description;
        }
    }
}