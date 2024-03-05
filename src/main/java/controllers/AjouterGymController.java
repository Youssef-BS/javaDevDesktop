package controllers;

import entities.Gym;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.GymService;
import java.io.File;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterGymController {

    @FXML
    private TextField adresse;

    @FXML
    private TextField nom;

    @FXML
    private TextField photo;

    @FXML
    private Label pathphoto;



    private final GymService gymService = new GymService();

    FileChooser filePhoto = new FileChooser();

    @FXML
    void choisirPhoto(MouseEvent event) {

        File fileP = filePhoto.showOpenDialog(new Stage());
        //PhotoPath.setText(fileP.getPath().replace( "/" , "//")  + fileP.getName());
        pathphoto.setText(fileP.getName());
    }



    @FXML
    void ajouter(ActionEvent event) {
        try {

            for (int i = 0; i < nom.getText().length(); i++) {
                char currentChar = nom.getText().charAt(i);

                // Check if the character is not a letter
                if (!Character.isLetter(currentChar)) {
                    // Display an alert for invalid input
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setContentText("Le champ 'nom' ne doit contenir que des lettres.");
                    errorAlert.showAndWait();

                    // Clear the text field or take appropriate action
                    nom.clear();

                    // Exit the loop as soon as an invalid character is found
                    return;
                }
            }

// If the loop completes without returning, it means all characters are valid
// Continue with the rest of your code for handling the input
            if (nom.getText().isEmpty() || adresse.getText().isEmpty()) {
                Alert successAlert = new Alert(Alert.AlertType.WARNING);
                successAlert.setTitle("Information");
                successAlert.setContentText("Les champs sont Obligatoires");
                successAlert.showAndWait();
            }
            else {
                gymService.ajouter(new Gym(nom.getText(), adresse.getText(), pathphoto.getText()));
                // Success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Information");
                successAlert.setContentText("Gym ajoutée avec succès");
                successAlert.showAndWait();

                // Close current stage
                ((Stage) nom.getScene().getWindow()).close();

                // Open new scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGym.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            }

        } catch (SQLException | IOException e) {
            // Handle exceptions
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }




   /* @FXML
    void naviguer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGym.fxml"));
            Parent root = loader.load();
            prenomTF.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
