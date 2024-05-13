package controllers;

import entities.Gym;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.GymService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficherUnGymController implements Initializable {
    @FXML
    private TextField adresse;

    @FXML
    private TextField nom;

    @FXML
    private TextField photo;

    @FXML
    private Label pathphoto;
    @FXML
    private Label nomLabel;

    @FXML
    private Label adresseLabel;

    private int gymId;


    @FXML
    private ImageView phtotgym;


    Gym gym = new Gym();
    private final GymService gymService = new GymService();
Gym p;
    FileChooser filePhoto = new FileChooser();

    @FXML
    void choisirPhoto(MouseEvent event) {

        File fileP = filePhoto.showOpenDialog(new Stage());

        pathphoto.setText(fileP.getName());
    }


    public void setGymId(int gymId)
    {
        this.gymId = gymId;
        loadProductDetails();
   }

    private void loadProductDetails()
    {
        try {
            System.out.println("load get 1 gym" + gymId);
            p = gymService.afficherUnGym(gymId);
            if (p != null)
            {
                Image imageP = new Image("file:///C://Users//Houssem//Desktop//GymProject - copy//GymProject//PIDEV3A9//image//" + p.getPhotoGym());
                phtotgym.setImage(imageP);
                nom.setText(p.getNomGym());
                adresse.setText(p.getAdresse());
            }

        } catch (SQLException e) {
            // Gérer les exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des factures");
            alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }
    public void afficherGym(int idGym) {
        try {
            gym = gymService.afficherUnGym(idGym);
            if (gym != null) {
                nom.setText(gym.getNomGym());

//                adresse.setText(gym.getAdresse());
                pathphoto.setText(gym.getPhotoGym());
            } else {
                // Handle the case when the gym is not found
            }
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
        }
    }
    @FXML
    void  quitter(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGym.fxml"));
            Parent root = loader.load();
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void modifier () {
        try {
            String Nom = nom.getText();
            String Adrs = adresse.getText();
            Gym g = new Gym(gymId , Nom ,Adrs);
            gymService.modifier(g);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("updated");
            System.out.println(gymId);
            System.out.println(Nom);
            System.out.println(Adrs);
            a.show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
     void supprimer()  {
//     gymService.supprimer(gymId);
        try{
            gymService.supprimer(gymId);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    public void loadFXML(String s,int productId){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ajouterCalander.fxml"));
            Parent root = fxmlLoader.load();
            AjouterCalanderControllor controller = fxmlLoader.getController();
            controller.setGymId(productId);
            adresse.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchP(){
        final int GymId = Gym.getIdGym();
        loadFXML("/ajouterCalander.fxml", GymId);
        System.out.println("hos****" + GymId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
