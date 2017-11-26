
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Objects;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.omg.IOP.Encoding;

import jdk.nashorn.internal.parser.JSONParser;
import sun.misc.IOUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.rmi.runtime.Log;

import static java.lang.System.in;

public class HTTPServer
{

    private static Db database;
    private static HttpServer server;

    public static void start() throws Exception
    {
        database = new Db();

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        NetworkInterface nInterface = interfaces.nextElement();
        Enumeration<InetAddress> adresses = nInterface != null ? nInterface.getInetAddresses() : null;
        InetAddress iAdress = null;
        while (adresses != null && adresses.hasMoreElements())
            iAdress = adresses.nextElement();
        String adress = iAdress != null ? iAdress.getHostAddress() : "";
        server = HttpServer.create(new InetSocketAddress(adress,
                8001), 0);

        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange t) throws IOException
        {
            //sendResponse(200, "erfolgreich", t);
            System.out.println("Got request");
            System.out.println("Body: " + t.getRequestBody() + "; Head: " + t.getRequestHeaders
                ().values());

            BufferedReader reader = new BufferedReader(new InputStreamReader(t.getRequestBody(),
                    "UTF-8"));

            final StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
                builder.append("\n");
            }

            JSONArray body = new JSONArray(builder.toString());


            System.out.println("Initial body: " + body);


            switch (t.getRequestMethod())
            {
                case "POST":
                    System.out.println("Request POST body: " + body.getString(0));
                    String commandType = body.getString(0);
                    body.remove(0);
                    if (Objects.equals(commandType, "writeLines"))
                    {
                        System.out.println("Request POST body 2: " + body.get(0));
                        try
                        {
                            System.out.println("Response: " + database.WriteZeilen(body));
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else if (Objects.equals(commandType, "writeLehrer"))
                    {
                        System.out.println(database.WriteLehrer(body.getJSONObject(0)) ?
                                "Successful" : "Not successful");
                    }
                    else if (Objects.equals(commandType, "writeArt"))
                    {
                        System.out.println(database.WriteLehrer(body.getJSONObject(0)) ?
                                "Successful" : "Not successful");
                    }
                    else if (Objects.equals(commandType, "writeKurs"))
                    {
                        System.out.println(database.WriteKurs(body.getJSONObject(0)) ?
                                "Successful" : "Not successful");
                    }
                    break;
                case "GET":
                    System.out.println("Request GET body: " + body.get(0));
                    String r = database.Read(body.getJSONObject(0)).toString();
                    System.out.println("Request response: " + r);
                    sendResponse(200, r, t);

                    break;
                default:
                    System.out.println("Default");
                    t.sendResponseHeaders(403, 0);
                    break;

            }

            sendResponse(200, "Standard response", t);

        }

        private void sendResponse(int code, String content, HttpExchange t) throws IOException
        {
            byte[] bytes = content.getBytes();
            t.sendResponseHeaders(code, content.length());
            OutputStream os = t.getResponseBody();
            for (int i = 0; i < bytes.length / 10; i++)
                os.write(bytes, 10 * i, 10);
            int finalOff = (bytes.length / 10 - 1) * 10;
            int finalLength = bytes.length % 10 - 1;
            os.write(bytes, finalOff, finalLength);
            os.close();
        }
    }

}

