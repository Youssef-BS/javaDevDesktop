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
import services.ServiceReclamation;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;



import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;


public class afficherController {

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private DatePicker dateTF;

    @FXML
    private TableColumn<?, ?> descriptionCol;

    @FXML
    private TextField descriptionTF;

    @FXML
    private TableColumn<?, ?> idReclamationCol;

    @FXML
    private ComboBox<Integer> idUserComboBox;

    @FXML
    private TableColumn<?, ?> idUsercol;

    @FXML
    private TableView<reclamation> tableView;

    @FXML
    private TableColumn<?, ?> titreCol;

    @FXML
    private TextField titreTF;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private ComboBox<String> typeTF;

    @FXML
    private DatePicker daterepTF;
    @FXML
    private TextField idrecrepTF;

    @FXML
    private TextField iduserrepTF;
    @FXML
    private TextField repTF;

    @FXML
    private TextField titrerepTF;




    @FXML
    void modifier(ActionEvent event) {
        // Étape 1 : Obtenir la réclamation sélectionnée dans le TableView
        reclamation selectedReclamation = tableView.getSelectionModel().getSelectedItem();

        if (selectedReclamation == null) {
            showAlert("Erreur de Modification", "Aucune réclamation sélectionnée pour la modification.");
            return;
        }

        // Étape 2 : Mettre à jour l'objet selectedReclamation avec les nouvelles valeurs
        selectedReclamation.setDescription(descriptionTF.getText());
        selectedReclamation.setTitre(titreTF.getText());
        selectedReclamation.setType(typeTF.getValue());
        selectedReclamation.setDate_reclamation(dateTF.getValue()); // Assurez-vous que votre entité reclamation a un setter pour date_reclamation

        // Étape 3 : Appeler la méthode du service pour mettre à jour la base de données
        try {
            ReclamationService.update(selectedReclamation);

            // Étape 4 : Rafraîchir la TableView pour afficher les données mises à jour
            refreshTableView(); // Il est préférable d'appeler une méthode qui recharge les données de la base de données et met à jour le TableView

            showAlert("Succès", "Réclamation modifiée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur de Base de Données", "Échec de la modification de la réclamation : " + e.getMessage());
        }
    }




    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    @FXML
    void supprimer(ActionEvent event) {

        reclamation selectedReclamation = tableView.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            try {
                ReclamationService.delete(selectedReclamation);
                tableView.getItems().remove(selectedReclamation);

                // Afficher un message de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suppression");
                alert.setHeaderText(null);
                alert.setContentText("La réclamation a été supprimée avec succès.");
                alert.showAndWait();

            } catch (SQLException e) {
                // Afficher une alerte d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur de Suppression");
                alert.setContentText("Impossible de supprimer la réclamation: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Afficher un message d'erreur si aucune réclamation n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune Sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une réclamation à supprimer.");
            alert.showAndWait();
        }

    }


    private final ServiceReclamation ReclamationService = new ServiceReclamation();
    @FXML
    public void initialize() {
        // Set up the TableView columns
        idReclamationCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date_reclamation"));
        idUsercol.setCellValueFactory(new PropertyValueFactory<>("iduser"));

        // Initialize the ComboBox with the types of reclamations
        typeTF.getItems().addAll(
                "Reclamation sur service",
                "Reclamation sur security",
                "Reclamation sur produit",
                "Reclamation sur livreur",
                "Reclamation sur colis"
        );
        typeTF.getSelectionModel().selectFirst();

        // Load the reclamations into the TableView
        loadReclamations();

        // Add a listener to the TableView's selected item property
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                reclamation selectedReclamation = tableView.getSelectionModel().getSelectedItem();

                // Set TextField values based on the selected reclamation
                titreTF.setText(selectedReclamation.getTitre());
                descriptionTF.setText(selectedReclamation.getDescription());
                dateTF.setValue(selectedReclamation.getDate_reclamation());
                typeTF.getSelectionModel().select(selectedReclamation.getType());
                idUserComboBox.getSelectionModel().select(Integer.valueOf(selectedReclamation.getIduser()));

            }
        });

        // Initialize user IDs ComboBox
        initializeIdUserComboBox();
    }


    private void initializeIdUserComboBox() {
        try {
            List<Integer> userIds = ReclamationService.fetchUserIds(); // Ensure this method correctly fetches user IDs
            if (!userIds.isEmpty()) {
                ObservableList<Integer> observableUserIds = FXCollections.observableArrayList(userIds);
                idUserComboBox.setItems(observableUserIds);
                System.out.println("ComboBox Items Loaded: " + userIds.size());
            } else {
                System.out.println("No User IDs found to load into ComboBox.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading User IDs into ComboBox: " + e.getMessage());
        }
    }


    private void loadReclamations() {
        try {
            ObservableList<reclamation> reclamations = FXCollections.observableArrayList(ReclamationService.showReclamation());
            tableView.setItems(reclamations); // Set the items for the TableView
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshTableView() {
        loadReclamations(); // Cette méthode recharge les données depuis la base de données
        tableView.refresh(); // Ceci force le TableView à se rafraîchir avec les nouvelles données
    }




    @FXML
    void back(ActionEvent event) {


        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AjouterReclamation.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Get the current stage from the event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setTitle("GymSphere!");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            System.err.println("IOExcepstion: Error loading listrep.fxml");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: An unknown error occurred");
            e.printStackTrace();
        }
    }

    }


