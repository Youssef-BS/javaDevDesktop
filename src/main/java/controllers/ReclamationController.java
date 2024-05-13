package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import entities.reclamation;
import entities.repondre;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceReclamation;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import services.ServiceRepondre;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.DatePicker;
import test.MainFX;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.ComboBox;
import utils.MyDatabase;



import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;



import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;




public class ReclamationController {


    @FXML
    private TableColumn<reclamation, Integer> idReclamationCol;
    @FXML
    private TableColumn<reclamation, String> descriptionCol;
    @FXML
    private TableColumn<reclamation, Integer> idUsercol;
    @FXML
    private TableView<reclamation> tableView;

    @FXML
    private TableColumn<?, ?> titreCol;


    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private Pane reponse ;



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
    private Button generateQRCodeButton;



    @FXML
    private TextField searchBar;

    @FXML
    private Button generatePDFButton;







    @FXML
    public void initialize() {
        // Setup columns
        idReclamationCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date_reclamation"));
        idUsercol.setCellValueFactory(new PropertyValueFactory<>("iduser")); // Ensure this matches the property name in reclamation
        loadReclamations();
    }

            @FXML
            private void handleGenerateQRAction(ActionEvent event) {

            }

    private void loadReclamations() {
        try {
            List<reclamation> reclamationsList = ReclamationService.showReclamation(); // Assuming this returns List<reclamation>
            ObservableList<reclamation> observableList = FXCollections.observableArrayList(reclamationsList);
            tableView.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void refreshTableView() {
                loadReclamations(); // Recharger les données
                tableView.refresh(); // Rafraîchir le TableView
            }


            @FXML
            void modifier(ActionEvent event) {


                try {
                    // Load the FXML file
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/afficher.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());

                    // Get the current stage from the event source
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    currentStage.setTitle("GymSphere!");
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
            private void supprimer(ActionEvent event) {

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

            @FXML
            void listerep(ActionEvent event) {
                try {
                    // Load the FXML file
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/listrep.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());

                    // Get the current stage from the event source
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    currentStage.setTitle("GymSphere!");
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
            void ajouterec(ActionEvent event) {

                try {
                    // Load the FXML file
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ajoutrec.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());

                    // Get the current stage from the event source
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    currentStage.setTitle("GymSphere!");
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
    void ajouterreponse(ActionEvent event) {
        try {
            // Assurer que les champs ID utilisateur et ID réclamation ne sont pas vides et contiennent des entiers
            String idUserStr = iduserrepTF.getText().trim();
            String idRecStr = idrecrepTF.getText().trim();
            if (idUserStr.isEmpty() || idRecStr.isEmpty()) {
                throw new IllegalArgumentException("Les champs ID utilisateur et ID réclamation ne peuvent pas être vides.");
            }
            int idUser = Integer.parseInt(idUserStr);
            int idRec = Integer.parseInt(idRecStr);

            // Valider que la réponse et le titre ne sont pas vides
            String titre = titrerepTF.getText().trim();
            String reponse = repTF.getText().trim(); // Ici, repTF est déjà un TextField
            if (titre.isEmpty() || reponse.isEmpty()) {
                throw new IllegalArgumentException("Les champs titre et réponse ne peuvent pas être vides.");
            }

            // Assurer que typeTF a un élément sélectionné pour éviter NullPointerException


            // Assurer que daterepTF a une valeur pour éviter IllegalArgumentException
            LocalDate dateReponse = daterepTF.getValue();
            if (dateReponse == null) {
                throw new IllegalArgumentException("La date ne peut pas être vide.");
            }

            // Création de l'objet repondre sans ID (si ID est auto-généré)
            repondre nouvelleReponse = new repondre(titre, reponse, dateReponse, idUser, idRec);

            // Instance du service pour l'ajout de la réponse
            ServiceRepondre reponseService = new ServiceRepondre();
            reponseService.add(nouvelleReponse); // Ici, vous devez implémenter la méthode add dans ReponseService
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Réponse ajoutée avec succès.");
            alert.showAndWait();
//            afficherFormulaireReponse();



        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de Format");
            alert.setContentText("Les IDs utilisateur et réclamation doivent être des nombres entiers.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de Validation");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur inattendue est survenue : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private final ServiceReclamation ReclamationService = new ServiceReclamation();







    @FXML
            void search(ActionEvent event) {
                // Obtenez la chaîne de recherche
                String searchText = searchBar.getText().toLowerCase();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Choose the pattern that matches your date format


                if (searchText.isEmpty()) {
                    loadReclamations(); // Chargez toutes les réclamations si la barre de recherche est vide
                    return;
                }

                try {
                    // Obtenez toutes les réclamations disponibles
                    ObservableList<reclamation> allReclamations = FXCollections.observableArrayList(ReclamationService.showReclamation());

                    // Filtrez la liste en fonction de la chaîne de recherche
                    ObservableList<reclamation> filteredReclamations = allReclamations.filtered(reclamation -> {
                        //  ajuster les critères de recherche
                        // Par exemple, recherchez dans le titre, la description.
                        return reclamation.getTitre().toLowerCase().contains(searchText) ||
                                reclamation.getDescription().toLowerCase().contains(searchText);
                    });

                    // Mettez à jour le TableView avec les réclamations filtrées
                    tableView.setItems(filteredReclamations);
                } catch (Exception e) {
                    // Gérez toute exception, par exemple, affichez une alerte à l'utilisateur
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de la recherche");
                    alert.setContentText("Une erreur est survenue lors de la recherche : " + e.getMessage());
                    alert.showAndWait();
                }
            }

            @FXML
            void handleSearchButtonAction(ActionEvent event) {
                search(); // Appelle la méthode de recherche
            }

            void search() {
                String searchText = searchBar.getText().toLowerCase().trim();

                try {
                    ObservableList<reclamation> allReclamations = FXCollections.observableArrayList(ReclamationService.showReclamation());
                    ObservableList<reclamation> filteredReclamations = allReclamations.filtered(reclamation -> {
                        // Convertit l'id et l'idUser en chaînes de caractères pour la recherche
                        String idStr = String.valueOf(reclamation.getId());
                        String idUserStr = String.valueOf(reclamation.getIduser());
                        String typeStr = reclamation.getType().toLowerCase(); // Assurez-vous que getType() retourne une chaîne de caractères

                        // Vérifie si le texte de recherche correspond à n'importe quel attribut
                        return idStr.contains(searchText) ||
                                idUserStr.contains(searchText) ||
                                typeStr.contains(searchText) ||
                                reclamation.getTitre().toLowerCase().contains(searchText) ||
                                reclamation.getDescription().toLowerCase().contains(searchText);
                    });

                    tableView.setItems(filteredReclamations);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de la recherche");
                    alert.setContentText("Une erreur est survenue lors de la recherche : " + e.getMessage());
                    alert.showAndWait();
                }


            }
            @FXML
public void afficherFormulaireReponse(){
    reponse.setVisible(true);
}

    @FXML
    public void retourner(){
        reponse.setVisible(false);
    }





    @FXML
    private void handleArchivedReclamations(ActionEvent event) {
        try {
            ServiceReclamation serviceReclamation = new ServiceReclamation();
            List<reclamation> archivedReclamations = serviceReclamation.getAnsweredReclamations();
            ObservableList<reclamation> observableList = FXCollections.observableArrayList(archivedReclamations);
            tableView.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la récupération des réclamations archivées.");
        }
    }



    @FXML
    private void handleUnansweredReclamations(ActionEvent event) {
        try {
            ServiceReclamation serviceReclamation = new ServiceReclamation();
            List<reclamation> unAnsweredReclamations = serviceReclamation.getUnansweredReclamations();
            ObservableList<reclamation> observableList = FXCollections.observableArrayList(unAnsweredReclamations);
            tableView.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la récupération des réclamations non répondues.");
        }
    }



















    @FXML
    private void handleGeneratePDFAction(ActionEvent event) {
        generatePDF();
    }

    private void generatePDF() {
        // Use the selected file from the FileChooser to determine the save location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("reclamations.pdf");
        File file = fileChooser.showSaveDialog(null); // Assuming 'null' is your Stage

        if (file != null) {
            try {
                ObservableList<reclamation> reclamations = tableView.getItems();
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file.getAbsolutePath())); // Save to chosen path
                document.open();

                // Improved document formatting
                Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
                Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

                // Adding an image
                try {
                    Image logo = Image.getInstance("C:\\Users\\compuserv plus\\Downloads\\tacherecttttt\\tacherecettttt"); // Adjust path as needed
                    logo.scaleToFit(150, 150);
                    logo.setAlignment(Element.ALIGN_CENTER);
                    document.add(logo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Iterate over your data
                for (reclamation reclamation : reclamations) {
                    document.add(new Paragraph("Réclamation", titleFont));
                    document.add(new Paragraph(" "));
                    document.add(new Paragraph("Titre: " + reclamation.getTitre(), textFont));
                    document.add(new Paragraph("Date: " + reclamation.getDate_reclamation(), textFont));
                    document.add(new Paragraph("Type: " + reclamation.getType(), textFont));
                    document.add(new Paragraph("Description: " + reclamation.getDescription(), textFont));
                    document.add(new Paragraph(" "));
                    document.add(new Paragraph("-----------------------------------------------------"));
                    document.add(new Paragraph(" "));
                    document.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------- "));
                    document.add(new Paragraph("                              GymSphere                    "));
                }

                document.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Téléchargement Terminé");
                alert.setHeaderText(null);
                alert.setContentText("Le fichier PDF a été généré avec succès et est prêt pour le téléchargement.");
                alert.showAndWait();
            } catch (Exception ex) {
                Logger.getLogger(ReclamationController.class.getName()).log(Level.SEVERE, null, ex);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de Génération PDF");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur est survenue lors de la génération du fichier PDF : " + ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleExportExcelAction(ActionEvent event) {
        exportToExcel();
    }

    private void exportToExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("reclamations.xlsx");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File file = fileChooser.showSaveDialog(null); // null peut être remplacé par une référence de stage si nécessaire

        if (file != null) {
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = workbook.createSheet("Reclamations");
                // Création de la ligne d'en-tête
                String[] headers = {"Titre", "Description", "Type", "Date"};
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.length; i++) {
                    Cell headerCell = headerRow.createCell(i);
                    headerCell.setCellValue(headers[i]);
                }

                // Remplir le fichier Excel avec les données
                ObservableList<reclamation> reclamations = tableView.getItems();
                int rowIndex = 1;
                for (reclamation rec : reclamations) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rec.getTitre());
                    row.createCell(1).setCellValue(rec.getDescription());
                    row.createCell(2).setCellValue(rec.getType());
                    row.createCell(3).setCellValue(rec.getDate_reclamation().toString()); // Convertir la date en String si nécessaire
                }

                // Écriture des données dans le fichier Excel
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                }

                // Informer l'utilisateur que le téléchargement est terminé
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Le fichier Excel a été téléchargé avec succès !");
                alert.showAndWait();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors de l'exportation");
                alert.setContentText("Impossible de télécharger le fichier Excel : " + e.getMessage());
                alert.showAndWait();
            }
        }
    }




















    @FXML
    private void generateQRCodeAction(ActionEvent event) {
        try {
            QRCodeDatabaseGenerator generator = new QRCodeDatabaseGenerator();
            Path qrCodePath = generator.generateQRCodeFromDB();
            if (qrCodePath != null) {
                showAlert("QR Code Generated", "Le QR Code a été généré avec succès à l'emplacement : " + qrCodePath.toString());
            } else {
                showAlert("QR Code Generation Failed", "La génération du QR Code a échoué.");
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    public void returnToMain(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrudAdmin.fxml"));
            Parent root = loader.load();
            repTF.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}


