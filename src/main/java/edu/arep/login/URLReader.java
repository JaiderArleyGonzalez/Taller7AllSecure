package edu.arep.login;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class URLReader {

    /**
     * Lee el contenido de una URL utilizando HTTPS.
     *
     * @param sitetoread La URL a leer.
     * @return El contenido de la URL en formato de cadena de caracteres.
     */
    public static String readURL(String sitetoread) {
        StringBuilder jsonContent = new StringBuilder();

        try {
            // Create a file and a password representation
            File trustStoreFile = new File("certificados/myTrustStore.p12");
            char[] trustStorePassword = "654321".toCharArray();

            // Load the trust store, the default type is "pkcs12", the alternative is "jks"
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);

            // Get the singleton instance of the TrustManagerFactory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            // Init the TrustManagerFactory using the truststore object
            tmf.init(trustStore);

            // Set the default global SSLContext so all the connections will use it
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            SSLContext.setDefault(sslContext);

            // Create the URL object
            URL siteURL = new URL(sitetoread);

            // Crea el objeto que URLConnection
            URLConnection urlConnection = siteURL.openConnection();

            // Lee la respuesta HTTP
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String inputLine;
            boolean jsonStarted = false;
            while ((inputLine = reader.readLine()) != null) {
                if (jsonStarted) {
                    // Si ya hemos empezado a leer el JSON, lo añadimos al StringBuilder
                    jsonContent.append(inputLine).append("\n");
                } else if (inputLine.startsWith("{")) {
                    // Si encontramos el inicio del JSON, marcamos que hemos empezado a leerlo
                    jsonContent.append(inputLine).append("\n");
                    jsonStarted = true;
                }
            }

            reader.close();

        } catch (IOException e) {
            // Manejar excepciones de entrada/salida
            e.printStackTrace();
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }

        return jsonContent.toString();
    }
}
