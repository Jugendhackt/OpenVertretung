public class Main
{


    private static HTTPServer server;

    public static void main(String[] args)
    {
        Db db = new Db();
        db.Test();
        System.out.println("Hello World!");
        server = new HTTPServer();
        try
        {
            server.start();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
