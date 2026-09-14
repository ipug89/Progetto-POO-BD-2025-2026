package implementazioniPostgresDAO;

import dao.SchedaIspezioneDAO;
import database.ConnessioneDatabase;
import model.schede.SchedaUscita;
import java.sql.*;
import java.time.LocalDate;

public class SchedaIspezioneImplementazionePostgresDAO implements SchedaIspezioneDAO {
    private Connection connection;

    public SchedaIspezioneImplementazionePostgresDAO()
    {
        try
        {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException ex) {
            System.err.println("Errore di connessione al DAO di veicolo");
            ex.printStackTrace();
        }
    }
 // metodo per registrare un nuovo utente nel db
    @Override
    public boolean registraUscitaDB(int idNoleggio, LocalDate dataUscita, double kmUscita, float carburante, boolean lavaggio, String noteDanni) {

        String query = "INSERT INTO \"SchedaIspezione\" (\"IdNoleggio\", \"DataUscita\", \"KmUscita\", \"CarburanteUscita\", \"Lavaggio\", \"NoteDanniUscita\") VALUES (?, ?, ?, ?, ?, ?);";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idNoleggio);
            //conversione da java.timr a java.sql.date
            ps.setDate(2, Date.valueOf(dataUscita));
            ps.setDouble(3, kmUscita);
            ps.setFloat(4, carburante);
            ps.setBoolean(5, lavaggio);
   // Gestione del campo testuale opzionale (converte vuoto/null in SQL NULL)
            if (noteDanni == null || noteDanni.trim().isEmpty()) {
                ps.setNull(6, java.sql.Types.VARCHAR);
            } else {
                ps.setString(6, noteDanni);
            }
        // Restituisce true se almeno una riga è stata inserita
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    //Aggiorna la scheda di ispezione esistente registrando i dati rilevati al rientro del veicolo.
    @Override
    public boolean registraRientroDB(int idNoleggio, LocalDate dataRientro, double kmRientro, float carburante, String nuoviDanni) {

        String query = "UPDATE \"SchedaIspezione\" SET \"DataRientro\" = ?, \"KmRientro\" = ?, \"CarburanteRientro\" = ?, \"NuoviDanni\" = ? WHERE \"IdNoleggio\" = ?;";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, Date.valueOf(dataRientro));
            ps.setDouble(2, kmRientro);
            ps.setFloat(3, carburante);
         // Gestione dei nuovi danni opzionali
            if (nuoviDanni == null || nuoviDanni.trim().isEmpty()) {
                ps.setNull(4, java.sql.Types.VARCHAR);
            } else {
                ps.setString(4, nuoviDanni);
            }

            ps.setInt(5, idNoleggio);
// Verifica che la clausola WHERE abbia intercettato e aggiornato la riga
            int righeInserite = ps.executeUpdate();
            return righeInserite > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}