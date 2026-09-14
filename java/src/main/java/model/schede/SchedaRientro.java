package model.schede;

import java.time.LocalDate;

public class SchedaRientro extends SchedaVeicolo {
    /* ---------- ATTRIBUTI DI CLASSE ---------- */
    private String nuoviDanni;
    private double chilometriIniziali; // Tiene traccia dei km che aveva il veicolo all'uscita

    /* ---------- COSTRUTTORE ---------- */
    public SchedaRientro(LocalDate dataRivelazione, double chilometriSegnatiRientro, float livelloCarburante,
                         String noteDanni, String nuoviDanni, double chilometriIniziali) {

        // Passiamo i dati alla classe madre SchedaVeicolo
        super(dataRivelazione, chilometriSegnatiRientro, livelloCarburante, noteDanni);

        // Controllo di sicurezza: non puoi rientrare con meno km di quando sei partito!
        if (chilometriSegnatiRientro < chilometriIniziali) {
            throw new IllegalArgumentException("Errore: I chilometri di rientro (" + chilometriSegnatiRientro +
                    ") non possono essere inferiori a quelli iniziali (" + chilometriIniziali + ").");
        }

        this.nuoviDanni = nuoviDanni;
        this.chilometriIniziali = chilometriIniziali;
    }

    /* ---------- METODI GETTER / SETTER ---------- */
    public String getNuoviDanni() {
        return nuoviDanni;
    }
    public void setNuoviDanni(String nuoviDanni) {
        this.nuoviDanni = nuoviDanni;
    }

    public double getChilometriIniziali() {
        return chilometriIniziali;
    }
    public void setChilometriIniziali(double chilometriIniziali) {
        this.chilometriIniziali = chilometriIniziali;
    }

    /* -------------- METODI AGGIUNTIVI ------------ */
    /**
     * Calcola i chilometri effettivi percorsi durante il periodo di noleggio.
     * @return differenza tra i chilometri attuali di rientro e quelli iniziali
     */
    public double calcolaChilometriPercorsi() {
        // getChilometriSegnati() viene ereditato da SchedaVeicolo ed è il valore attuale al rientro
        return this.getChilometriSegnati() - this.chilometriIniziali;
    }
}