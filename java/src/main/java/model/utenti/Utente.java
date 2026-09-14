package model.utenti;

public class Utente
{
    /* ---------- ATTRUBUTI DI CLASSE ---------- */
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String email;
    private String telefono;

    /* ---------- COSTRUTTORE ---------- */
    public Utente(String nome, String cognome, String codiceFiscale, String email, String telefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.telefono = telefono;
    }

    /* ---------- METODI GETTER / SETTER ---------- */
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public String getCodiceFiscale()
    {
        return codiceFiscale;
    }
    public void setCodiceFiscale(String codiceFiscale)
    {
        this.codiceFiscale = codiceFiscale;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
