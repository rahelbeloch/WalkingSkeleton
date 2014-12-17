using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib
{
    /// <summary>
    ///  Class to generate specific URLs for server communication.
    /// </summary>
    public static class URLRouter
    {
        /// <summary>
        ///  Generate a url without typeparam. 
        /// </summary>
        /// <param name="method">The URL-Method (Enum beneath class)</param>
        /// <param name="values">route params</param>
        /// <returns>the url</returns>
        public static string generateUrl(UrlMethod method, params string [] values)
        {
            return generateUrl(method, null, values);
        }

        /// <summary>
        ///  Generate URL with typeparam.
        /// </summary>
        /// <param name="method">The URL-Method (Enum beneath class)</param>
        /// <param name="objType">type of requestet resource</param>
        /// <param name="values">route params</param>
        /// <returns>the url</returns>
        public static string generateUrl(UrlMethod method,Type objType, params string[] values)
        {
            String url = (method == UrlMethod.Resource) ? "resource/" : "command/";
           
            if (objType != null)
            {
                String typeName = objType.FullName.Split('.').Last().ToLower();
                url += "/" + typeName + "s";
            }

            // append all url params to url; seperated by '/'
            foreach (var val in values)
            {
                url += "/" + val;
            }
            Console.WriteLine("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + url);
            return url;
        }
    }

    public enum UrlMethod
    {
        Resource,
        Operation
    };

    

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
