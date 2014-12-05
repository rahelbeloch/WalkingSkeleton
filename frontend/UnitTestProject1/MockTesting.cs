/*
using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using CommunicationLib.Model;
using Nancy;

namespace UnitTestProject1
{
    [TestClass]
    public class MockTesting
    {
        [TestMethod]
        public void TestMethod1()
        {
        }
    }
    
    public class TestServerModule : NancyModule
    {

        const string DOMAIN = "http://localhost:8080";

        public TestServerModule()
        {
            // Route to get a workflow
            Get["/resource/workflow/{val:int}"] = _testID =>
            {
                Workflow newWf = new Workflow();
                newWf.id = (int)_testID.val;
                return newWf;
            };

            // Route to get a workflow
            Get["/resource/user/{val:string}"] = _ =>
            {
                User newUs = new User();
                newUs.username = _.val;
                return newUs;
            };

            // Route to post a workflow
            Post["/resource/workflow"] = parameters =>
            {
                return HttpStatusCode.OK;
            };

            // Route to delete a workflow
            Delete["/resource/workflow/{id}"] = parameters =>
            {
                Workflow newWf = new Workflow();
                newWf.id = parameters.val;
                return newWf;
            };

            // Route to login an user
            Post["/command/user/login"] = parameters =>
            {
                return HttpStatusCode.OK;
            };

            // Route to start a workflow
            Post["/command/workflow/start/{wId}/{username}"] = parameters =>
            {
                return HttpStatusCode.OK;
            };

            // Route to forward a workflow
            Post["/command/workflow/forward/{stepId}/{itemId}/{username}"] = parameters =>
            {
                return HttpStatusCode.OK;
            };

        }
        
        public void Init()
        {
            // create a new self-host server
            var nancyHost = new Nancy.Hosting.Self.NancyHost(new Uri(DOMAIN));
            // start
            nancyHost.Start();
            Console.WriteLine("REST service listening on " + DOMAIN);
            // stop with an <Enter> key press
            Console.ReadLine();
            nancyHost.Stop();
        }
     
    }
}
*/