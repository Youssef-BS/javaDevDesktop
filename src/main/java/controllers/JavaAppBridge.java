package controllers;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;
public class JavaAppBridge {
    private UserControllors userController;
    public void setUserController(UserControllors userController) {
        this.userController = userController;
    }
    public void processPayment(String clientSecret, int subscriptionAmount) {
        System.out.println("Processing payment with client secret: " + clientSecret);
        if (userController != null) {
            userController.handlePaymentWithToken(clientSecret, subscriptionAmount);
        }
    }
    public void inject(WebEngine webEngine) {
        webEngine.getLoadWorker().stateProperty().addListener(
                (ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        JSObject window = (JSObject) webEngine.executeScript("window");
                        window.setMember("javaApp", this);
                    }
                });
    }
}
