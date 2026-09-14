package model.schede;

import java.time.LocalDate;

public class SchedaVeicolo {
    /* ---------- ATTRIBUTI DI CLASSE ---------- */
    private LocalDate dataRivelazione;
    private double chilometriSegnati;
    private float livelloCarburante;
    private String noteDanni;

    /* ---------- COSTRUTTORE ---------- */
    public SchedaVeicolo(LocalDate dataRivelazione, double chilometriSegnati, float livelloCarburante, String noteDanni) {
        this.dataRivelazione = dataRivelazione;
        this.chilometriSegnati = chilometriSegnati;
        this.livelloCarburante = livelloCarburante;
        this.noteDanni = noteDanni;
    }

    /* ---------- METODI GETTER / SETTER ---------- */
    public LocalDate getDataRivelazione() {
        return this.dataRivelazione;
    }

    public void setDataRivelazione(LocalDate dataRivelazione) {
        this.dataRivelazione = dataRivelazione;
    }

    public double getChilometriSegnati() {
        return this.chilometriSegnati;
    }

    public void setChilometriSegnati(double chilometriSegnati) {
        this.chilometriSegnati = chilometriSegnati;
    }

    public float getLivelloCarburante() {
        return this.livelloCarburante;
    }

    public void setLivelloCarburante(float livelloCarburante) {
        this.livelloCarburante = livelloCarburante;
    }

    public String getNoteDanni() {
        return this.noteDanni;
    }

    public void setNoteDanni(String noteDanni) {
        this.noteDanni = noteDanni;
    }

}