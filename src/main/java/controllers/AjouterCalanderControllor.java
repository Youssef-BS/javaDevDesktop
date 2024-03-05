package controllers;

import entities.Calendrier;
import entities.Gym;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import services.CalendrierService;
import services.GymService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AjouterCalanderControllor {
    @FXML
    private TextField type;
    @FXML
    private TextArea desc;
    @FXML
    private DatePicker date;
    @FXML
    private TextField dateEnd;

//    @FXML
//    private TextField  tf ;

    @FXML
    private TextField  idGym ;
    CalendrierService cn = new CalendrierService();
    GymService s = new GymService() ;
    Gym gym = new Gym();

    Gym g ;
    private int gymId;

    private void loadProductDetails()
    {
        try {
            System.out.println("load get 1 gym" + gymId);
            g = s.afficherUnGym(gymId);
            if (g != null)
            {

                idGym.setText(String.valueOf(gymId));
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
    public void setGymId(int gymId)
    {
        this.gymId = gymId;
        loadProductDetails();
    }
    @FXML
    public void ajouterCalander() {
        try {
            String typeActivite = type.getText();
            String description = desc.getText();
            LocalDate startDate = date.getValue();
            String dateFermuture = dateEnd.getText();
            int id = Integer.parseInt(String.valueOf(Integer.parseInt(idGym.getText())));
            Calendrier newDate = new Calendrier(id ,typeActivite, description, startDate, dateFermuture );
            cn.ajouter(newDate);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Calendar.fxml"));
                Parent root = fxmlLoader.load();
                date.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
