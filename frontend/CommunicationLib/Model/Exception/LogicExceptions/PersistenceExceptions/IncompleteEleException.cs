﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class IncompleteEleException : PersistenceException
    {
        private int _number = 11210;
        public int number { get { return _number; } }

        public IncompleteEleException()
        {

        }

        public IncompleteEleException(string message)
         : base(message)
        {
        }

        public IncompleteEleException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}