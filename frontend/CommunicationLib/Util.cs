using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Configuration;
using System.Text.RegularExpressions;
using CommunicationLib.Exception;
using CommunicationLib.Model;

namespace CommunicationLib
{
    /// <summary>
    /// This class offers some utility methods like validating server/broker urls or saving attributes to config file.
    /// </summary>
    public static class Util
    {
        /// <summary>
        /// Saves server/broker urls after validating them to config file of the calling client.
        /// </summary>
        /// <param name="configPath">path of the config file</param>
        /// <param name="brokerAddress">broker address to save</param>
        /// <param name="serverAddress">server address to save</param>
        public static void SaveAddressesToConfig(string configPath, string brokerAddress, string serverAddress)
        {
            // save server and broker address to config file
            Configuration config = ConfigurationManager.OpenExeConfiguration(configPath);

            // test if given addresses are valid
            if (ValidateAddress(serverAddress) && ValidateAddress(brokerAddress))
            {
                if (config.AppSettings.Settings[Constants.SERVER_ADDRESS_NAME] == null)
                {
                    config.AppSettings.Settings.Add(Constants.SERVER_ADDRESS_NAME, serverAddress);
                }
                else
                {
                    config.AppSettings.Settings[Constants.SERVER_ADDRESS_NAME].Value = serverAddress;
                }

                if( config.AppSettings.Settings[Constants.BROKER_ADDRESS_NAME] == null)
                {
                    config.AppSettings.Settings.Add(Constants.BROKER_ADDRESS_NAME, brokerAddress);
                }
                else
                {
                    config.AppSettings.Settings[Constants.BROKER_ADDRESS_NAME].Value = brokerAddress;
                }
            }
            else
            {
                throw new InvalidAddressException();
            }

            config.Save(ConfigurationSaveMode.Modified);
            ConfigurationManager.RefreshSection("appSettings");
        }

        /// <summary>
        /// Validate the passed address by means of a regex pattern.
        /// </summary>
        /// <param name="matchString">the string to test</param>
        /// <returns>true if succeded, else false</returns>
        public static bool ValidateAddress(string matchString)
        {
            if (matchString != null)
            {
                // calling regex for server address
                Match matchIp = Regex.Match(matchString, Constants.URLPATTERN, RegexOptions.IgnoreCase);

                // check the match success
                return matchIp.Success;
            }

            return false;
        }
    }
}