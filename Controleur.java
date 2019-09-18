import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controleur extends Application {


    public static final int RETOUR_LIGNE = 50;

    private Stage primaryStage;
    private GestionBase gestionBase;

    @FXML
    private Button buttonFermer;

    @FXML
    private TextField zone;

    @FXML
    private VBox titres;



    public void start(Stage primaryStage) throws Exception {


        Parent root = FXMLLoader.load(getClass().getResource("ihm.fxml"));
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Films");
        this.primaryStage.setScene(new Scene(root, 800, 500));
        this.primaryStage.show();
    }

    public void fermer() {
        Stage stage = (Stage) buttonFermer.getScene().getWindow();
        stage.close();
    }

    public void clear() {
        zone.clear();
        titres.getChildren().clear();
    }


    public void generePopUp(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(contenu);
        alert.showAndWait();
    }


    public void chercher() throws SQLException {

        ResultSet nombreComediens;
        ResultSet nomComedien;
        int taille;
        ResultSet resultat;
        ArrayList<String> tab = new ArrayList<String>();



        if (zone.getText().equals("")) {
            generePopUp("Erreur", "Zone de texte vide !");
            return;
        }


        gestionBase = new GestionBase("jdbc:sqlite:cinema.db","org.sqlite.JDBC");
        nombreComediens = gestionBase.chercherTitres("SELECT COUNT(*) FROM comediens where nomcomedien like \""+zone.getText()+"%\";");
        taille = nombreComediens.getInt(1);

        switch(taille)
        {
            case 1:
                resultat = gestionBase.chercherTitres("select titre from films f join com_film c on " +
                        "f.numerofilm=c.numerofilm join comediens co on c.numerocomedien=co.numerocomedien where " +
                        "nomcomedien like \"" + zone.getText() + "%\";");

                while (resultat.next()) {
                    tab.add(resultat.getString(1));
                }
                afficheResume(tab,resultat);
                nomComedien = gestionBase.chercherTitres( "SELECT nomcomedien FROM comediens where nomcomedien like \""+zone.getText()+"%\";");
                zone.setText(nomComedien.getString(1));
                break;

            case 0 :
                generePopUp("Erreur","Aucun nom ne correspond, veuillez réassayer");
                break;

            default:
                generePopUp("Erreur","Plusieurs noms possibles : précisez votre recherche");
                break;

        }

    }

    public void afficheResume (ArrayList<String> tab, ResultSet resultat) throws SQLException {

        String resume;
        Label l;
        Tooltip tooltip;

        titres.getChildren().clear();


        for (int cpt = 0; cpt < tab.size(); cpt++) {
            l = new Label(tab.get(cpt));

            resultat = gestionBase.chercherTitres("SELECT resume FROM films where titre like \"" + tab.get(cpt) + "\";");

            resume = resultat.getString(1);


            tooltip = new Tooltip(resume);

            l.setTooltip(tooltip);
            titres.getChildren().add(l);
        }

    }



   
    }


    public static void main(String[] args) {
        launch(args);
    }
}
