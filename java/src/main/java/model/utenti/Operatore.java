package model.utenti;

public class Operatore extends Utente
{
    /* ---------- ATTRUBUTI DI CLASSE ---------- */
    private String matricola;

    /* ---------- COSTRUTTORE ---------- */
    public Operatore(String matricola, String nome, String cognome, String codiceFiscale, String email, String telefono)
    {
        super(nome, cognome, codiceFiscale, email, telefono);
        this.matricola = matricola;
    }

    /* ---------- METODI GETTER / SETTER ---------- */
    public String getMatricola() {
        return matricola;
    }
    public void setMatricola(String matricolaDipendente) {
        this.matricola = matricolaDipendente;
    }

    // Override del metodo toString
    @Override
    public String toString() {
        // Usiamo super.toString() se la classe Utente ha già un suo toString,
        // oppure accediamo ai getter della classe Utente (es. getNome(), getCognome())
        return "OPERATORE -> " + getNome() + " " + getCognome() +
                " E-mail: " + getEmail() +
                " | Tel: " + getTelefono() +
                " | [MATRICOLA: " + matricola + "]";
    }
}

