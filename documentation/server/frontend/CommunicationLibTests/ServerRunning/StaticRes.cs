using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UnitTestProject1
{
    /// <summary>
    /// This class holds static variables for client testing.
    /// ATTENTION: Before testing on your own device, please adjust SERVER_URL and BROKER_URL!
    /// </summary>
    public static class StaticRes
    {
        /// <summary>
        /// The server URL.
        /// </summary>
        public static string SERVER_URL = "http://localhost:18887";

        /// <summary>
        /// The broker URL.
        /// </summary>
        public static string BROKER_URL = "tcp://localhost:61616";
    }
}
