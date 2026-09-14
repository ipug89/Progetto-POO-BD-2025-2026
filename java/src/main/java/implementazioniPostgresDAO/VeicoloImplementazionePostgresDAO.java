package implementazioniPostgresDAO;

import dao.VeicoloDAO;
import database.ConnessioneDatabase;
import model.veicoli.Auto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VeicoloImplementazionePostgresDAO implements VeicoloDAO {

    private Connection connection;

    public VeicoloImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException ex) {
            System.err.println("Errore di connessione al DAO di veicolo");
            ex.printStackTrace();
        }
    }
//registra nuoa auto nel database o ne aggiorna i dati se già presente
    //se la targa esiste gia aggiorna gli attributi e ripristina lo stato a non cancellato
    @Override
    public boolean registraAuto(String targa, String marca, String modello, double km, double tariffaGg, int numeroPosti, int numeroPorte, String categoria) {
       // Query con clausola ON CONFLICT per gestire l'inserimento o l'aggiornamento idempotente (Upsert)
        String query = "INSERT INTO \"Veicolo\" (\"Targa\", \"Tipologia\", \"Marca\", \"Modello\", \"ChilometraggioAttuale\", \"TariffaGiornaliera\", \"NumeroPosti\", \"NumeroPorte\", \"CategoriaAuto\", \"Cancellato\") " +
                "VALUES (?, 'Auto', ?, ?, ?, ?, ?, ?, ?, FALSE) " +
                "ON CONFLICT (\"Targa\") DO UPDATE SET " +
                "\"Marca\" = EXCLUDED.\"Marca\", \"Modello\" = EXCLUDED.\"Modello\", \"ChilometraggioAttuale\" = EXCLUDED.\"ChilometraggioAttuale\", " +
                "\"TariffaGiornaliera\" = EXCLUDED.\"TariffaGiornaliera\", \"NumeroPosti\" = EXCLUDED.\"NumeroPosti\", " +
                "\"NumeroPorte\" = EXCLUDED.\"NumeroPorte\", \"CategoriaAuto\" = EXCLUDED.\"CategoriaAuto\", \"Cancellato\" = FALSE;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, targa);
            ps.setString(2, marca);
            ps.setString(3, modello);
            ps.setDouble(4, km);
            ps.setDouble(5, tariffaGg);
            ps.setInt(6, numeroPosti);
            ps.setInt(7, numeroPorte);
            ps.setString(8, categoria);

            int righeInserite = ps.executeUpdate();
            return righeInserite > 0;
        } catch (SQLException ex) {
            System.err.println("Errore SQL durante la registrazione dell'auto: " + ex.getMessage());
            return false;
        }
    }
//Registra un nuovo furgone nel database o ne aggiorna i dettagli di carico se la targa esiste già (Upsert).
    @Override
    public boolean registraFurgone(String targa, String marca, String modello, double km, double tariffaGg,
                                   int nPosti, int nPorte, double capacitaKg, double volume, double altezza) {
        String sql = "INSERT INTO \"Veicolo\" (\"Targa\", \"Tipologia\", \"Marca\", \"Modello\", \"ChilometraggioAttuale\", \"TariffaGiornaliera\", \"NumeroPosti\", \"NumeroPorte\", \"CapacitaKg\", \"VolumeM3\", \"AltezzaM\", \"Cancellato\") " +
                "VALUES (?, 'Furgone', ?, ?, ?, ?, ?, ?, ?, ?, ?, FALSE) " +
                "ON CONFLICT (\"Targa\") DO UPDATE SET " +
                "\"Marca\" = EXCLUDED.\"Marca\", \"Modello\" = EXCLUDED.\"Modello\", \"ChilometraggioAttuale\" = EXCLUDED.\"ChilometraggioAttuale\", " +
                "\"TariffaGiornaliera\" = EXCLUDED.\"TariffaGiornaliera\", \"NumeroPosti\" = EXCLUDED.\"NumeroPosti\", \"NumeroPorte\" = EXCLUDED.\"NumeroPorte\", " +
                "\"CapacitaKg\" = EXCLUDED.\"CapacitaKg\", \"VolumeM3\" = EXCLUDED.\"VolumeM3\", \"AltezzaM\" = EXCLUDED.\"AltezzaM\", \"Cancellato\" = FALSE;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, targa);
            ps.setString(2, marca);
            ps.setString(3, modello);
            ps.setDouble(4, km);
            ps.setDouble(5, tariffaGg);
            ps.setInt(6, nPosti);
            ps.setInt(7, nPorte);
            ps.setDouble(8, capacitaKg);
            ps.setDouble(9, volume);
            ps.setDouble(10, altezza);

            int righeInserite = ps.executeUpdate();
            return righeInserite > 0;

        } catch (SQLException ex) {
            System.err.println("Errore SQL durante la registrazione del furgone: " + ex.getMessage());
            return false;
        }
    }
