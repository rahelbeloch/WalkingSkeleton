using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using NLog;

namespace UnitTestProject1
{

    [TestClass]
    public class LoggingTest
    {

        private readonly Logger logger = LogManager.GetCurrentClassLogger();

        [TestMethod]
        public void LogTest()
        {
            logger.Info("Test");
            logger.Warn("etwas krasser Test");
            logger.Error("krasser Test");

            try{
                int n = 0;
                int i = 1/n;
            }catch(DivideByZeroException d){
                logger.Error("divide by zero test", d);
            }


            Assert.IsTrue(true == true);
        }
    }

    [TestClass]
    public class LoggingTest2
    {

        private readonly Logger logger = LogManager.GetCurrentClassLogger();

        [TestMethod]
        public void LogTest()
        {
            logger.Info("Test2");
            logger.Warn("etwas krasser Test2");
            logger.Error("krasser Test2");

            try
            {
                int n = 0;
                int i = 1 / n;
            }
            catch (DivideByZeroException d)
            {
                logger.Error("divide by zero Test 2", d);
            }


            Assert.IsTrue(true == true);
        }
    }
}
