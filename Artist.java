import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;

/* Each table you wish to access in your database requires a model class, like this example: */
public class Artist
{
    /* First, map each of the fields (columns) in your table to some public variables. */
    public int artistID;
    public String artistName;
    public int genreId;

    /* Next, prepare a constructor that takes each of the fields as arguements. */
    public Artist(int artistID, String artistName, int genreId)
    {
        this.artistID = artistID;        
        this.artistName = artistName;
        this.genreId = genreId;
    }

    /* A toString method is vital so that your model items can be sensibly displayed as text. */
    @Override public String toString()
    {
        return artistName;
    }

    /* Different models will require different read and write methods. Here is an example 'loadAll' method 
     * which is passed the target list object to populate. */
    public static void readAll(List<Artist> list)
    {
        list.clear();       // Clear the target list first.

        /* Create a new prepared statement object with the desired SQL query. */
        PreparedStatement statement = Application.database.newStatement("SELECT artistID, artistName, genreId FROM artists ORDER BY artistID"); 

        if (statement != null)      // Assuming the statement correctly initated...
        {
            ResultSet results = Application.database.runQuery(statement);       // ...run the query!

            if (results != null)        // If some results are returned from the query...
            {
                try {                               // ...add each one to the list.
                    while (results.next()) {                                               
                        list.add( new Artist(results.getInt("artistID"), results.getString("artistName"), results.getInt("genreId")));
                    }
                }
                catch (SQLException resultsexception)       // Catch any error processing the results.
                {
                    System.out.println("Database result processing error: " + resultsexception.getMessage());
                }
            }
        }

    }

    public static Artist getById(int artistID)
    {
        Artist artist = null;

        PreparedStatement statement = Application.database.newStatement("SELECT artistID, artistName, genreId FROM artists WHERE artistID = ?"); 

        try 
        {
            if (statement != null)
            {
                statement.setInt(1, artistID);
                ResultSet results = Application.database.runQuery(statement);

                if (results != null)
                {
                    artist = new Artist(results.getInt("artistID"), results.getString("artistName"), results.getInt("genreId"));
                }
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }

        return artist;
    }

    public static void deleteById(int artistID)
    {
        try 
        {

            PreparedStatement statement = Application.database.newStatement("DELETE FROM artists WHERE artistID = ?");             
            statement.setInt(1, artistID);

            if (statement != null)
            {
                Application.database.executeUpdate(statement);
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }

    }
    
    public void save()    
    {
        PreparedStatement statement;

        try 
        {

            if (artistID == 0)
            {

                statement = Application.database.newStatement("SELECT artistID FROM artists ORDER BY artistID DESC");             

                if (statement != null)	
                {
                    ResultSet results = Application.database.runQuery(statement);
                    if (results != null)
                    {
                        artistID = results.getInt("artistID") + 1;
                    }
                }

                statement = Application.database.newStatement("INSERT INTO artists (artistID, artistName, genreId) VALUES (?, ?, ?)");             
                statement.setInt(1, artistID);
                statement.setString(2, artistName);
                statement.setInt(3, genreId);         

            }
            else
            {
                statement = Application.database.newStatement("UPDATE artists SET artistName = ?, genreId = ? WHERE artistID = ?");             
                statement.setString(1, artistName);
                statement.setInt(2, genreId);   
                statement.setInt(3, artistID);
            }

            if (statement != null)
            {
                Application.database.executeUpdate(statement);
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }

    }

}
