import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;

public class GestionBase {
    private Connection conn ;

    public GestionBase (String base, String driver)
    {

        try {
            Class.forName(driver);
            conn= DriverManager.getConnection(base);
        } catch (ClassNotFoundException | SQLException ex)
        {
            System.err.println ("Erreur: " + ex.getMessage()) ;
            System.exit (1) ;
        }

    }

    public ResultSet chercherTitres (String requete)
    {

        ArrayList<String> ret = new ArrayList<String>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultat = statement.executeQuery( requete);



            return resultat;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
