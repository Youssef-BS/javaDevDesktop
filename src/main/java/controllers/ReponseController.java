package controllers;

import entities.reclamation;
import entities.repondre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.ServiceRepondre;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReponseController {

    @FXML
    private TableColumn<?, ?> daterepcol;

    @FXML
    private TableColumn<?, ?> idreclacol;

    @FXML
    private TableColumn<?, ?> idrepcol;

    @FXML
    private TableColumn<?, ?> idusercol;

    @FXML
    private TableColumn<?, ?> reponsecol;

    @FXML
    private TableView<repondre> tablerep;

    @FXML
    private TableColumn<?, ?> titrecol;


    @FXML
    private DatePicker moddateTF;

    @FXML
    private TextField modidrecTF;

    @FXML
    private TextField modiduserTF;

    @FXML
    private TextField modrepTF;

    @FXML
    private TextField modtitreTF;


    @FXML
    public void initialize() {
        // Initialize table columns
        idrepcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idreclacol.setCellValueFactory(new PropertyValueFactory<>("idreclamation"));
        idusercol.setCellValueFactory(new PropertyValueFactory<>("iduser"));
        titrecol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        reponsecol.setCellValueFactory(new PropertyValueFactory<>("reponse"));
        daterepcol.setCellValueFactory(new PropertyValueFactory<>("date_reponse"));

        // Initialize selection listener
        tablerep.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                modtitreTF.setText(newSelection.getTitre());
                modrepTF.setText(newSelection.getReponse());
                modidrecTF.setText(String.valueOf(newSelection.getIdreclamation()));
                modiduserTF.setText(String.valueOf(newSelection.getIduser()));
                moddateTF.setValue(newSelection.getDate_reponse());
            }
        });

        loadReponses();
    }
    private void loadReponses() {
        try {
            ServiceRepondre serviceRepondre = new ServiceRepondre();
            List<repondre> reponses = serviceRepondre.showReponse();
            tablerep.setItems(FXCollections.observableArrayList(reponses));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void refreshTableView() {
        loadReponses(); // Reload the data
        tablerep.refresh(); // Refresh the TableView
    }


    @FXML
    void supprep(ActionEvent event) {

            repondre selectedResponse = tablerep.getSelectionModel().getSelectedItem();
            if (selectedResponse != null) {
                try {
                    ServiceRepondre ServiceRepondre = new ServiceRepondre();
                    ServiceRepondre.delete(selectedResponse); // Assuming ServiceRepondre.delete() is your method to delete a response
                    tablerep.getItems().remove(selectedResponse);

                    // Display a confirmation message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Suppression");
                    alert.setHeaderText(null);
                    alert.setContentText("La réponse a été supprimée avec succès.");
                    alert.showAndWait();

                } catch (SQLException e) {
                    // Display an error alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur de Suppression");
                    alert.setContentText("Impossible de supprimer la réponse: " + e.getMessage());
                    alert.showAndWait();
                }
            } else {
                // Display an error message if no response is selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune Sélection");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une réponse à supprimer.");
                alert.showAndWait();
            }
        }
    @FXML
    void back(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AjouterReclamation.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setTitle("SphereGym!");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            System.err.println("IOException: Error loading listrep.fxml");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: An unknown error occurred");
            e.printStackTrace();
        }

    }

    @FXML
    void modifierreponse(ActionEvent event)  {
        repondre selectedResponse = tablerep.getSelectionModel().getSelectedItem();
        if (selectedResponse == null) {
            showAlert("Modification Error", "No response selected for modification.");
            return;
        }
        selectedResponse.setTitre(modtitreTF.getText());
        selectedResponse.setReponse(modrepTF.getText());
        try {
            int idReclamation = Integer.parseInt(modidrecTF.getText());
            int idUser = Integer.parseInt(modiduserTF.getText());
            selectedResponse.setIdreclamation(idReclamation);
            selectedResponse.setIduser(idUser);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "ID Reclamation and ID User must be valid integers.");
            return;
        }
        LocalDate modDate = moddateTF.getValue();
        if (modDate == null) {
            showAlert("Date Error", "The date cannot be empty.");
            return;
        }
        selectedResponse.setDate_reponse(modDate);
        try {
            ServiceRepondre ServiceRepondre = new ServiceRepondre();

            ServiceRepondre.update(selectedResponse);
            tablerep.refresh();

            showAlert("Success", "Response modified successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to modify the response: " + e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

}
