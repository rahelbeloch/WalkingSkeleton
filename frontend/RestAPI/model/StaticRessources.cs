using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.model
{
    class ErrorMessageMapper
    {
        private static Dictionary<int, string> errorMessages = new Dictionary<int, string>()
        {   
            {200, "request successfully done"},
            
            // Error codes for errors concerning user operations
            {1001,"user does not exist in db"},
            {1002,"user already existing"},
            {1003,"user is not permitted for access"},
            {1004,""},
            {1005,""},
            // Error codes for errors concerning workflows
            {2001,"workflow does not exist"},
            {2002,""},
            {2003,""},
            // Error codes for errors concerning ??
            {3001,""},
            {3002,""},
            {3003,""}
        
        };



    }
}
