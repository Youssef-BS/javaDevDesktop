package midlleware;

import java.security.Key;
import java.util.Base64;
import java.util.prefs.Preferences;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class TokenManager {
    private static final String TOKEN_KEY = "jwt_token";
    private static final String SECRET_KEY = "YSF";

    public static String generateJwtToken(int id, String email, int isAdmin, int isCoach) {
        long expirationTime = System.currentTimeMillis() + 3600000;

        // Generate a secure key
        Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String token = Jwts.builder()
                .setSubject("user") // Subject of the token
                .claim("id", id)
                .claim("email", email)
                .claim("isAdmin", isAdmin)
                .claim("isCoach", isCoach)
                .setExpiration(new java.util.Date(expirationTime))
                .signWith(secretKey)
                .compact();

        // When generating the token
        String base64EncodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        // Store the token and key
        storeTokenAndKey(token, base64EncodedKey);

        return token;
    }

    public static void saveToken(String token) {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        prefs.put(TOKEN_KEY, token);
    }

    public static String getToken() {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        return prefs.get(TOKEN_KEY, null);
    }

    public static boolean hasToken() {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        return prefs.get(TOKEN_KEY, null) != null;
    }

    public static void clearToken() {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        prefs.remove(TOKEN_KEY);
    }

    public boolean verifToken() {
        String token = TokenManager.getToken();
        return token != null;
    }


    public static boolean verifIfAdmin() {
        String token = getToken();
        if (token == null) {
            return false;
        } else {
            try {
                // Decode the Base64-encoded key
                byte[] decodedKey = Base64.getDecoder().decode(getStoredKey());
                Key secretKey = Keys.hmacShaKeyFor(decodedKey);

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);
                Claims body = claimsJws.getBody();
                Object isAdminObject = body.get("isAdmin");
                Boolean isAdmin = null;
                if (isAdminObject instanceof Integer) {
                    isAdmin = ((Integer) isAdminObject) != 0;
                } else if (isAdminObject instanceof Boolean) {
                    isAdmin = (Boolean) isAdminObject;
                }
                return isAdmin != null && isAdmin;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int decodeId() {
        String token = getToken();
        if (token == null) {
            return -1;
        } else {
            try {
                // Decode the Base64-encoded key
                byte[] decodedKey = Base64.getDecoder().decode(getStoredKey());
                Key secretKey = Keys.hmacShaKeyFor(decodedKey);

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);

                Claims body = claimsJws.getBody();

                // Check for null before extracting the "id" from claims
                Object idObject = body.get("id");
                if (idObject != null) {
                    return (int) idObject; // or Integer.parseInt(idObject.toString())
                } else {
                    // Handle the case where "id" is null
                    return -1; // or throw new CustomException("id is null");
                }
            } catch (Exception e) {
                // Handle the exception appropriately
                e.printStackTrace(); // Consider logging the exception
                return -1; // or throw new CustomException("Error decoding ID");
            }
        }
    }



    private static void storeTokenAndKey(String token, String key) {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        prefs.put(TOKEN_KEY, token);
        prefs.put("key", key);
    }

    private static String getStoredKey() {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        return prefs.get("key", null);
    }
}
