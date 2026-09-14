package implementazioniPostgresDAO;

import dao.FatturaDAO;
import database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;

public class FatturaImplementazionePostgresDAO implements FatturaDAO {

    private Connection connection;
    // COSTRUTTORE: recupera la connessione dal Singleton
    public FatturaImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        // GESTIONE ERRORI
        } catch (SQLException ex) {
            System.err.println("Errore di connessione nel DAO Fattura!");
            ex.printStackTrace();
        }
    }

    // Implementazione del metodo per emettere la fattura sul DB
    @Override
    public boolean emettiFatturaDB(int idNoleggio, LocalDate dataEmissione, double importoBase, double importoPenali, double importoTotale, String metodoPagamento) {
        // Query SQL per l'inserimento dei dati
        String query = "INSERT INTO \"Fattura\" (\"IdNoleggio\", \"DataEmissione\", \"ImportoBase\", \"ImportoPenali\", \"ImportoTotale\", \"MetodoPagamento\") VALUES (?, ?, ?, ?, ?, ?);";
        // Preparazione dello statement e impostazione dei parametri
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idNoleggio);
            ps.setDate(2, Date.valueOf(dataEmissione));
            ps.setDouble(3, importoBase);
            ps.setDouble(4, importoPenali);
            ps.setDouble(5, importoTotale);
            ps.setString(6, metodoPagamento);
            return ps.executeUpdate() > 0;
        // GESTIONE ERRORI
        } catch (SQLException ex) {
            System.err.println("Errore SQL durante l'emissione della fattura: " + ex.getMessage());
            return false;
        }
    }

    // Implementazione metodo per la restituzione dei dati della fattura
    @Override
    public String[] getDatiFatturaPerGUI(int idNoleggio) {
        // Query SQL per la selezione dei dati sul DB
        String query = "SELECT c.\"Nome\", c.\"Cognome\", c.\"CodiceFiscale\", c.\"NumeroPatente\", " +
                "v.\"Marca\", v.\"Modello\", v.\"Targa\", f.\"ImportoTotale\" " +
                "FROM \"Noleggio\" n " +
                "JOIN \"Cliente\" c ON n.\"NumeroPatente\" = c.\"NumeroPatente\" " +
                "JOIN \"Veicolo\" v ON n.\"TargaVeicolo\" = v.\"Targa\" " +
                "JOIN \"Fattura\" f ON n.\"IdNoleggio\" = f.\"IdNoleggio\" " +
                "WHERE n.\"IdNoleggio\" = ?;";
        // Preparazione dello statement
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idNoleggio);
            // Impostazione dei parametri
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[] {
                            rs.getString("Nome"),
                            rs.getString("Cognome"),
                            rs.getString("CodiceFiscale"),
                            rs.getString("NumeroPatente"),
                            rs.getString("Marca"),
                            rs.getString("Modello"),
                            rs.getString("Targa"),
                            String.valueOf(rs.getDouble("ImportoTotale"))
                    };
                }
            }
        // GESTIONE ERRORI
        } catch (SQLException ex) {
            System.err.println("Errore SQL durante il recupero dei dati per la Fattura GUI: " + ex.getMessage());
        }
        return null;
    }
}