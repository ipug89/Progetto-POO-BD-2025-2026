package model.schede;

import java.time.LocalDate;

public class SchedaUscita extends SchedaVeicolo{
    /* ---------- ATTRUBUTI DI CLASSE ---------- */
    private boolean lavaggio;

    /* ---------- COSTRUTTORE ---------- */
    public SchedaUscita(LocalDate dataRivelazione, double chilometriSegnati, float livelloCarburante, String noteDanni){
        super(dataRivelazione, chilometriSegnati, livelloCarburante, noteDanni);
        this.lavaggio = false;
    }

    /* ---------- METODI GETTER / SETTER ---------- */

    public boolean isLavata() {
        return lavaggio;
    }
    public void registraLavaggio() {
        lavaggio = true;
    }

}
