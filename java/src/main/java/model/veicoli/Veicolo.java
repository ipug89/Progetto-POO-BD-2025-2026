package model.veicoli;

import model.noleggio.Noleggio;

import java.util.ArrayList;

public class Veicolo
{
    /* ---------- ATTRUBUTI DI CLASSE ---------- */
    private String targa;
    private String marca;
    private String modello;
    private double chilometraggioAttuale;
    private boolean statoVeicolo;
    private double tariffaGiornaliera;
    private ArrayList<Noleggio> listaNoleggi;

    /* ------------- COSTRUTTORE ------------- */
    public Veicolo(String targa, String marca, String modello, double chilometraggioAttuale, double tariffaGiornaliera) {
        this.targa = targa;
        this.marca = marca;
        this.modello = modello;
        this.chilometraggioAttuale = chilometraggioAttuale;
        this.statoVeicolo = false;
        this.tariffaGiornaliera = tariffaGiornaliera;
    }

    /* ---------- METODI GETTER / SETTER ---------- */
    public String getTarga() {
        return targa;
    }
    public void setTarga(String targa) {
        this.targa = targa;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getModello() {
        return modello;
    }
    public void setModello(String modello) {
        this.modello = modello;
    }
    public double getChilometraggioAttuale() {
        return chilometraggioAttuale;
    }
    public void setChilometraggioAttuale(double chilometraggioAttuale) {
        this.chilometraggioAttuale = chilometraggioAttuale;
    }
    public boolean isStato() {
        return statoVeicolo;
    }
    public void setStato(boolean stato) {
        this.statoVeicolo = stato;
    }
    public double getTariffaGiornaliera() {
        return tariffaGiornaliera;
    }
    public void setTariffaGiornaliera(double tariffaGiornaliera) {
        this.tariffaGiornaliera = tariffaGiornaliera;
    }
    @Override
    public String toString() {
        // Scegli il formato che preferisci vedere all'interno della JList
        return marca + " " + modello + " [" + targa + "]";
    }
}