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


    public boolean Test()
    {
        int id = WriteVertretungsplan("RBSVertretung", "Ulm", "RBS");
        WriteLehrer("Holzer", id);
        WriteArt("Entfall");
        WriteKurs("TGI12/2", "12", "Informatik", id);
        int zugangsArt = WriteZugangsart("Admin");
        WriteZugang("Timo", "4242", zugangsArt);

        JSONObject object = new JSONObject();
        object.put("Lehrer", "Holzer");
        object.put("Fach", "Informatik");
        object.put("Art", "Entfall");
        object.put("Kurs", "TGI12/2");
        object.put("Vertretungsplan", id);
        object.put("StundeVon", 1);
        object.put("StundeBis", 5);
        object.put("Kommentar", "Geil alder");
        object.put("Raum", "B7-1.09");
        object.put("Datum", "01 01 2012");

        JSONArray array = new JSONArray();
        array.put(object);
        String message = WriteZeilen(array);

        JSONObject readObject = new JSONObject();
        readObject.put("Vertretungsplan", id);
        readObject.put("Benutzername", "Timo");
        readObject.put("Passwort", "4242");
        JSONArray read = Read(readObject);
        return object.equals(read);
    }

    public Db()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/Vertretungsplan", "root",
                            "42424242");
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
        if (zugangsart == null)
            return null;
        zugangsart = zugangsart.toLowerCase();
        if (!Objects.equals(zugangsart, "schule") && !Objects.equals
                (zugangsart, "admin") && !Objects.equals(zugangsart, "schüler"))
            return null;
        else
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
                    jsonObjectToAdd.put("Lehrer", lehrer);
                    jsonObjectToAdd.put("Fach", fach);
                    jsonObjectToAdd.put("Art", art);
                    jsonObjectToAdd.put("Kurs", kurs);
                    jsonObjectToAdd.put("StundeVon", stundeVon);
                    jsonObjectToAdd.put("StundeBis", stundeBis);
                    jsonObjectToAdd.put("Kommentar", kommentar);
                    jsonObjectToAdd.put("Raum", raum);
                    jsonObjectToAdd.put("Datum", datum);

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
    }

    public String WriteZeilen(JSONArray jsonObjects)
    {
        for (int i = 0; i < jsonObjects.length(); i++)
        {
            JSONObject jsonObject = jsonObjects.getJSONObject(i);

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
            Date date = null;
            try
            {
                date = new Date(new SimpleDateFormat("dd MM yyyy").parse(/*"01 " +
                        "NOVEMBER 2012"*/datum).getTime());
            } catch (ParseException e)
            {
                return "Parse error";
            }

            int lehrerId = GetIdToLehrer(lehrer, vertretungsplan);
            int artId = GetIdToArt(art);
            int kursId = GetIdToKurs(kurs, vertretungsplan);
            if (lehrerId == -1)
                return "Invalid teacher";
            if (artId == -1)
                return "Invalid type";
            if (kursId == -1)
                return "Invalid course";

            PreparedStatement statement = null;
            try
            {
                statement = connect.prepareStatement("INSERT INTO Zeile " +
                        "(Lehrer, Fach, Art, Kurs, Vertretungsplan, StundeVon, StundeBis, " +
                        "Kommentar, Raum, Datum) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                        "; ");
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
                statement.execute();
            } catch (SQLException e)
            {
                e.printStackTrace();

                return "Unknown error";
            } finally
            {
                if (statement != null)
                {
                    try
                    {
                        statement.close();
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

    private boolean WriteZugang (String benutzername, String kennwort, int zugangsart)
    {
        PreparedStatement statement = null;

        try
        {
            statement = connect.prepareStatement("INSERT INTO Zugang (Benutzername, Kennwort, " +
                    "Zugangsart) " +
                    "VALUES (?, ?, ?)");
            statement.setString(1, benutzername);
            statement.setString(2, kennwort);
            statement.setInt(3, zugangsart);
            return statement.execute();
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        } finally
        {
            closeFinally(null, statement);
        }
    }

    private int WriteZugangsart (String name)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            statement = connect.prepareStatement("INSERT INTO Zugangsart (Name) VALUES (?);");
            statement.setString(1, name);
            statement.execute();
            statement = connect.prepareStatement("SELECT LAST_INSERT_ID() AS LastInserted;");
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("LastInserted");

        } catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        } finally
        {
            closeFinally(resultSet, statement);
        }
    }

    private boolean WriteLehrer (String name, int vertretungsplan)
    {
        PreparedStatement statement = null;

        try
        {
            statement = connect.prepareStatement("INSERT INTO Lehrer (Name, Vertretungsplan) " +
                    "VALUES (?, ?)");
            statement.setString(1, name);
            statement.setInt(2, vertretungsplan);
            return statement.execute();
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        } finally
        {
            closeFinally(null, statement);
        }
    }

    private boolean WriteArt(String name)
    {
        PreparedStatement statement = null;

        try
        {
            statement = connect.prepareStatement("INSERT INTO Vertretungsart (Name) VALUES (?)");
            statement.setString(1, name);
            return statement.execute();
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        } finally
        {
            closeFinally(null, statement);
        }
    }

    private boolean WriteKurs (String name, String stufe, String fach, int vertretungsPlan)
    {
        PreparedStatement statement = null;

        try
        {
            statement = connect.prepareStatement("INSERT INTO Kurs (Name, Stufe, Fach, Vertretungsplan) " +
                    "VALUES (?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, stufe);
            statement.setString(3, fach);
            statement.setInt(4, vertretungsPlan);
            return statement.execute();
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        } finally
        {
            closeFinally(null, statement);
        }
    }

    private int WriteVertretungsplan (String name, String ort, String schulname)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            statement = connect.prepareStatement("INSERT INTO Vertretungsplan (Name, Ort, Schulname) " +
                    "VALUES (?, ?, ?);");
            statement.setString(1, name);
            statement.setString(2, ort);
            statement.setString(3, schulname);
            statement.execute();
            statement = connect.prepareStatement("SELECT LAST_INSERT_ID() AS LastInserted");
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("LastInserted");
        } catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        } finally
        {
            closeFinally(resultSet, statement);
        }
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
            statement = connect.prepareStatement("SELECT Zugangsart.Name Name FROM Zugang INNER " +
                    "JOIN " +
                    "Zugangsart ON Zugang.Zugangsart = Zugangsart.idZugangsart " +
                    "WHERE Benutzername = ? AND Kennwort = ?");
            statement.setString(1, benutzername);
            statement.setString(2, passwort);
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
                    "Vertretungsart WHERE Name = ?");
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
                    "WHERE Vertretungsplan = ? AND Name = ?;");
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