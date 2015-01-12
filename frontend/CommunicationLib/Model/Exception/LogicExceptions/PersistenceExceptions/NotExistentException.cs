﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class NotExistentException : PersistenceException
    {
        /// <summary>
        /// This Exception es a child of PersistenceException. 
        /// Each Exception has an number, here it is 11250. 
        /// </summary>
        private int _number = 11250;
        new public int number { get { return _number; } }

        public NotExistentException()
            : base("Das Element existiert nicht.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public NotExistentException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public NotExistentException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}