import com.sun.corba.se.spi.copyobject.CopyobjectDefaults;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

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

    public JSONArray Read(JSONObject jsonObject)
    {
        int vertretungsplan = jsonObject.getInt("Vertretungsplan");
        String benutzername = jsonObject.getString("Benutzername");
        String passwort = jsonObject.getString("Passwort");

        String zugangsart = GetZugangsart(benutzername, passwort);
        if (Objects.equals(zugangsart, "schule") || Objects.equals(zugangsart, "admin") ||
                Objects.equals(zugangsart, "schüler"))
        {
            JSONArray finalArray = new JSONArray();

            ResultSet resultSet = null;
            PreparedStatement statement = null;

            try
            {
                statement = connect.prepareStatement("SELECT Lehrer, Fach, Art, Kurs, " +
                        "StundeVon, StundeBis, Kommentar, Raum, Datum " +
                        "FROM Zeile " +
                        "WHERE Vertretungsplan = ?");
                statement.setInt(1, vertretungsplan);
                resultSet = statement.executeQuery();
                while (resultSet.next())
                {
                    int lehrerId = resultSet.getInt("Lehrer");
                    String fach = resultSet.getString("Fach");
                    int artId = resultSet.getInt("Art");
                    int kursId = resultSet.getInt("Kurs");
                    int stundeVon = resultSet.getInt("StundeVon");
                    int stundeBis = resultSet.getInt("StundeBis");
                    String kommentar = resultSet.getString("Kommentar");
                    String raum = resultSet.getString("Raum");
                    Date datum = resultSet.getDate("Datum");

                    String lehrer = GetLehrerToId(lehrerId);
                    String art = GetArtToId(artId);
                    String kurs = GetKursToId(kursId);

                    JSONObject jsonObjectToAdd = new JSONObject();
                    jsonObject.put("Lehrer", lehrer);
                    jsonObject.put("Fach", fach);
                    jsonObject.put("Art", art);
                    jsonObject.put("Kurs", kurs);
                    jsonObject.put("StundeVon", stundeVon);
                    jsonObject.put("StundeBis", stundeBis);
                    jsonObject.put("Kommentar", kommentar);
                    jsonObject.put("Raum", raum);
                    jsonObject.put("Datum", datum);

                    finalArray.put(jsonObjectToAdd);
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
                return null;
            } finally
            {
                closeFinally(resultSet, statement);
            }
            return finalArray;
        }
        else
            return null;
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
                if(!statement.execute())
                    return "Unknown error";
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

    private String GetLehrerToId (int id)
    {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try
        {
            statement = connect.prepareStatement("SELECT Name FROM Lehrer WHERE idLehrer = ?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString("Name");
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            closeFinally(resultSet, statement);
        }
    }

    private String GetArtToId (int id)
    {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try
        {
            statement = connect.prepareStatement("SELECT Name FROM Vertretungsart WHERE " +
                    "idVertretungsart = ?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString("Name");
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            closeFinally(resultSet, statement);
        }
    }

    private String GetKursToId (int id)
    {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try
        {
            statement = connect.prepareStatement("SELECT Name FROM Kurs WHERE idKurs = ?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString("Name");
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            closeFinally(resultSet, statement);
        }
    }

    private String GetZugangsart(String benutzername, String passwort)
    {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try
        {
            statement = connect.prepareStatement("SELECT Zugangsart.Name FROM Zugang INNER JOIN " +
                    "Zugangsart ON Zugang.Zugangsart = Zugangsart.idZugangsart " +
                    "WHERE Benutzername = ? AND Kennwort = ?");
            statement.setString(1, benutzername);
            statement.setString(2, passwort);
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString("Zugangsart");
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            closeFinally(resultSet, statement);
        }
    }

    private int GetIdToKurs(String kurs, int vertretungsplan)
    {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try
        {
            statement = connect.prepareStatement("SELECT idKurs FROM Kurs WHERE " +
                    "Name = ? AND Vertretungsplan = ?");
            statement.setString(1, kurs);
            statement.setInt(2, vertretungsplan);
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("idKurs");
        } catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        } finally
        {
            closeFinally(resultSet, statement);
        }
    }

    private int GetIdToArt(String art)
    {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try
        {
            statement = connect.prepareStatement("SELECT idVertretungsart FROM " +
                    "Vertretungsart WHERE \"Name\" = ?");
            statement.setString(1, art);
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("idVertretungsArt");
        } catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        } finally
        {
            closeFinally(resultSet, statement);
        }
    }

    private int GetIdToLehrer(String lehrerName, int vertretungsplan)
    {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try
        {
            statement = connect.prepareStatement("SELECT idLehrer FROM Lehrer " +
                    "WHERE Vertretungsplan = ? AND \"Name\" = ?;");
            statement.setInt(1, vertretungsplan);
            statement.setString(2, lehrerName);
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("idLehrer");
        } catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        } finally
        {
            closeFinally(resultSet, statement);
        }
    }

    private void closeFinally (ResultSet resultSet, PreparedStatement statement)
    {
        try
        {
            if (resultSet != null)
                resultSet.close();
            if (statement != null)
                statement.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
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