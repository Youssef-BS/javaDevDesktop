package controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import entities.reclamation;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceReclamation;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;



public class ajouterController {

    private final ServiceReclamation ReclamationService = new ServiceReclamation(); // Moved here, correct location

    @FXML
    private DatePicker dateTF;
    @FXML
    private TextField descriptionTF;
    // Specify the type of objects the ComboBoxes will hold
    @FXML
    private ComboBox<Integer> idUserComboBox; // Assuming it holds Integer IDs
    @FXML
    private TextField titreTF;
    @FXML
    private ComboBox<String> typeTF;
    // Assuming it holds String types


    @FXML
    private PieChart pieChart;


    @FXML
    private TextArea chatArea; // Pour afficher les messages
    @FXML
    private TextField userInputField; // Pour entrer les messages à envoyer


    @FXML
    private void onSendMessage(ActionEvent event) {
        String userMessage = userInputField.getText().trim();
        if (!userMessage.isEmpty()) {
            chatArea.appendText("Vous: " + userMessage + "\n");

            // Send the user message to Wit.ai and get the JSON response
            String jsonResponse = WitAiConnector.sendMessage(userMessage);

            // Parse the JSON response to get a user-friendly bot response
            String botResponse = BotResponseParser.getReadableResponse(jsonResponse);

            // Append the bot's response to the chat area
            chatArea.appendText("Bot: " + botResponse + "\n");

            // Clear the input field for the next message
            userInputField.clear();
        }
    }


