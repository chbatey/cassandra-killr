package info.batey.cassandra.breaker;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/prime")
public class PrimingResource {

    @POST
    @Path("readtimeout")
    @Produces(MediaType.APPLICATION_JSON)
    public void makeItBreak() {
        PrimingServer.setReadtimeout(true);
    }

    @POST
    @Path("reset")
    @Produces(MediaType.APPLICATION_JSON)
    public void success() {
        PrimingServer.setReadtimeout(false);
    }
}
