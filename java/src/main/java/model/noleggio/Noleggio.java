package model.noleggio;

import exception.FormatoNonValidoException;
import model.utenti.Cliente;
import model.utenti.Operatore;
import model.veicoli.Veicolo;
import model.schede.SchedaRientro;
import model.schede.SchedaUscita;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Noleggio {
    /* ---------- FORMSTATTER DI DATA ---------- */
    // Definiamo il formato standard GG/MM/AAAA (in Java si scrive dd/MM/yyyy)
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /* ---------- ATTRIBUTI DI CLASSE ---------- */
    private int idNoleggio;
    private LocalDate dataInizio; // Cambiato in LocalDate
    private LocalDate dataFinePrevista; // Cambiato in LocalDate
    private LocalDate dataRiconsegnaEffettiva; // Cambiato in LocalDate
    private String statoNoleggio;

    private Veicolo veicolo;
    private SchedaUscita schedaUscitaNoleggio;
    private SchedaRientro schedaRientroNoleggio;
    private Cliente cliente;
    private ArrayList<Operatore> listaOperatori;

    /* ---------- COSTRUTTORE ---------- */
    public Noleggio(int idNoleggio, String dataInizioStr, String dataFinePrevistaStr,
                    String dataRiconsegnaEffettivaStr, SchedaRientro schedaRientroNoleggio,
                    SchedaUscita schedaUscitaNoleggio, Cliente cliente) throws FormatoNonValidoException {

        this.idNoleggio = idNoleggio;

        // Convertiamo e validiamo le stringhe in oggetti LocalDate
        this.dataInizio = parsingData(dataInizioStr);
        this.dataFinePrevista = parsingData(dataFinePrevistaStr);
        this.dataRiconsegnaEffettiva = parsingData(dataRiconsegnaEffettivaStr);

        this.schedaRientroNoleggio = schedaRientroNoleggio;
        this.schedaUscitaNoleggio = schedaUscitaNoleggio;
        this.cliente = cliente;
        this.listaOperatori = new ArrayList<>();
    }

    /* ---------- METODO PRIVATO DI PARSING E VALIDAZIONE ---------- */
    // Questo metodo fa tutto il lavoro sporco in modo ultra-efficiente
    private LocalDate parsingData(String dataStr) throws FormatoNonValidoException {
        if (dataStr == null || dataStr.trim().isEmpty()) {
            return null; // Utile per dataRiconsegnaEffettiva che all'inizio può essere vuota
        }
        try {
            // LocalDate.parse lancia una DateTimeParseException se il formato è errato
            // o se la data non esiste (es. 31/02/2026)
            return LocalDate.parse(dataStr, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new FormatoNonValidoException("La data '" + dataStr + "' non è valida. Usa il formato GG/MM/AAAA.");
        }
    }

    /* ---------- METODI GETTER / SETTER ---------- */
    public int getIdNoleggio() {
        return idNoleggio;
    }
    public void setIdNoleggio(int idNoleggio) {
        this.idNoleggio = idNoleggio;
    }

    // Il getter restituisce la String nel formato corretto quando viene richiesta all'esterno
    public String getDataInizio() {
        return dataInizio != null ? dataInizio.format(FORMATTER) : null;
    }
    // Il setter accetta la stringa, la valida e aggiorna l'oggetto LocalDate
    public void setDataInizio(String dataInizio) throws FormatoNonValidoException {
        this.dataInizio = parsingData(dataInizio);
    }
    public String getDataFinePrevista() {
        return dataFinePrevista != null ? dataFinePrevista.format(FORMATTER) : null;
    }
    public void setDataFinePrevista(String dataFinePrevista) throws FormatoNonValidoException {
        this.dataFinePrevista = parsingData(dataFinePrevista);
    }
    public String getDataRiconsegnaEffettiva() {
        return dataRiconsegnaEffettiva != null ? dataRiconsegnaEffettiva.format(FORMATTER) : null;
    }
    public void setDataRiconsegnaEffettiva(String dataRiconsegnaEffettiva) throws FormatoNonValidoException {
        this.dataRiconsegnaEffettiva = parsingData(dataRiconsegnaEffettiva);
    }
    public SchedaUscita getSchedaUscitaNoleggio() { return schedaUscitaNoleggio; }
    public void setSchedaUscitaNoleggio(SchedaUscita schedaUscitaNoleggio) { this.schedaUscitaNoleggio = schedaUscitaNoleggio; }
    public SchedaRientro getSchedaRientroNoleggio() { return schedaRientroNoleggio; }
    public void setSchedaRientroNoleggio(SchedaRientro schedaRientroNoleggio) { this.schedaRientroNoleggio = schedaRientroNoleggio; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Veicolo getVeicolo() { return veicolo; }
    public void setVeicolo(Veicolo veicolo) { this.veicolo = veicolo; }

    public ArrayList<Operatore> getListaOperatori() { return listaOperatori; }
    public void setListaOperatori(ArrayList<Operatore> listaOperatori) { this.listaOperatori = listaOperatori; }
    public void addOperatore(Operatore o){ listaOperatori.add(o); }
    public void rimuoviOperatore(Operatore o){ listaOperatori.remove(o); }

    /* ---------------METODI AGGIUNTIVI--------------------*/
    //calcoliamo i giorni previsti del noleggio
    //utilizziamo una classe apposita chiamata chrono unit che aiuta a fare operazioni sulle date
    public long calcolaGiorniPrevistiNoleggio(LocalDate dataInizio, LocalDate dataFinePrevista){
        long giorniNoleggioPrevisti = ChronoUnit.DAYS.between(dataInizio, dataFinePrevista);

        return giorniNoleggioPrevisti;
    }
    //anche qui calcoliamo i giorni del noleggio effettivi, quindi quando è stato consegnato effettivamente il veicolo
    public long calcolaGiorniEffettiviNoleggio(LocalDate dataInizio, LocalDate dataRiconsegnaEffettiva){
        long giorniNoleggioEffettivi = ChronoUnit.DAYS.between(dataInizio, dataRiconsegnaEffettiva);

        return giorniNoleggioEffettivi;
    }
    // Verifica se il noleggio è in ritardo
    public boolean isInRitardo(LocalDate dataFinePrevista, LocalDate dataRiconsegnaEffettiva) {
        if (dataRiconsegnaEffettiva != null) {
            // Caso 1: È già stato riconsegnato. Verifichiamo se ha sforato
            return dataRiconsegnaEffettiva.isAfter(dataFinePrevista);
        } else {
            // Caso 2: Non ancora riconsegnato. Verifichiamo se oggi ha superato la scadenza
            return LocalDate.now().isAfter(dataFinePrevista);
        }
    }

    // Determina lo stato del noleggio dinamicamente in base alla data di oggi
    public String determinaStatoNoleggio() {
        LocalDate oggi = LocalDate.now();

        if (this.dataRiconsegnaEffettiva != null) {
            return "Concluso";
        } else if (oggi.isBefore(this.dataInizio)) {
            return "In Programma";
        } else if (oggi.isAfter(this.dataFinePrevista)) {
            return "In Ritardo";
        } else {
            return "In Corso";
        }
    }
}