    private void setupPieChart() {
        try {
            List<reclamation> reclamations = ReclamationService.showReclamation(); // Utilisez votre service pour obtenir les réclamations

            if (reclamations != null && !reclamations.isEmpty()) {
                // Calculer les statistiques
                Map<String, Integer> stats = new HashMap<>();
                for (reclamation rec : reclamations) {
                    String type = rec.getType();
                    stats.put(type, stats.getOrDefault(type, 0) + 1);
                }

                // Mise à jour du PieChart dans le thread de l'interface utilisateur
                Platform.runLater(() -> {
                    pieChart.getData().clear(); // Effacer les données précédentes
                    stats.forEach((type, count) -> {
                        PieChart.Data data = new PieChart.Data(type, count);
                        pieChart.getData().add(data);
                    });

                    // Appliquer les couleurs personnalisées et les labels pour les légendes
                    applyCustomColorsAndLabels();
                });
            } else {
                Platform.runLater(() -> pieChart.getData().clear()); // Nettoyer le PieChart si aucune donnée n'est disponible
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyCustomColorsAndLabels() {
        // Un map pour stocker la correspondance des types et des couleurs pour référence
        Map<String, String> typeToColorMap = new HashMap<>();
        typeToColorMap.put("Reclamation sur service", "orange");
        typeToColorMap.put("Reclamation sur security", "blue");
        typeToColorMap.put("Reclamation sur produit", "yellow");
        typeToColorMap.put("Reclamation sur colis", "red");
        typeToColorMap.put("Reclamation sur livreur", "green");
        // Ajoutez les autres types et couleurs ici

        pieChart.getData().forEach(data -> {
            String type = data.getName();
            String color = typeToColorMap.get(type);
            if (color != null) {
                data.getNode().setStyle("-fx-pie-color: " + color + ";");
            }
        });

        // Mise à jour ou création d'une légende personnalisée
        // Vous devrez peut-être créer votre propre légende personnalisée si JavaFX ne la gère pas automatiquement comme vous le souhaitez
    }





    @FXML
    void ajouter(ActionEvent event) {
        String titre = null;
        String type = null;
        LocalDate dateReclamation = null;
        Integer idUser = null;
        try {
            // Correctly retrieve selected items with proper types
            idUser = idUserComboBox.getSelectionModel().getSelectedItem();
            if (idUser == null) {
                throw new IllegalArgumentException("Vous devez sélectionner un ID utilisateur.");
            }

            String description = descriptionTF.getText().trim();
            if (description.isEmpty() || description.matches("^\\d+$")) {
                throw new IllegalArgumentException("La description ne peut pas être vide ou un nombre.");
            }

            titre = titreTF.getText().trim();
            if (titre.isEmpty() || titre.matches("^\\d+$")) {
                throw new IllegalArgumentException("Le titre ne peut pas être vide ou un nombre.");
            }

            type = typeTF.getSelectionModel().getSelectedItem();
            if (type == null || type.isEmpty()) {
                throw new IllegalArgumentException("Vous devez sélectionner un type de réclamation.");
            }

            dateReclamation = dateTF.getValue();
            if (dateReclamation == null) {
                throw new IllegalArgumentException("La date ne peut pas être vide.");
            }

            // Proceed with adding the reclamation
            reclamation newReclamation = new reclamation(description, titre, type, dateReclamation, idUser);
            ReclamationService.add(newReclamation);

            showAlert(Alert.AlertType.INFORMATION, "Information", "Réclamation ajoutée avec succès.");


            // Après l'ajout réussi, envoyez l'email.
            String emailSubject = "Nouvelle Réclamation";
            String emailMessage = String.format(
                    "Une nouvelle réclamation a été ajoutée dans le système.\n\nTitre: %s\nType: %s\nDescription: %s\nDate: %s",
                    titre,
                    type,
                    descriptionTF.getText().trim(),
                    dateReclamation.toString()
            );
            sendEmail("ahmeddslama2002@gmail.com", emailSubject, emailMessage);




            // Construct the SMS message to include the title and type
            String adminPhoneNumber = "+21654828257"; // Replace with the admin's phone number
            String message = "A new reclamation has been added.\nTitle: " + titre + "\nType: " + type;
            sendSmsNotification(adminPhoneNumber, message);


            String whatsappNumber = "+21654828257"; // Remplacez par votre numéro de téléphone WhatsApp connecté à la sandbox
            String whatsappMessage = "Une nouvelle réclamation a été ajoutée.\nTitre: " + titre + "\nType: " + type;
            sendWhatsAppNotification(whatsappNumber, whatsappMessage);



        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }




    @FXML
    public void initialize() {
        // Correctly initialize typeTF with String items
        typeTF.getItems().clear();
        List<String> types = Arrays.asList(
                "Reclamation sur service",
                "Reclamation sur security",
                "Reclamation sur produit",
                "Reclamation sur livreur",
                "Reclamation sur colis"
        );
        typeTF.getItems().addAll(types);
        typeTF.getSelectionModel().selectFirst();

        initializeIdUserComboBox();
        setupPieChart();
    }

    private void initializeIdUserComboBox() {
        try {
            List<Integer> userIds = ServiceReclamation.fetchUserIds();
            if (!userIds.isEmpty()) {
                idUserComboBox.setItems(FXCollections.observableArrayList(userIds));
                idUserComboBox.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading User IDs into ComboBox: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void afficherrec(ActionEvent event) {

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
            System.err.println("IOException: Error loading listrep.fxml");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: An unknown error occurred");
            e.printStackTrace();
        }

    }

    public void setData(int id, String titre, String description, String type, int iduser, LocalDate date_reclamation) {
        // Assuming titreTF is a TextField
        titreTF.setText(titre);

        // Assuming descriptionTF is a TextField
        descriptionTF.setText(description);

        // For ComboBox, you need to select the item that matches 'type'
        // This assumes 'type' exactly matches one of the items in the ComboBox
        typeTF.getSelectionModel().select(typeTF.getItems().stream()
                .filter(item -> item.equals(type))
                .findFirst()
                .orElse(null)); // Selects the item if found, otherwise does nothing

        // Set the date in DatePicker
        dateTF.setValue(date_reclamation);
    }


    @FXML
    void supprimer(ActionEvent event) {

    }






    private void clearFormFields() {
        // Clear each form field
        dateTF.setValue(null); // Clear DatePicker
        descriptionTF.setText(""); // Clear TextField
        idUserComboBox.getSelectionModel().clearSelection(); // Clear ComboBox selection (does not remove items)
        titreTF.setText(""); // Clear TextField
        typeTF.getSelectionModel().clearSelection(); // Clear ComboBox selection (does not remove items)
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Changed to INFORMATION for success message
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    // Your Twilio credentials
    private static final String ACCOUNT_SID = "AC2c8145c6186f63aba7fec5a2e397cb30";
    private static final String AUTH_TOKEN = "2c2c87d7e3545a9a04170bb710c3c6e4";
    private static final String TWILIO_PHONE_NUMBER = "+17692475984";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendSmsNotification(String toPhoneNumber, String messageBody) {
        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber),  // To phone number
                new PhoneNumber(TWILIO_PHONE_NUMBER),  // From Twilio phone number
                messageBody
        ).create();

        System.out.println("Sent message with ID: " + message.getSid());
    }






    private void sendEmail(String recipientEmail, String subject, String messageText) {
        // Configuration des propriétés pour Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Votre adresse email Gmail et mot de passe ou mot de passe d'application
        final String myAccountEmail = "ahmeddslama2002@gmail.com";
        final String password = "nicg pbyy gfbu wqtz";

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        try {
            jakarta.mail.Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);
            System.out.println("Email envoyé avec succès");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    public void sendWhatsAppNotification(String toWhatsAppNumber, String messageBody) {
        // Le format du numéro de téléphone doit être préfixé par 'whatsapp:'
        String toWhatsApp = "whatsapp:" + toWhatsAppNumber;

        // Le numéro de téléphone de Twilio pour WhatsApp commence également par 'whatsapp:'
        String fromWhatsApp = "whatsapp:+14155238886"; // Utilisez le numéro de votre Sandbox WhatsApp ici

        Message message = Message.creator(
                new PhoneNumber(toWhatsApp),
                new PhoneNumber(fromWhatsApp),
                messageBody
        ).create();

        System.out.println("Sent WhatsApp message with ID: " + message.getSid());
    }


    @FXML
    public void returnToMain(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface.fxml"));
            Parent root = loader.load();
            titreTF.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}



