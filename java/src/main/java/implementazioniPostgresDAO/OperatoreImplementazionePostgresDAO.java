package implementazioniPostgresDAO;

import dao.OperatoreDAO;
import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//implementazione dell'interfaccia per il database , gestisce la persistenza e il recupero dei dati
public class OperatoreImplementazionePostgresDAO implements OperatoreDAO {

    private Connection connection;

    //costruttore della classe , inizializza recuperando l'istanza singleton
    public OperatoreImplementazionePostgresDAO() {

        try
        {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException ex) {
            System.err.println("ERRORE, connessione al database non riuscita!");
            ex.printStackTrace();
        }
    }
    //funzione per verificare la matricola
    @Override
    public boolean verificaMatricolaDB(String matricola) {
        boolean esiste = false;

        String query = "SELECT * FROM \"Operatore\" WHERE \"Matricola\" = ?;";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, matricola);

            ResultSet rs = ps.executeQuery();
    // se il cursore si sposta sul primo record allora l'operatore è presente
            if (rs.next()) {
                esiste = true;
            }

            rs.close();
        } catch (SQLException e) {
            System.err.println("ERRORE, matricola inesistente!");
            e.printStackTrace();
        };

        return esiste;
    }

    // reigstra un nuovo operatore nella tabella dedicata del database
    //gestisce i campi facoltativi convertendo in stringhe vuote o nylle
    @Override
    public boolean registaOperatoreDB(String matricola, String nome, String cognome, String codiceFiscale, String email, String telefono) {
        String query = "INSERT INTO \"Operatore\" (\"Matricola\", \"Nome\", \"Cognome\", \"CodiceFiscale\", \"Email\", \"Telefono\") VALUES (?, ?, ?, ?, ?, ?);";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            //parametri obbligatori
            ps.setString(1, matricola);
            ps.setString(2, nome);
            ps.setString(3, cognome);
            ps.setString(4, codiceFiscale);

          //gestisce i valori opzionali
            if (email == null || email.trim().isEmpty()) {
                ps.setNull(5, java.sql.Types.VARCHAR);
            } else {
                ps.setString(5, email);
            }

            if (telefono == null || telefono.trim().isEmpty()) {
                ps.setNull(6, java.sql.Types.VARCHAR);
            } else {
                ps.setString(6, telefono);
            }
    // esecuzione e verifica del numero di righe modificate
            int righeInserite = ps.executeUpdate();
            return righeInserite > 0;
        } catch (SQLException e) {
            System.err.println("Errore SQL durante l'inserimento dell'operatore: " + e.getMessage());
            return false;
        }
    }
}
