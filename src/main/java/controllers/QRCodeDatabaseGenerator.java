package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class QRCodeDatabaseGenerator {

    private String jdbcURL = "jdbc:mysql://localhost:3306/sportbd";
    private String username = "root";
    private String password = "";

    // Modifiez cette méthode pour qu'elle retourne Path
    public Path generateQRCodeFromDB() {
        StringBuilder dataBuilder = new StringBuilder();
        Path qrCodePath = null; // Initialisez qrCodePath à null

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            String query = "SELECT id, description, titre, type, iduser, date_reclamation FROM reclamation";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String description = resultSet.getString("description");
                    String titre = resultSet.getString("titre");
                    String type = resultSet.getString("type");
                    int iduser = resultSet.getInt("iduser");
                    Date dateReclamation = resultSet.getDate("date_reclamation");
                    // Convertissez dateReclamation en LocalDate si nécessaire, sinon utilisez directement dateReclamation.toString() dans le formatage ci-dessous
                    String reclamationData = String.format("ID: %d, Description: %s, Titre: %s, Type: %s, ID Utilisateur: %d, Date de Réclamation: %s\n",
                            id, description, titre, type, iduser, dateReclamation);
                    dataBuilder.append(reclamationData);
                }

            }

            if (dataBuilder.length() > 0) {
                String allData = dataBuilder.toString();
                String fileName = "AllReclamationsData_QRCode.png";
                String specificPath = "C:\\Users\\compuserv plus\\Downloads\\tacherecttttt\\tacherecttttt"; // Chemin spécifié pour la sauvegarde
                qrCodePath = Paths.get(specificPath, fileName); // Utilisez Paths.get pour créer l'objet Path
                generateQRCode(allData, qrCodePath.toString(), 300, 300); // Modifiez ici pour utiliser qrCodePath.toString()
                System.out.println("Generated QR code for all reclamation data at " + qrCodePath);
            }
        } catch (SQLException | WriterException | IOException e) {
            e.printStackTrace();
        }
        return qrCodePath; // Retournez le Path du fichier QR Code généré
    }

    private static void generateQRCode(String data, String filePath, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
