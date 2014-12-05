﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class AlreadyExistsException : PersistenceException
    {
        private int _number = 11220;
        public int number { get { return _number; } }

        public AlreadyExistsException()
        {

        }

        public AlreadyExistsException(string message)
            : base(message)
        {
        }

        public AlreadyExistsException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
