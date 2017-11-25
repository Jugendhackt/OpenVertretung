import com.sun.corba.se.spi.copyobject.CopyobjectDefaults;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Db
{
    private Connection connect = null;


    public Db()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/Vertretungsplan");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String WriteZeile(JSONArray jsonObjects) throws ParseException
    {
        for (Object jsonObjectO : jsonObjects.getJSONArray(0).toList())
        {
            JSONObject jsonObject = (JSONObject) jsonObjectO;

            String lehrer = jsonObject.getString("Lehrer");
            String fach = jsonObject.getString("Fach");
            String art = jsonObject.getString("Art");
            String kurs = jsonObject.getString("Kurs");
            int vertretungsplan = jsonObject.getInt("Vertretungsplan");
            int stundeVon = jsonObject.getInt("StundeVon");
            int stundeBis = jsonObject.getInt("StundeBis");
            String kommentar = jsonObject.getString("Kommentar");
            String raum = jsonObject.getString("Raum");
            String datum = jsonObject.getString("Datum");
            Date date = new java.sql.Date(new SimpleDateFormat("dd MMM yyyy").parse("01 " +
                    "NOVEMBER 2012").getTime());

            int lehrerId = GetIdToLehrer(lehrer, vertretungsplan);
            int artId = GetIdToArt(art);
            int kursId = GetIdToKurs(kurs, vertretungsplan);
            if (lehrerId == -1)
                return "Invalid teacher";
            if (artId == -1)
                return "Invalid type";
            if (kursId == -1)
                return "Invalid course";

            ResultSet resultSet = null;
            try
            {
                PreparedStatement statement = connect.prepareStatement("INSERT INTO Zeile " +
                        "(Lehrer, Fach, Art, Kurs, Vertretungsplan, StundeVon, StundeBis, " +
                        "Kommentar, Raum, Datum) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?); " +
                        "SELECT LAST_INSERT_ID();");
                statement.setInt(1, lehrerId);
                statement.setString(2, fach);
                statement.setInt(3, artId);
                statement.setInt(4, kursId);
                statement.setInt(5, vertretungsplan);
                statement.setInt(6, stundeVon);
                statement.setInt(7, stundeBis);
                statement.setString(8, kommentar);
                statement.setString(9, raum);
                statement.setDate(10, date);
            } catch (SQLException e)
            {
                e.printStackTrace();

                return "Unknown error";
            } finally
            {
                if (resultSet != null)
                {
                    try
                    {
                        resultSet.close();
                    } catch (SQLException e)
                    {
                        e.printStackTrace();

                        return "Unknown error";
                    }
                }
            }
        }
        return "Successful";
    }


    private int GetIdToKurs (String kurs, int vertretungsplan)
    {
        ResultSet resultSet = null;

        try
        {
            PreparedStatement statement = connect.prepareStatement("SELECT idKurs FROM Kurs WHERE " +
                    "Name = ? AND Vertretungsplan = ?");
            statement.setString(1, kurs);
            statement.setInt(2, vertretungsplan);
            resultSet = statement.executeQuery();
            return resultSet.getInt("idKurs");
        } catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
        finally
        {
            if (resultSet != null)
            {
                try
                {
                    resultSet.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private int GetIdToArt (String art)
    {
        ResultSet resultSet = null;

        try
        {
            PreparedStatement statement = connect.prepareStatement("SELECT idVertretungsart FROM " +
                    "Vertretungsart WHERE \"Name\" = ?");
            statement.setString(1, art);
            resultSet = statement.executeQuery();
            return resultSet.getInt("idVertretungsArt");
        } catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
        finally
        {
            if (resultSet != null)
            {
                try
                {
                    resultSet.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private int GetIdToLehrer (String lehrerName, int vertretungsplan)
    {
        ResultSet resultSet = null;

        try
        {
            PreparedStatement statement = connect.prepareStatement("SELECT idLehrer FROM Lehrer " +
                    "WHERE Vertretungsplan = ? AND \"Name\" = ?;");
            statement.setInt(1, vertretungsplan);
            statement.setString(2, lehrerName);
            resultSet = statement.executeQuery();
            return resultSet.getInt("idLehrer");
        } catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
        finally
        {
            if (resultSet != null)
            {
                try
                {
                    resultSet.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void close()
    {
        try
        {
            if (connect != null)
            {
                connect.close();
            }
        } catch (Exception e)
        {

        }
    }
}