package implementazioniPostgresDAO;

import dao.NoleggioDAO;
import database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NoleggioImplementazionePostgresDAO implements NoleggioDAO {

    private Connection connection;

    //COSTRUTTORE: recupera la connessione condivisa dal Singleton
    public NoleggioImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException ex) {
            System.err.println("Errore di connessione al DAO di Noleggio: " + ex.getMessage());
        }
    }

    // Implementazione del metodo per registrare un nuovo noleggio sul DB
    @Override
    public int registraNoleggioDB(LocalDate dataInizio, LocalDate dataFinePrevista, String stato,
                                  String numeroPatente, String targa, String matricolaOperatore) {
        // Recupera la connessione
        try {
            connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException ex) {
            System.err.println("Errore di connessione: " + ex.getMessage());
            return -1;
        }
        if (connection == null) return -1;

        // Query SQL per modificare la tabella del DB
        String sql = "INSERT INTO \"Noleggio\" (\"DataInizio\", \"DataFinePrevista\", \"Stato\", \"NumeroPatente\", \"TargaVeicolo\", \"MatricolaOperatore\") " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        // Preparazione dello statement e impostazione dei parametri
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(dataInizio));
            ps.setDate(2, Date.valueOf(dataFinePrevista));
            ps.setString(3, stato);
            ps.setString(4, numeroPatente);
            ps.setString(5, targa);
            ps.setString(6, matricolaOperatore);

            // Esecuzione della query
            int affectedRows = ps.executeUpdate();
            // Controllo della corretta esecuzione della query
            if (affectedRows > 0) {
                // Recupero delle chiavi primarie dal DB
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Restituzione dell' id noleggio presente nella colonna 1
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Errore SQL registrazione noleggio: " + ex.getMessage());
        }
        return -1;
    }

    // Implementazione del metodo per la restituzione della lista noleggi
    @Override
    public void getListaNoleggiDB(ArrayList<String> id, ArrayList<String> inizio, ArrayList<String> fine,
                                  ArrayList<String> riconsegna, ArrayList<String> stato, ArrayList<String> patente,
                                  ArrayList<String> targa, ArrayList<Integer> posti, ArrayList<Integer> porte,
                                  ArrayList<String> matricola) {

        // Recupera la connessione
        try {
            connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException ex) {
            System.err.println("Errore di connessione: " + ex.getMessage());
            return;
        }
        if (connection == null) return;

        // Query SQL che seleziona i dati sul DB
        String sql = "SELECT N.\"IdNoleggio\", N.\"DataInizio\", N.\"DataFinePrevista\", N.\"DataRiconsegna\", " +
                "N.\"Stato\", N.\"NumeroPatente\", N.\"TargaVeicolo\", N.\"MatricolaOperatore\", " +
                "V.\"NumeroPosti\", V.\"NumeroPorte\" " +
                "FROM \"Noleggio\" N " +
                "JOIN \"Veicolo\" V ON N.\"TargaVeicolo\" = V.\"Targa\" " +
                "ORDER BY N.\"IdNoleggio\" DESC;";
        // Inizializzazione del formato della data
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Preparazione dello statement ed esecuzione della query
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            // Lettura e formattazione dei valori selezionati dalla query
            while (rs.next()) {
                id.add(String.valueOf(rs.getInt("IdNoleggio")));

                Date dInizio = rs.getDate("DataInizio");
                inizio.add(dInizio != null ? dInizio.toLocalDate().format(fmt) : "");

                Date dFine = rs.getDate("DataFinePrevista");
                fine.add(dFine != null ? dFine.toLocalDate().format(fmt) : "");

                Date dRientro = rs.getDate("DataRiconsegna");
                riconsegna.add(dRientro != null ? dRientro.toLocalDate().format(fmt) : "In Corso");

                stato.add(rs.getString("Stato"));
                patente.add(rs.getString("NumeroPatente"));
                targa.add(rs.getString("TargaVeicolo"));
                posti.add(rs.getInt("NumeroPosti"));
                porte.add(rs.getInt("NumeroPorte"));
                matricola.add(rs.getString("MatricolaOperatore"));
            }

        } catch (SQLException ex) {
            System.err.println("Errore SQL recupero lista noleggi: " + ex.getMessage());
        }
    }

    // Implementazione del metodo per la chiusura di un noleggio sul DB
    @Override
    public boolean chiudiNoleggioDB(int idNoleggio, LocalDate dataRientro) {
        // Recupera la connessione
        try {
            connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException ex) {
            System.err.println("Errore di connessione: " + ex.getMessage());
            return false;
        }
        if (connection == null) return false;

        // Query SQL per aggiornare la tabella 'Noleggio' del DB
        String sql = "UPDATE \"Noleggio\" SET \"Stato\" = 'Concluso', \"DataRiconsegna\" = ? WHERE \"IdNoleggio\" = ?;";

        // Preparazione dello statement ed esecuzione query
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(dataRientro));
            ps.setInt(2, idNoleggio);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Errore SQL chiusura noleggio: " + ex.getMessage());
            return false;
        }
    }
}