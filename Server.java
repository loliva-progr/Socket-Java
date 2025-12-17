import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class Server{ 
   
    public static void main(String[] args) {
                try (ServerSocket serverSocket = new ServerSocket(6789)) {
            
   System.out.println("🚀 Server avviato - in attesa di connessioni sulla porta 6789...");
            
            // ⏳ Il server rimane in attesa di un client
          // === STABILIRE LA CONNESSIONE E INIZIALIZZARE GLI STREAM ===
            try (Socket clientSocket = serverSocket.accept();
                 BufferedInputStream inputStream = 
                     new BufferedInputStream(clientSocket.getInputStream());
                 BufferedOutputStream outputStream = 
                     new BufferedOutputStream(clientSocket.getOutputStream())) {
                
     System.out.println("✅ Client connesso ");


                // === SEZIONE SERVER RICEZIONE MESSAGGIO DAL CLIENT ===
                System.out.println("⏳ In attesa di messaggio dal client...");
                
                // Creiamo un buffer dinamico per accumulare i dati ricevuti
                ByteArrayOutputStream requestBuffer = new ByteArrayOutputStream();
                byte[] tempBuffer = new byte[1024]; // Buffer temporaneo di 1KB
                int bytesLetti;
                
                // Leggiamo tutti i dati disponibili dal client
                while ((bytesLetti = inputStream.read(tempBuffer)) != -1) {
                    requestBuffer.write(tempBuffer, 0, bytesLetti);
                    
                    // Se non ci sono più dati immediatamente disponibili, usciamo
                    if (inputStream.available() == 0) {
                        break;
                    }
                }
                
                // Convertiamo i bytes ricevuti in stringa
                String clientMessage = requestBuffer.toString(StandardCharsets.UTF_8);
               
	       System.out.println("📨 Messaggio ricevuto dal Client: " + clientMessage);


                // === SEZIONE SERVER INVIO RISPOSTA AL CLIENT ===
 String responseMessage = "Ciao Client! Ho ricevuto il tuo messaggio: '" + clientMessage;
                
                // Traduciamo la stringa in un array di bytes
                byte[] responseBytes = responseMessage.getBytes(StandardCharsets.UTF_8);
                
                // Invio risposta al client
                outputStream.write(responseBytes);
                // Svuotiamo il buffer per forzare l'invio immediato
                outputStream.flush();
                
                System.out.println("✅ Risposta inviata al Client: " + responseMessage);
                System.out.println("🎉 Comunicazione con client completata!");

            } catch (IOException e) {
                System.err.println("❌ Errore nella comunicazione con il client: " + e.getMessage());
                e.printStackTrace();
            }
            // ✅ Risorse client chiuse automaticamente (clientSocket, inputStream, outputStream)
            
        } catch (IOException e) {
            System.err.println("❌ Errore nell'avvio del server: " + e.getMessage());
            e.printStackTrace();
        }
        // ✅ ServerSocket chiuso automaticamente
    }

    
    
}
