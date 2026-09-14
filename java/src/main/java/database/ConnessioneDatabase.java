package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    // Mantiene l'unica istanza condivisa di questa classe
    private static ConnessioneDatabase instance;
    // Definizione dell' oggetto connection, che gestirà la sessione attiva con il DB
    public Connection connection;

    // Variabile privata per il nome utente per accedere a postgres
    private String nome = "postgres";
    // Variabile privata per la password
    private String password = "1234";
    // Variabile privata per l' URL per raggiungere il DB
    private String url = "jdbc:postgresql://localhost:5432/NoleggioSmart";

    // COSTRUTTORE PRIVATO : IMPEDISCE DI CREARE OGETTI DALL' ESTERNO
    private ConnessioneDatabase() throws SQLException {
        // Blocco di tentativi per instaurare la connessione
        try {
            // Stabilisce la connessione passando url, nome e password
            this.connection = DriverManager.getConnection(url, nome, password);
            // Stampa del messaggio informativo
            System.out.println("Connessione al database 'NoleggioSmart' riuscita con successo!");
        // GESTIONI ERRORI
        } catch (SQLException ex) {
            System.err.println("ERRORE: Impossibile connettersi al database: " + ex.getMessage());
            throw ex;
        }
    }
    // METODO STATICO PUBBLICO PER ACCEDERE ALL' UNICA ISTANZA DELLA CLASSE
    public static ConnessioneDatabase getInstance() throws SQLException {
        // Controllo dell'esistenza dell' istanza e della connessione (chiusa/aperta)
        if (instance == null || instance.connection == null || instance.connection.isClosed()) {
            // Creazione dell' istanza
            instance = new ConnessioneDatabase();
        }
        return instance;
    }

    // Metodo getter richiesto dai DAO
    public Connection getConnection() {
        try {
            // Controllo dello stato della connessione
            if (this.connection == null || this.connection.isClosed()) {
                // Se la connessione è stata chiusa, la riapre
                this.connection = DriverManager.getConnection(url, nome, password);
                // Stampa del messaggio informativo
                System.out.println("Connessione al database ripristinata automaticamente.");
            }
        // GESTIONE ERRORI
        } catch (SQLException e) {
            System.err.println("Impossibile ripristinare la connessione: " + e.getMessage());
        }
        return this.connection;
    }
}