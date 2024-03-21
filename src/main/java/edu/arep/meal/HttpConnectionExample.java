package edu.arep.meal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class HttpConnectionExample {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String API_URL = "https://www.themealdb.com/api/json/v1/1/filter.php?";

    /**
     * Método principal para ejecutar la clase desde la línea de comandos.
     * Se espera un argumento que represente el parámetro de consulta para la API.
     *
     * @param args Los argumentos de línea de comandos.
     * @throws IOException Si ocurre un error de E/S durante la ejecución.
     */
    public static void main(String[] args) throws IOException {
        // Verificar si se proporciona un query como argumento de línea de comandos
        if (args.length != 1) {
            System.out.println("Uso: HttpConnectionExample <query>");
            return;
        }

        // Obtener el query de los argumentos de línea de comandos
        String queryParam = args[0];

        // Llamar al método makeGetRequest con el query proporcionado
        String response = makeGetRequest(queryParam);
        System.out.println(response);
    }

    /**
     * Método que realiza una solicitud HTTP GET a la API de comidas.
     *
     * @param queryParam El parámetro de consulta para la API.
     * @return La respuesta de la API en formato de cadena de caracteres.
     * @throws IOException Si ocurre un error de E/S durante la conexión.
     */
    public static String makeGetRequest(String queryParam) throws IOException {
        // Construir la URL completa con el parámetro de consulta
        String fullUrl = API_URL + queryParam;

        URL url = new URL(fullUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        // Realizar la conexión
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Leer la respuesta si la conexión es exitosa
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            // Manejar el caso en que la conexión no sea exitosa
            System.out.println("GET request not worked");
            return null;
        }
    }
}