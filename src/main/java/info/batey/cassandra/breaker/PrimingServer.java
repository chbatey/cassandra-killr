package info.batey.cassandra.breaker;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrimingServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrimingServer.class);
    private static volatile boolean isReadtimeout = false;

    public void start() {

        LOGGER.info("Starting priming server");

        ServletHolder sh = new ServletHolder(ServletContainer.class);
        sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        sh.setInitParameter("com.sun.jersey.config.property.packages", "info.batey.cassandra.breaker");
        sh.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

        final Server jettyServer = new Server(9999);
        ServletContextHandler context = new ServletContextHandler(jettyServer, "/", ServletContextHandler.SESSIONS);
        context.addServlet(sh, "/*");

        try {
            jettyServer.start();
        } catch (Exception e) {
            LOGGER.error("Failed to start Jetty");
            throw new RuntimeException(e);
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    jettyServer.join();
                } catch (InterruptedException e) {
                    LOGGER.warn("Interrupted, bye bye");
                } finally {
                    jettyServer.destroy();
                }
            }
        }.start();
    }

    public static  boolean isReadtimeout() {
        return isReadtimeout;
    }

    public static void setReadtimeout(boolean isReadtimeout) {
        PrimingServer.isReadtimeout = isReadtimeout;
    }
}
