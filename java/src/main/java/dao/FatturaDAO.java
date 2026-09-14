package dao;

import java.time.LocalDate;

public interface FatturaDAO {

    // FIRMA DEL METODO PER EMETTERE LA FATTURA SUL DB
    public boolean emettiFatturaDB(int idNoleggio, LocalDate dataEmissione, double importoBase, double importoPenali, double importoTotale, String metodoPagamento);

    // FIRMA DEL METODO PER PRELEVARE I DATI DELLA FATTURA DI UN NOLEGGIO SUL DB
    public String[] getDatiFatturaPerGUI(int idNoleggio);
}