package io.loli.sc.api;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.grizzly.threadpool.GrizzlyExecutorService;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;

public class GrizzlyServer {
    // TCP port
    public int port;
    HttpServer httpServer;

    private boolean stop = false;
    private String paramToGet = null;
    private String code = null;

    public GrizzlyServer(int port) {
        this.port = port;
    }

    public GrizzlyServer(int port, String paramToGet) {
        this(port);
        this.paramToGet = paramToGet;
    }

    public void run() {
        httpServer = new HttpServer();
        NetworkListener networkListener = new NetworkListener(
                "sample-listener", "127.0.0.1", port);
        ThreadPoolConfig threadPoolConfig = ThreadPoolConfig.defaultConfig()
                .setCorePoolSize(1).setMaxPoolSize(1);
        networkListener.getTransport().setWorkerThreadPoolConfig(
                threadPoolConfig);
        httpServer.addListener(networkListener);
        MyHttpHandler httpHandler = new MyHttpHandler();
        httpServer.getServerConfiguration().addHttpHandler(httpHandler,
                new String[] { "/gDriveAuth" });
        try {
            httpServer.start();
            while (!stop) {
                System.out.print("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop();
        }
    }

    public String getCode() {
        while (code == null) {
        }
        return code;
    }

    public void stop() {
        httpServer.shutdownNow();
    }

    public static void main(String[] args) throws IOException {
        GrizzlyServer s = new GrizzlyServer(18431, "code");
        s.run();
        System.out.println(s.getCode());
    }

    class MyHttpHandler extends HttpHandler {

        final ExecutorService complexAppExecutorService = GrizzlyExecutorService
                .createInstance(ThreadPoolConfig.defaultConfig().copy()
                        .setCorePoolSize(5).setMaxPoolSize(5));

        public void service(final Request request, final Response response)
                throws Exception {
            code = request.getParameter(paramToGet == null ? "code"
                    : paramToGet);
            response.suspend();
            try {
                response.setContentType("text/html");
                response.getWriter()
                        .write("<script>setTimeout(function(){window.opener=null;window.open(\"\",\"_self\");window.close();}, 3000);</script>");
            } catch (Exception e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
            } finally {
                stop = true;
                response.resume();
            }
        }
    }
}
