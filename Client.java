import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class Client {

    
    public static void main(String[] args) {
      // ✅ Client cerca di connettersi al Server in ascolto sulla porta 6789
        // Un solo costrutto try-with-resources per tutte le risorse
        
        // === STABILIRE LA CONNESSIONE E INIZIALIZZARE GLI STREAM ===
        try (Socket socket = new Socket("localhost", 6789);
             BufferedOutputStream outputStream = 
                     //ottiene lo stream di output
                 new BufferedOutputStream(socket.getOutputStream()); 
             BufferedInputStream inputStream =
                    //ottiene lo stream di input 
                 new BufferedInputStream(socket.getInputStream())) {
            
            System.out.println("✅ Connesso al server - pronto per comunicazione");
            


            // === SEZIONE CLIENT INVIO MESSAGGIO AL SERVER ===
            String message = "Ciao, Server!";
            // Traduciamo la stringa in un array di bytes con metodo getBytes
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            // Invio effettivo dati al server
            outputStream.write(messageBytes);
            // Svuotiamo il buffer per forzare l'invio immediato
            outputStream.flush();
            
            System.out.println("✅ Messaggio inviato al Server: " + message);
            


            // === SEZIONE CLIENT RICEZIONE RISPOSTA DAL SERVER ===
            System.out.println("⏳ In attesa di risposta dal server...");
            
            // Creiamo un buffer dinamico per accumulare i dati ricevuti
            
            //buffer finale che accumula tutta la risposta
            ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();
            //buffer temporaneo di 1KB per letture parziali
            byte[] tempBuffer = new byte[1024]; 
            int bytesLetti;// contatore per bytes letti in ogni ciclo
            
            // Leggiamo tutti i dati disponibili dal server
            //1. FASE DI LETTURA 
            while ((bytesLetti = inputStream.read(tempBuffer)) != -1) {
                //ACCUMULO NEL BUFFER FINALE
                responseBuffer.write(tempBuffer, 0, bytesLetti);
                
                // Se non ci sono più dati immediatamente disponibili, usciamo
                if (inputStream.available() == 0) {
                    break;
                }
            }
            
            // Convertiamo i bytes ricevuti in stringa
            String serverResponse = responseBuffer.toString(StandardCharsets.UTF_8);
            System.out.println("📨 Messaggio ricevuto dal Server: " + serverResponse);
            
            System.out.println("🎉 Comunicazione completata con successo!");
            
        } catch (IOException e) {
            System.err.println("❌ Errore durante la comunicazione: " + e.getMessage());
            e.printStackTrace();
        }
        // ✅ Risorse chiuse automaticamente (socket, outputStream, inputStream)
    }

    
    
}

