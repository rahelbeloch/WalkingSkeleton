package restserver;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("help")
public class Resource {
	@GET @Path("site")
    @Produces(MediaType.TEXT_HTML)
    public String getHelpAsHTML() {
    	return "<html>" +
    			"<head><title>GeoInfos</title></head>" +
    			"<body><h1>Info</h1>" +
    			"Dies ist eine <strong>coole</strong>Anwendung." +
    			"</body></html>";
    }
}
