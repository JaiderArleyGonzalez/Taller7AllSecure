package edu.arep.meal;
import java.util.*;
import static spark.Spark.*;
public class mealService {
    /**
     * Método principal que inicia el servicio y define las rutas para manejar las solicitudes de comidas.
     * Configura el puerto del servidor y establece la seguridad utilizando un almacén de claves y confianza.
     *
     * @param args Los argumentos de línea de comandos (no utilizados en este caso).
     */
    public static void main(String[] args) {
        //API: secure(keystoreFilePath, keystorePassword, truststoreFilePath,truststorePassword);
        secure("certificados/ecikeystore.p12", "123456", null, null);
        port(getPort());

        get("/meal", (req, res) -> {
            String iParam = req.queryParams("i");
            String sParam = req.queryParams("s");
            String response = "Prueba con otra petición";

            if (iParam != null) {
                response = HttpConnectionExample.makeGetRequest("i=" + iParam);

            } else if (sParam != null) {
                response = HttpConnectionExample.makeGetRequest("s=" + sParam);

            }
            return response;
        });

    }

    /**
     * Método estático que obtiene el puerto del servidor desde la variable de entorno o devuelve un valor predeterminado.
     *
     * @return El puerto del servidor.
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 38000;
    }
}
