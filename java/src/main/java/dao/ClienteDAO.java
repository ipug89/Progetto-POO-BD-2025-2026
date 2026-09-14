package dao;

import java.time.LocalDate;

public interface ClienteDAO {

    // FIRMA DEL METODO PER VERIFICARE LA PATENTE SUL DB
    public boolean verificaPatenteDB(String numeroPatente);

    // FIRMA DEL METODO PER REGISTRARE UN CLIENTE SUL DB
    public boolean registraClienteDB(String numeroPatente, LocalDate dataScadenza, String categoriaPatente,
                              String nome, String cognome, String codiceFiscale, String email, String telefono);
}
