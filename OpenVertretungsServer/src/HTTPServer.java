
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONObject;

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
            JSONArray body = new JSONArray(t.getRequestBody());
            switch (t.getRequestMethod()){
                case "POST":
                        if(body.get(0).toString() == "writeLines") {
                            try {
                                database.WriteZeile(body);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    break;
                case "GET":
                        String r = database.Read((JSONObject) body.get(0)).toString();
                        t.sendResponseHeaders(200, r.length());
                        OutputStream os = t.getResponseBody();
                        os.write(r.getBytes());
                        os.close();
                    break;
                default:
                    t.sendResponseHeaders(403, 0);
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

