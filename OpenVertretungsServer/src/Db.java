import org.json.JSONObject;

import java.sql.*;

public class Db
{
    public String WriteZeile(JSONObject[] jsonObjects)
    {
        for (JSONObject jsonObject : jsonObjects)
        {
            String lehrer = jsonObject.getString("Lehrer");
            String fach = jsonObject.getString("Fach");
            String art = jsonObject.getString("Art");
            String kurs = jsonObject.getString("Kurs");
            String vertretungsplan = jsonObject.getString("Vertretungsplan");
            int stundeVon = jsonObject.getInt("StundeVon");
            int stundeBis = jsonObject.getInt("StundeBis");
            String kommentar = jsonObject.getString("Kommentar");
            String raum = jsonObject.getString("Raum");
            String date = jsonObject.getString("Datum");
        }
    }
}