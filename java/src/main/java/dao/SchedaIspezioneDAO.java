package dao;

import java.time.LocalDate;

public interface SchedaIspezioneDAO {

    // FIRMA DEL METODO PER REGISTRARE L' USCITA DI UN NOLEGGIO SUL DB
    boolean registraUscitaDB(int idNoleggio, LocalDate dataUscita, double kmUscita, float carbUscita, boolean lavaggio, String note);

    // FIRMA DEL METODO PER REGISTRARE IL RIENTRO DI UN NOLEGGIO SUL DB
    boolean registraRientroDB(int idNoleggio, LocalDate dataRientro, double kmRientro, float carbRientro, String nuoviDanni);
}