package implementazioniPostgresDAO;

import dao.ClienteDAO;
import database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;

public class ClienteImplementazionePostgresDAO implements ClienteDAO {

    private Connection connection;
    // COSTRUTTORE: recupera la connessione condivisa dal Singleton
    public ClienteImplementazionePostgresDAO(){
        try
        {
            connection = ConnessioneDatabase.getInstance().connection;
        // GESTIONE ERRORI
        } catch (SQLException ex) {
            System.err.println("Errore, connessione al database non riuscita!");
            ex.printStackTrace();
        }
    }

    // Implementazione del metodo per la verifica del cliente sul DB tramite CF
    @Override
    public boolean verificaPatenteDB(String codiceFiscale) {
        boolean esiste = false;
        // Scrittura della query che selezionerà le informazioni sul DB tramite Codice Fiscale
        String query = "SELECT * FROM \"Cliente\" WHERE \"CodiceFiscale\" = ?;";

        // Preparazione ed esecuzione della query
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, codiceFiscale);
            // Esecuzione della query e verifica della presenza di risultati
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                esiste = true;
            }
            rs.close();
            // GESTIONE ERRORI
        } catch (SQLException ex) {
            System.err.println("Errore SQL durante la verifica del cliente: " + ex.getMessage());
        }
        return esiste;
    }

    // Implementazione del metodo per la registrazione di un nuovo cliente nel DB
    @Override
    public boolean registraClienteDB(String numeroPatente, LocalDate dataScadenza, String categoriaPatente, String nome, String cognome, String codiceFiscale, String email, String telefono) {
        // Query SQL per l'inserimento dei dati
        String query = "INSERT INTO \"Cliente\" (\"NumeroPatente\", \"DataScadenzaPatente\", \"CategoriaPatente\", \"Nome\", \"Cognome\", \"CodiceFiscale\", \"Email\", \"Telefono\") VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        // Preparazione dello statement e impostazione dei parametri
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, numeroPatente);
            ps.setDate(2, Date.valueOf(dataScadenza));
            ps.setString(3, categoriaPatente);
            ps.setString(4, nome);
            ps.setString(5, cognome);
            ps.setString(6, codiceFiscale);

            // Gestione dei campi
            if (email == null || email.trim().isEmpty()) {
                ps.setNull(7, java.sql.Types.VARCHAR);
            } else {
                ps.setString(7, email);
            }
            if (telefono == null || telefono.trim().isEmpty()) {
                ps.setNull(8, java.sql.Types.VARCHAR);
            } else {
                ps.setString(8, telefono);
            }
            // Esecuzione dell'inserimento e verifica del numero di righe modificate
            int righeInserite = ps.executeUpdate();
            return righeInserite > 0;
        // GESTIONE ERRORI
        } catch (SQLException ex) {
            System.err.println("Errore SQL durante la registrazione del cliente: " + ex.getMessage());
            return false;
        }
    }

    // Implementazione del metodo per l'aggiornamento di un cliente nel DB
    @Override
    public boolean aggiornaClienteDB(String numeroPatente, LocalDate dataScadenza, String categoriaPatente, String nome, String cognome, String codiceFiscale, String email, String telefono) {

        // Query SQL per l'aggiornamento dei dati
        String query = "UPDATE \"Cliente\" SET \"DataScadenzaPatente\" = ?, \"CategoriaPatente\" = ?, \"Nome\" = ?, \"Cognome\" = ?, \"NumeroPatente\" = ?, \"Email\" = ?, \"Telefono\" = ? WHERE \"CodiceFiscale\" = ?;";

        // Preparazione dello statement e impostazione dei parametri
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, Date.valueOf(dataScadenza));
            ps.setString(2, categoriaPatente);
            ps.setString(3, nome);
            ps.setString(4, cognome);
            ps.setString(5, numeroPatente);

            // controllo dell'inserimento dell e-mail
            if (email == null || email.trim().isEmpty()) {
                ps.setNull(6, Types.VARCHAR);
            } else {
                ps.setString(6, email);
            }

            // controllo dell'inserimento del numero di telefono
            if (telefono == null || telefono.trim().isEmpty()) {
                ps.setNull(7, Types.VARCHAR);
            } else {
                ps.setString(7, telefono);
            }

            // Impostiamo il Codice Fiscale per la clausola WHERE
            ps.setString(8, codiceFiscale);

            int righeAggiornate = ps.executeUpdate();
            return righeAggiornate > 0;

        } catch (SQLException ex) {
            System.err.println("Errore SQL durante l'aggiornamento del cliente: " + ex.getMessage());
            return false;
        }
    }
}
