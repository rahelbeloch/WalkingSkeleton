using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace CommunicationLib.Exception
{
    public class BasicException : System.Exception
    {
        private int _number = 10000;
        public int number { get { return _number; }}
        
        public BasicException()
        {

        }

        public BasicException(string message)
         : base(message)
        {
        }

        public BasicException(string message, System.Exception inner)
         : base(message, inner)
        { 
        }


    }
}
