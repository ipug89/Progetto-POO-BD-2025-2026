package dao;

import java.time.LocalDate;

public interface ClienteDAO {

    // FIRMA DEL METODO PER VERIFICARE L'ESISTENZA DEL CLIENTE SUL DB TRAMITE CF
    public boolean verificaPatenteDB(String codiceFiscale);

    // FIRMA DEL METODO PER REGISTRARE UN CLIENTE SUL DB
    public boolean registraClienteDB(String numeroPatente, LocalDate dataScadenza, String categoriaPatente,
                                     String nome, String cognome, String codiceFiscale, String email, String telefono);

    // FIRMA DEL METODO PER AGGIORNARE I DATI DEL CLIENTE
    public boolean aggiornaClienteDB(String numeroPatente, LocalDate dataScadenza, String categoriaPatente,
                                     String nome, String cognome, String codiceFiscale, String email, String telefono);
}
