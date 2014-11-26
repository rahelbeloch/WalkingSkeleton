﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    public class Item : RootElement
    {
        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private int _workflowId;
        public int workflowId { get { return _workflowId; } set { _workflowId = value; } }

        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private List<MetaEntry> _metadata;
        public List<MetaEntry> metadata { get { return _metadata; } set { _metadata = value; } }

        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private bool _finished;
        public bool finished { get { return _finished; } set { _finished = value; } }

        public Item()
            : base()
        {
            _metadata = new List<MetaEntry>();
        }
    }
}
