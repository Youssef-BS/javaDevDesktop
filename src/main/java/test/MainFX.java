package test;
import midlleware.TokenManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        TokenManager midlleware = new TokenManager();
        boolean hasToken = midlleware.verifToken();
        boolean isAdmin = TokenManager.verifIfAdmin();
       FXMLLoader loader = new FXMLLoader(getClass().getResource(hasToken && isAdmin ? "/CrudAdmin.fxml"  : (hasToken && !isAdmin ? "/UserInterface.fxml"  : "/Login.fxml")));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReclamation.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signup.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EvenClient.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGym.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        if (hasToken) {
            System.out.println("Token exists");
        } else {
            System.out.println("Token does not exist");
        }
//        boolean isAdmin = TokenManager.verifIfAdmin();
//
//        if (isAdmin) {
//            System.out.println("User is an admin.");
//        } else {
//            System.out.println("User is not an admin or token is invalid/missing.");
//        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
