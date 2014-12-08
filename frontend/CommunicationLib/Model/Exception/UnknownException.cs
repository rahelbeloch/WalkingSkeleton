using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace CommunicationLib.Exception
{
    public class UnknownException : BasicException
    {
        private int _number = 9999;
        new public int number { get { return _number; }}
        
        public UnknownException()
        {

        }

        public UnknownException(string message)
         : base(message)
        {
        }

        public UnknownException(string message, System.Exception inner)
         : base(message, inner)
        { 
        }


    }
}
