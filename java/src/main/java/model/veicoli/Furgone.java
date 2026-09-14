package model.veicoli;

public class Furgone extends Veicolo {
    /* ---------- ATTRIBUTI DI CLASSE ---------- */
    private int numeroPosti;
    private int numeroPorte;
    private double capacitaCaricoKg;
    private double volumeCaricoM3;
    private double altezzaMetri;

    /* ---------- COSTRUTTORE ---------- */
    public Furgone(String targa, String marca, String modello, double chilometraggioAttuale, double tariffaGiornaliera,
                   int numeroPosti, int numeroPorte, double capacitaCaricoKg, double volumeCaricoM3, double altezzaMetri) {
        super(targa, marca, modello, chilometraggioAttuale, tariffaGiornaliera);

        this.numeroPosti = numeroPosti;
        this.numeroPorte = numeroPorte;
        this.capacitaCaricoKg = capacitaCaricoKg;
        this.volumeCaricoM3 = volumeCaricoM3;
        this.altezzaMetri = altezzaMetri;
    }

    /* ---------- METODI GETTER / SETTER ---------- */
    public int getNumeroPosti() {
        return numeroPosti;
    }

    public void setNumeroPosti(int numeroPosti) {
        this.numeroPosti = numeroPosti;
    }

    public int getNumeroPorte() {
        return numeroPorte;
    }

    public void setNumeroPorte(int numeroPorte) {
        this.numeroPorte = numeroPorte;
    }

    public double getCapacitaCaricoKg() {
        return capacitaCaricoKg;
    }

    public void setCapacitaCaricoKg(double capacitaCaricoKg) {
        this.capacitaCaricoKg = capacitaCaricoKg;
    }

    public double getVolumeCaricoM3() {
        return volumeCaricoM3;
    }

    public void setVolumeCaricoM3(double volumeCaricoM3) {
        this.volumeCaricoM3 = volumeCaricoM3;
    }

    public double getAltezzaMetri() {
        return altezzaMetri;
    }

    public void setAltezzaMetri(double altezzaMetri) {
        this.altezzaMetri = altezzaMetri;
    }
}