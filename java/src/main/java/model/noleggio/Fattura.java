package model.noleggio;

import exception.FormatoNonValidoException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Fattura {
    private Noleggio noleggio;
    /* ---------- FORMATTER DI DATA ---------- */
    // Utilizziamo lo stesso standard dd/MM/yyyy per il formato GG/MM/AAAA
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /* ---------- ATTRIBUTI DI CLASSE ---------- */
    private double nFattura;
    private LocalDate dataEmissione; // Cambiato da String a LocalDate
    private float importoBase;
    private float importoPenali;
    private float importoTotale;
    private String metodoPagamento;
    private boolean stato;

    /* ---------- COSTRUTTORE ---------- */
    public Fattura(double nFattura, String dataEmissioneStr, float importoBase, float importoPenali,
                   String metodoPagamento, boolean stato,Noleggio noleggio) throws FormatoNonValidoException {

        this.nFattura = nFattura;
        this.dataEmissione = parsingData(dataEmissioneStr);
        this.importoBase = importoBase;
        this.importoPenali = importoPenali;
        this.importoTotale = 0;
        this.metodoPagamento = metodoPagamento;
        this.stato = stato;
        this.noleggio = noleggio;
    }

    /* ---------- METODO PRIVATO DI PARSING E VALIDAZIONE ---------- */
    private LocalDate parsingData(String dataStr) throws FormatoNonValidoException {
        if (dataStr == null || dataStr.trim().isEmpty()) {
            throw new FormatoNonValidoException("La data di emissione non può essere vuota.");
        }
        try {
            // Se la stringa è malformata o la data non è reale (es. 30/02/2026), lancia un'eccezione
            return LocalDate.parse(dataStr, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new FormatoNonValidoException("La data '" + dataStr + "' non è valida. Usa il formato GG/MM/AAAA.");
        }
    }

    /* ---------- METODI GETTER / SETTER ---------- */
    public double getnFattura() {
        return nFattura;
    }
    public void setnFattura(double nFattura) {
        this.nFattura = nFattura;
    }

    // Il getter restituisce automaticamente la stringa formattata correttamente
    public String getDataEmissione() {
        return dataEmissione != null ? dataEmissione.format(FORMATTER) : null;
    }
    // Il setter riceve una stringa, la valida e aggiorna l'oggetto LocalDate interno
    public void setDataEmissione(String dataEmissione) throws FormatoNonValidoException {
        this.dataEmissione = parsingData(dataEmissione);
    }

    public float getImportoBase() {
        return importoBase;
    }
    public void setImportoBase(float importoBase) {
        this.importoBase = importoBase;
    }

    public float getImportoPenali() {
        return importoPenali;
    }
    public void setImportoPenali(float importoPenali) {
        this.importoPenali = importoPenali;
    }

    public float getImportoTotale() {
        return importoTotale;
    }
    public void calcolaImportoTotale(float importoBase, float importoPenali) {
        this.importoTotale = importoBase + importoPenali;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }
    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public boolean isStato() {
        return stato;
    }
    public void setStato(boolean stato) {
        this.stato = stato;
    }

}