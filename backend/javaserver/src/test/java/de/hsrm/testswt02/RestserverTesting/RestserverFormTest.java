package de.hsrm.testswt02.RestserverTesting;

import javax.ws.rs.client.Client;

import de.hsrm.swt02.restserver.RestServer;

/**
 * 
 * @author akoen001
 *
 * Test class for the FormResource
 *
 */
public class RestserverFormTest {

    public static RestServer restServer;
    public static Client client;
    public static String targetUrl;
    public static String headerUsername = "TestAdmin";
    public static String headerPW = "abc123";
    public static String headerClientID = "admin";

//    /**
//     * This method sets and starts the REST-Server. Additionally it provides a test client.
//     */
//    @BeforeClass
//    public static void setUp() {
//        LogConfigurator.setup();
//        restServer = new RestServer();
//        restServer.startHTTPServer();
//        client = ClientBuilder.newClient();
//        final Properties properties = new Properties();
//        BufferedInputStream stream;
//        // read configuration file for rest properties
//        try {
//            stream = new BufferedInputStream(new FileInputStream(
//                    "server.config"));
//            properties.load(stream);
//            stream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//        targetUrl = properties.getProperty("RestServerURI");
//    }
//    
//    /**
//     * This test checks if a new user can be saved.
//     * It's successful if the response is 200.
//     */
//    @Test
//    public void testSaveForm() {
//        final int httpstatus = 200;
//        final de.hsrm.swt02.model.Form testForm = new de.hsrm.swt02.model.Form();
//        final ObjectMapper mapper = new ObjectMapper();
//        final Form dataform;
//        String formAsString = null;
//        testForm.setDescription("Ein Form.");
//        
//        try {
//            formAsString = mapper.writeValueAsString(testForm);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        dataform = new Form().param("data", formAsString);
//        final Response resp = client
//                .target(targetUrl)
//                .path("resource/forms")
//                .request()
//                .header("username", headerUsername)
//                .header("password", headerPW)
//                .header("clientID",headerClientID)
//                .post(Entity.entity(dataform,
//                        MediaType.APPLICATION_FORM_URLENCODED));
//        assertEquals(httpstatus, resp.getStatus());
//    }
//    
//    /**
//     * This test checks if the client can get a new user successfully.
//     * It succeeds if the requested user is the same as the sent user.
//     */
//    @Test
//    public void testGetAllForms() {
//        final ObjectMapper mapper = new ObjectMapper();
//        String formsAsString = null;
//        List<de.hsrm.swt02.model.Form> testForms = null;
//        
//        final de.hsrm.swt02.model.Form testForm = new de.hsrm.swt02.model.Form();
//        final Form dataform;
//        String formAsString = null;
//        testForm.setDescription("Ein weiteres Form.");
//        
//        try {
//            formAsString = mapper.writeValueAsString(testForm);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        dataform = new Form().param("data", formAsString);
//        client
//                .target(targetUrl)
//                .path("resource/forms")
//                .request()
//                .header("username", headerUsername)
//                .header("password", headerPW)
//                .header("clientID",headerClientID)
//                .post(Entity.entity(dataform,
//                        MediaType.APPLICATION_FORM_URLENCODED));
//        
//        formsAsString = client.target(targetUrl)
//                .path("resource/forms").request()
//                .header("username", headerUsername)
//                .header("password", headerPW)
//                .header("clientID",headerClientID)
//                .get(String.class);
//        
//        try {
//            testForms = mapper.readValue(formsAsString, new TypeReference<List<de.hsrm.swt02.model.Form>>() { } );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//        assertTrue(testForms.contains(testForm));
//
//    }
//    
//    /**
//     * After the test execution, stop server and close client.
//     */
//    @AfterClass
//    public static void cleanUp() {
//        restServer.stopHTTPServer(true);
//    }
    
}
