package controllers;
import com.twilio.Twilio;
import entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import midlleware.TokenManager;
import services.UserService;
import java.io.IOException;
import java.sql.SQLException;
import javax.crypto.SecretKey;
import javax.mail.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Random;
import okhttp3.*;

import static com.twilio.example.ValidationExample.ACCOUNT_SID;
import static com.twilio.example.ValidationExample.AUTH_TOKEN;

public class LoginControllers {

    @FXML
    private TextField emailFx;

    @FXML
    private PasswordField passwordFx;

    @FXML
    private Button btnForgetPassword ;

    @FXML
    private Button btnLogBack ;

    @FXML
    private AnchorPane loginForm;
    @FXML
    private AnchorPane forgetPassword ;

    @FXML
    private TextField VeirfPhoneNumber ;

    @FXML
    private Pane secondForm ;

    @FXML
    private TextField codeVerif ;

    @FXML
    private TextField newPasswordInput;

    @FXML
    private Pane phoneInputs;

    @FXML
    private Pane newPassword;

    private final UserService userService = new UserService();
    private static final SecretKey YOUR_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);



    @FXML
    void authentifier(ActionEvent event){
        String enteredEmail = emailFx.getText();
        String enteredPassword = passwordFx.getText();
        User user = new User();
        user.setEmail(enteredEmail);
        user.setPassword(enteredPassword);

        try {
            String token = userService.authentifier(user);
            if (token != null) {
                System.out.println("Authentication successful. Token: " + token);
//                System.out.println(TokenManager.decodeEmail());
                boolean isAdmin = TokenManager.verifIfAdmin();
                System.out.println(isAdmin);
                if(isAdmin){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrudAdmin.fxml"));
                    Parent root = loader.load();
                    emailFx.getScene().setRoot(root);
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }} else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface.fxml"));
                        Parent root = loader.load();
                        emailFx.getScene().setRoot(root);
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                System.out.println("Authentication failed. Incorrect email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String verifIfAdmin() {
        String token = TokenManager.getToken();
        if (token == null) {
            return "nooo";
        } else {
           return token ;
        }
    }

    @FXML
    void naviguer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Register.fxml"));
            Parent root = loader.load();
            emailFx.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnForgetPassword) {
            loginForm.setVisible(false);
            forgetPassword.setVisible(true);
        }
        if (actionEvent.getSource() == btnLogBack) {
            loginForm.setVisible(true);
            forgetPassword.setVisible(false);
        }
    }
    private static final String TWILIO_PHONE_NUMBER = "+21693920399";
    private static final String API_KEY = "064090b192ec8ee7b14ad28f1deb9387-d0c837b4-d481-4ef2-8fbe-dccbb0f48434";
    private static final String API_BASE_URL = "https://dk6x2g.api.infobip.com";


    String phoneInput ;
    public void fetchUserByMail(ActionEvent event) {
        try {
            String phone = VeirfPhoneNumber.getText();
            if (phone.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Enter Your Email");
                alert.showAndWait();
                return;
            }

            boolean userFound = userService.fetchOneByPhone(phone);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            if (userFound) {
                String verificationCode = generateRandomCode();
                sendVerificationCodeViaSMS("+216"+phone, verificationCode); // Update the phone number here
                alert.setContentText("Verification code sent to your phone number");
                secondForm.setVisible(true);
                userService.insertOtpCode(verificationCode,phone);
                phoneInput = phone ;
            } else {
                alert.setContentText("User not found");
                secondForm.setVisible(false);
            }

            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendVerificationCodeViaSMS(String phoneNumber, String verificationCode) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\"from\":\"InfoSMS\",\"to\":\"" + phoneNumber + "\",\"text\":\"Your verification code is: " + verificationCode + "\"}";
        Request request = new Request.Builder()
                .url(API_BASE_URL + "/sms/1/text/single")
                .post(RequestBody.create(mediaType, requestBody))
                .addHeader("Authorization", "App " + API_KEY)
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println("Verification code sent via SMS to " + phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateRandomCode() {
        int codeLength = 4;
        String characters = "0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            code.append(randomChar);
        }
        return code.toString();
    }

    @FXML
    public void verifCodeInput(){
        try {
            String otp = userService.fetchUserOtp(phoneInput).getOtp();
            System.out.println(otp);
            System.out.println(codeVerif.getText());
            if (otp.equals(codeVerif.getText())) {
                phoneInputs.setVisible(false);
                newPassword.setVisible(true);
                secondForm.setVisible(false);
            } else {
                System.out.println("erreur");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updatePassword(){
        try{
            userService.updatePassword(newPasswordInput.getText(),phoneInput);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Password changed successfully");
            alert.showAndWait();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


}