//Recupera tutte le autovetture attualmente disponibili per il noleggio (non occupate e non rimosse logicamente).
//Popola le collezioni passate come argomento in modo parallelo per ciascun record estratto.
    @Override
    public void getAutoDisponibiliDB(ArrayList<String> targhe, ArrayList<String> marche, ArrayList<String> modelli,
                                     ArrayList<Double> chilometri, ArrayList<Double> tariffe,
                                     ArrayList<Integer> posti, ArrayList<Integer> porte, ArrayList<String> categorie) {
        // Filtra per tipo 'Auto', stato libero e record non marcati come cancellati
        String query = "SELECT * FROM \"Veicolo\" WHERE \"Tipologia\" = 'Auto' AND \"StatoOccupato\" = FALSE AND (\"Cancellato\" = FALSE OR \"Cancellato\" IS NULL);";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                targhe.add(rs.getString("Targa"));
                marche.add(rs.getString("Marca"));
                modelli.add(rs.getString("Modello"));
                chilometri.add(rs.getDouble("ChilometraggioAttuale"));
                tariffe.add(rs.getDouble("TariffaGiornaliera"));
                posti.add(rs.getInt("NumeroPosti"));
                porte.add(rs.getInt("NumeroPorte"));
                categorie.add(rs.getString("CategoriaAuto"));
            }

        } catch (SQLException ex) {
            System.err.println("Errore nell'esecuzione della query per le auto disponibili.");
            ex.printStackTrace();
        }
    }
//Recupera tutti i furgoni attualmente disponibili per il noleggio (non occupati e non rimossi logicamente).
// Popola le collezioni fornite come argomento con i dati anagrafici e dimensionali dei mezzi.
    @Override
    public void getFurgoniDisponibiliDB(ArrayList<String> targhe, ArrayList<String> marche, ArrayList<String> modelli,
                                        ArrayList<Double> chilometri, ArrayList<Double> tariffe,
                                        ArrayList<Integer> posti, ArrayList<Integer> porte,
                                        ArrayList<Double> capacita, ArrayList<Double> volumi, ArrayList<Double> altezze) {

        String sql = "SELECT * FROM \"Veicolo\" WHERE \"Tipologia\" = 'Furgone' AND \"StatoOccupato\" = FALSE AND (\"Cancellato\" = FALSE OR \"Cancellato\" IS NULL);";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                targhe.add(rs.getString("Targa"));
                marche.add(rs.getString("Marca"));
                modelli.add(rs.getString("Modello"));
                chilometri.add(rs.getDouble("ChilometraggioAttuale"));
                tariffe.add(rs.getDouble("TariffaGiornaliera"));
                posti.add(rs.getInt("NumeroPosti"));
                porte.add(rs.getInt("NumeroPorte"));
                capacita.add(rs.getDouble("CapacitaKg"));
                volumi.add(rs.getDouble("VolumeM3"));
                altezze.add(rs.getDouble("AltezzaM"));
            }

        } catch (SQLException ex) {
            System.err.println("Errore nell'esecuzione della query per i furgoni disponibili.");
            ex.printStackTrace();
        }
    }
    //Modifica lo stato di occupazione di un veicolo
    @Override
    public boolean impostaStatoVeicoloDB(String targa, boolean statoOccupato) {
        String sql = "UPDATE \"Veicolo\" SET \"StatoOccupato\" = ? WHERE \"Targa\" = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, statoOccupato);
            ps.setString(2, targa);

            int righeAggiornate = ps.executeUpdate();
            return righeAggiornate > 0;
        } catch (SQLException ex) {
            System.err.println("Errore durante l'aggiornamento dello stato del veicolo: " + ex.getMessage());
            return false;
        }
    }
//Aggiorna il valore del contachilometri del veicolo specificato.
    @Override
    public boolean aggiornaChilometriVeicoloDB(String targa, double nuoviKm) {
        String query = "UPDATE \"Veicolo\" SET \"ChilometraggioAttuale\" = ? WHERE \"Targa\" = ?;";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, nuoviKm);
            ps.setString(2, targa);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Errore SQL durante l'aggiornamento dei km del veicolo: " + ex.getMessage());
            return false;
        }
    }
//Recupera la tariffa giornaliera associata a un veicolo tramite la targa.
    @Override
    public double getTariffaVeicoloDB(String targa) {
        String query = "SELECT \"TariffaGiornaliera\" FROM \"Veicolo\" WHERE \"Targa\" = ?;";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, targa);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("TariffaGiornaliera");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Errore SQL nella lettura della tariffa: " + ex.getMessage());
        }

        return 0;
    }
//Esegue una cancellazione logica (soft delete) del veicolo impostando il flag 'Cancellato'
//Il veicolo non viene rimosso fisicamente per mantenere l'integrità referenziale con lo storico dei noleggi.
    @Override
    public boolean disattivaVeicoloDB(String targa) {
        String query = "UPDATE \"Veicolo\" SET \"Cancellato\" = TRUE WHERE \"Targa\" = ?;";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, targa);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Errore SQL durante la disattivazione del veicolo: " + ex.getMessage());
            return false;
        }
    }
//Legge il chilometraggio attuale registrato per un dato veicolo.
    @Override
    public double getChilometriVeicoloDB(String targa) {
        String query = "SELECT \"ChilometraggioAttuale\" FROM \"Veicolo\" WHERE \"Targa\" = ?;";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, targa);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("ChilometraggioAttuale");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Errore SQL nella lettura dei chilometri: " + ex.getMessage());
        }
        return -1;
    }
}