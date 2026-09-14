package dao;

import java.time.LocalDate;
import java.util.ArrayList;

public interface NoleggioDAO {

    // FIRMA DEL METODO PER REGISTRARE UN NOLEGGIO SUL DB
    int registraNoleggioDB(LocalDate dataInizio, LocalDate dataFinePrevista, String stato,
                           String numeroPatente, String targa, String matricolaOperatore);

    // FIRMA DEL METODO PER LA RESTITUZIONE DELLA LISTA DEI NOLEGGI SUL DB
    void getListaNoleggiDB(ArrayList<String> id, ArrayList<String> inizio, ArrayList<String> fine,
                           ArrayList<String> riconsegna, ArrayList<String> stato, ArrayList<String> patente,
                           ArrayList<String> targa, ArrayList<Integer> posti, ArrayList<Integer> porte,
                           ArrayList<String> matricola);

    // FIRMA DEL METODO PER CHIUDERE UN NOLEGGIO SUL DB
    boolean chiudiNoleggioDB(int idNoleggio, LocalDate dataRientro);
}