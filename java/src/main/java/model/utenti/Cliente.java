package model.utenti;

import exception.FormatoNonValidoException;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.zip.DataFormatException;

public class Cliente extends Utente {
    /* ---------- ATTRIBUTI DI CLASSE ---------- */
    private String numeroPatente;
    private LocalDate dataScadenzaPatente; // Cambiato in LocalDate per fare i calcoli
    private String categoriaPatente;       // Cambiato in String (es. "B", "A", ecc.)
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /* ---------- COSTRUTTORE ---------- */
    public Cliente(String numeroPatente, String dataScadenzaPatenteStr, String categoriaPatente,
                   String nome, String cognome, String codiceFiscale, String email, String telefono) throws FormatoNonValidoException {
        super(nome, cognome, codiceFiscale, email, telefono);
        this.numeroPatente = numeroPatente;
        this.categoriaPatente = categoriaPatente;
        try{
            // Convertiamo la stringa ricevuta nel costruttore in un oggetto LocalDate
            this.dataScadenzaPatente = LocalDate.parse(dataScadenzaPatenteStr, FORMATTER);

        }catch (java.time.format.DateTimeParseException e){
            throw new FormatoNonValidoException("La data inserita non è del formato corretto. Inserire la data nel seguente formato: GG/MM/AAAA");
        }
    }

    /* ---------- METODI GETTER / SETTER ---------- */
    public String getNumeroPatente() {
        return numeroPatente;
    }
    public void setNumeroPatente(String numeroPatente) {
        this.numeroPatente = numeroPatente;
    }

    // Restituisce la data già formattata come Stringa (comodo per le viste/stampe)
    public String getDataScadenzaPatenteFormattata() {
        return dataScadenzaPatente.format(FORMATTER);
    }

    // Restituisce l'oggetto LocalDate se ti serve per altri calcoli
    public LocalDate getDataScadenzaPatente() {
        return dataScadenzaPatente;
    }

    // Accetta una stringa (es. "25/12/2030") e la converte prima di salvarla
    public void setDataScadenzaPatente(String dataScadenzaPatenteStr) {
        this.dataScadenzaPatente = LocalDate.parse(dataScadenzaPatenteStr, FORMATTER);
    }

    public String getCategoriaPatente() {
        return categoriaPatente;
    }
    public void setCategoriaPatente(String categoriaPatente) {
        this.categoriaPatente = categoriaPatente;
    }

}