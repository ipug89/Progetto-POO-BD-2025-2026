package model.veicoli;

public class Auto extends Veicolo
{
    /* ---------- ATTRUBUTI DI CLASSE ---------- */
    private int numeroPosti;
    private int numeroPorte;
    private String categoria;

    /* ---------- COSTRUTTORE ---------- */
    public Auto(String targa, String marca, String modello, double chilometraggioAttuale,double tariffaGiornaliera,
                int numeroPosti, int numeroPorte, String categoria)
    {
        super(targa, marca, modello, chilometraggioAttuale, tariffaGiornaliera);

        this.numeroPosti = numeroPosti;
        this.numeroPorte = numeroPorte;
        this.categoria = categoria;
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
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
