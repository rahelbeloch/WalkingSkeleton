using System;
using System.Collections.Generic;
using System.Linq;
using System.Security;
using System.Text;
using System.Threading.Tasks;

namespace UserClient.Model
{
    public class Authentication
    {
        public static bool Authenticate1(string UserName, SecureString Password)
        {
            return true; // Do authentication against database, web service, whatever
        }
    }
}
