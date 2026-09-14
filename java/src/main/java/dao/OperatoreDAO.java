package dao;

import java.sql.SQLException;

public interface OperatoreDAO {

    // FIRMA DEL METODO PER LA VERIFICA DELLA PRESENZA DI UNA MATRICOLA SUL DB
    public boolean verificaMatricolaDB(String matricola);

    // FIRMA DEL METODO PER LA REGISTRAZIONE DI UN NUOVO OPERATORE SUL DB
    public boolean registaOperatoreDB(String matricola, String nome, String cognome,
                                      String codiceFiscale, String email, String telefono);
}
