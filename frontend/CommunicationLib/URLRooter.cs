using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib
{
    
    public static class URLRooter
    {
        enum UrlMethod 
        {
            Resource,
            Operation 
        };


        public string generateUrl(UrlMethod method, params string [] values)
        {
            String url = (method == UrlMethod.Resource)? "resource" : "command";
            foreach(var val in values)
            {
                url += "/" + val;
            }

            return url;
        }

        public string generateUrl(UrlMethod method,Type objType, params string[] values)
        {
            String typeName = objType.FullName.Split('.').Last().ToLower();
            typeName += "s";
            String url = (method == UrlMethod.Resource) ? "resource" : "command";
            url += typeName;

            foreach (var val in values)
            {
                url += "/" + val;
            }

            return url;
        }
    }
    

    /* Vorhandene URLS in Rest-Schnitstelle
     * 
     * ALT: resource/<typename>s (alle Workflows holen) (method GET)
     * NEU: resource/<typename>s
     * 
     * ALT: resource/<typename>s/<username> 
     * NEU: resource/<typename>s 
     * 
     * ALT: resource/<typename>s/startables/<username>
     * NEU: resource/<typename>s/startables
     * 
     * ALT: resource/<typename>s/<username>/<workflowId>
     * NEU: resource/<typename>s/<workflowId>
     * 
     * ALT: resource/<typename>/<id>
     * NEU: resource/<typename>s/<id>
     * 
     * ALT: resource/<typename>/<username>
     * NEU: resource/<typename>s
     * 
     * ALT: resource/<typename>
     * NEU: resource/<typename>s
     * 
     * ALT: command/user/login
     * NEU: command/users/login
     * 
     * ALT: command/workflow/start/<id>/<username>
     * NEU: command/workflows/start/<id>
     * 
     * ALT: command/workflow/forward/<stepId>/<itemId>/<username>
     * NEU: command/workflows/forward/<stepId>/<itemId>
     * 
     * Änderungen: überall MIT 's'; 
     */
}
