package edu.arep.login;

import edu.arep.meal.HttpConnectionExample;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;

import static spark.Spark.*;
public class LoginService {
    private static HashMap<String, String> usuarios = new HashMap<>();
    public static HashMap<String, Boolean> sesiones = new HashMap<>();

    private static String URLFOOD = "https://ec2-44-201-194-75.compute-1.amazonaws.com:38000/meal?";

    /**
     * Método principal que inicia el servicio de inicio de sesión.
     *
     * @param args Los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        //API: secure(keystoreFilePath, keystorePassword, truststoreFilePath,truststorePassword);
        secure("certificados/ecikeystore.p12", "123456", null, null);
        port(getPort());
        staticFiles.location("/public");
        get("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");
            if (login(username, password)) {
                String sesion = UUID.randomUUID().toString();
                sesiones.put(sesion, true);
                res.cookie("sesion", sesion);
                res.redirect("/meals.html");
                return null;
            } else {
                return "Nombre de usuario o contraseña incorrectos";
            }
        });

        // Definir filtro de autenticación
        before("/meal/*", (req, res) -> {
            if (!sesionValida(req.cookie("sesion"))) {
                halt(401, "No autorizado");
            }
        });

        get("/meal/filter", (req, res) -> {

            String iParam = req.queryParams("i");
            String sParam = req.queryParams("s");
            String response = "Prueba con otra petición";
            if (iParam != null) {
                return URLReader.readURL(URLFOOD + "i=" + iParam);

            } else if (sParam != null) {
                return URLReader.readURL(URLFOOD + "s=" + sParam);

            }
            return response;

        });
        initializeUsers();
    }

    /**
     * Método para obtener el puerto del entorno o utilizar uno predeterminado.
     *
     * @return El número de puerto.
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000;
    }

    /**
     * Método para obtener el mapa de usuarios.
     *
     * @return El mapa de usuarios.
     */
    private HashMap<String, String> getUsuarios() {
        return usuarios;
    }

    /**
     * Método para realizar el inicio de sesión.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña.
     * @return true si el inicio de sesión es exitoso, false de lo contrario.
     */
    public static boolean login(String username, String password) {
        String storedPassword = usuarios.get(username);
        if (storedPassword == null) {
            return false;
        }

        String hashedPassword = hashSHA256(password);

        if (hashedPassword.equals(storedPassword)) {
            // Generar token de sesión y almacenarlo
            String sesion = UUID.randomUUID().toString();
            sesiones.put(sesion, true);

            return true;
        }

        return false;
    }

    /**
     * Método para verificar si la sesión es válida.
     *
     * @param sesion El token de sesión.
     * @return true si la sesión es válida, false de lo contrario.
     */
    public static boolean sesionValida(String sesion) {
        return sesion != null && sesiones.containsKey(sesion);
    }

    /**
     * Método para inicializar los usuarios con sus contraseñas hash.
     */
    public static void initializeUsers() {

        usuarios.put("usuario1", hashSHA256("password1"));
        usuarios.put("usuario2", hashSHA256("password2"));
        usuarios.put("usuario3", hashSHA256("password3"));
    }

    /**
     * Método para generar el hash SHA-256 de una cadena.
     *
     * @param input La cadena de entrada.
     * @return El hash SHA-256 de la cadena.
     */
    private static String hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedHash);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}