
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;

public class HTTPServer
{

    private static Db database;

    public static void start() throws Exception
    {
        database = new Db();
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange t) throws IOException
        {
            switch (t.getRequestMethod()){
                case "POST":
                    JSONArray body = new JSONArray(t.getRequestBody());
                        if(body.get(0).toString() == "writeLines"){
                            database.WriteZeilen(body);
                        }
                    break;
                case "GET":
                    break;
                default:
                    break;

            }
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}

