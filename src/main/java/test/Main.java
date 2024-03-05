package test;

import entities.Abonnement;
import services.AbonnementService;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args)  {
        AbonnementService abn = new AbonnementService() ;
       try {
           Abonnement ab = new Abonnement(120, true, 1, 1);
           abn.ajouter(ab);
       }catch (SQLException e){
           e.printStackTrace();
       }
    }
}
