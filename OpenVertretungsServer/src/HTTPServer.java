
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class HTTPServer
{

    private static Db database;

    public static void start() throws Exception {
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
                    body.remove(0);
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
                        sendResponse(200, r, t);

                    break;
                default:
                    t.sendResponseHeaders(403, 0);
                    break;

            }
            
            sendResponse(200, "Standard response", t);

        }

        private void sendResponse(int code, String content, HttpExchange t)throws IOException{
            t.sendResponseHeaders(code, content.length());
            OutputStream os = t.getResponseBody();
            os.write(content.getBytes());
            os.close();
        }
    }

}

