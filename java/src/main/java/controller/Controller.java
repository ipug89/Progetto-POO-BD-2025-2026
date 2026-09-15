package controller;

import dao.*;
import exception.CampoNonPresente;
import exception.FormatoNonValidoException;
import implementazioniPostgresDAO.*;
import model.noleggio.Noleggio;
import model.utenti.Cliente;
import model.utenti.Operatore;
import model.veicoli.Auto;
import model.veicoli.Furgone;
import model.veicoli.Veicolo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

public class Controller {

    private ArrayList<Operatore> listaOperatori;

    public Controller() {
        this.listaOperatori = new ArrayList<>();
    }

    // METODO PER PRELEVARE LE AUTO DISPONIBILI AL NOLEGGIO
    public ArrayList<Auto> getAutoDisponibili() {
        ArrayList<String> targhe = new ArrayList<>();
        ArrayList<String> marche = new ArrayList<>();
        ArrayList<String> modelli = new ArrayList<>();
        ArrayList<Double> chilometri = new ArrayList<>();
        ArrayList<Double> tariffe = new ArrayList<>();
        ArrayList<Integer> posti = new ArrayList<>();
        ArrayList<Integer> porte = new ArrayList<>();
        ArrayList<String> categorie = new ArrayList<>();

        VeicoloDAO veicoloDAO = new VeicoloImplementazionePostgresDAO();
        veicoloDAO.getAutoDisponibiliDB(targhe, marche, modelli, chilometri, tariffe, posti, porte, categorie);

        ArrayList<Auto> listaAutoDisponibili = new ArrayList<>();

        for (int i = 0; i < targhe.size(); i++) {
            Auto a = new Auto(
                    targhe.get(i),
                    marche.get(i),
                    modelli.get(i),
                    chilometri.get(i),
                    tariffe.get(i),
                    posti.get(i),
                    porte.get(i),
                    categorie.get(i)
            );
            listaAutoDisponibili.add(a);
        }

        return listaAutoDisponibili;
    }

    // METODO PER PRELEVARE I FURGONI DISPONIBILI AL NOLEGGIO
    public ArrayList<Furgone> getFurgoniDisponibili() {
        ArrayList<String> targhe = new ArrayList<>();
        ArrayList<String> marche = new ArrayList<>();
        ArrayList<String> modelli = new ArrayList<>();
        ArrayList<Double> chilometri = new ArrayList<>();
        ArrayList<Double> tariffe = new ArrayList<>();
        ArrayList<Integer> posti = new ArrayList<>();
        ArrayList<Integer> porte = new ArrayList<>();
        ArrayList<Double> capacita = new ArrayList<>();
        ArrayList<Double> volumi = new ArrayList<>();
        ArrayList<Double> altezze = new ArrayList<>();

        VeicoloDAO veicoloDAO = new VeicoloImplementazionePostgresDAO();
        veicoloDAO.getFurgoniDisponibiliDB(targhe, marche, modelli, chilometri, tariffe, posti, porte, capacita, volumi, altezze);

        ArrayList<Furgone> listaFiltrata = new ArrayList<>();

        for (int i = 0; i < targhe.size(); i++) {
            Furgone f = new Furgone(
                    targhe.get(i),
                    marche.get(i),
                    modelli.get(i),
                    chilometri.get(i),
                    tariffe.get(i),
                    posti.get(i),
                    porte.get(i),
                    capacita.get(i),
                    volumi.get(i),
                    altezze.get(i)
            );
            listaFiltrata.add(f);
        }

        return listaFiltrata;
    }

    // METODO PER INSTANZIARE UN AUTO
    public boolean aggiungiAuto(String targa, String marca, String modello, double km, double tariffaGg,
                                int numeroPosti, int numeroPorte, String categoria) throws CampoNonPresente, FormatoNonValidoException {

        // Chiamata al metodo per controllare la validità dei campi
        controllaCampi("Auto", targa, marca, modello, tariffaGg, km);

        if (numeroPosti <= 0 || numeroPorte <= 0) {
            throw new FormatoNonValidoException("Numero posti e porte devono essere maggiori di zero.");
        }

        VeicoloDAO veicoloDAO = new VeicoloImplementazionePostgresDAO();
        return veicoloDAO.registraAuto(targa, marca, modello, km, tariffaGg, numeroPosti, numeroPorte, categoria);
    }

    // METODO PER INSTANZIARE UN FURGONE
    public boolean aggiungiFurgone(String targa, String marca, String modello, double km, double tariffaGg,
                                   int nPosti, int nPorte, double capacitaM3, double volume, double altezza) throws CampoNonPresente, FormatoNonValidoException {

        // Chiamata al metodo per controllare la validità dei campi
        controllaCampi("Furgone", targa, marca, modello, tariffaGg, km);

        if (nPosti <= 0 || nPorte <= 0) {
            throw new FormatoNonValidoException("Numero posti e porte devono essere maggiori di zero.");
        }
        if (capacitaM3 <= 0 || volume <= 0 || altezza <= 0) {
            throw new FormatoNonValidoException("Capacità, volume e altezza devono essere valori positivi.");
        }

        VeicoloDAO veicoloDAO = new VeicoloImplementazionePostgresDAO();
        return veicoloDAO.registraFurgone(targa, marca, modello, km, tariffaGg, nPosti, nPorte, capacitaM3, volume, altezza);
    }

    // METODO PER CONTROLLARE LA VALIDITA' DEI CAMPI
    private void controllaCampi(String tipologia, String targa, String marca, String modello, double tariffa, double km) throws CampoNonPresente, FormatoNonValidoException {

        if (tipologia == null || tipologia.isBlank())
            throw new CampoNonPresente("Il campo tipologia non è presente");

        if (targa == null || targa.isBlank())
            throw new CampoNonPresente("Il campo targa non è presente");

        // Gestione del formato della targa (EX. AB123CD)
        String caratteriTarga = "^[A-Za-z]{2}\\d{3}[A-Za-z]{2}$";
        if (!targa.matches(caratteriTarga))
            throw new FormatoNonValidoException("Il formato della targa non è valido. Usufruire del formato standard [AB123CD]");

        if (marca == null || marca.isBlank())
            throw new CampoNonPresente("Il campo marca non è presente");

        if (modello == null || modello.isBlank())
            throw new CampoNonPresente("Il campo modello non è presente");

        if (tariffa <= 0)
            throw new FormatoNonValidoException("Il campo tariffa non è valido");

        if (km < 0)
            throw new FormatoNonValidoException("Il formato dei chilometri non è valido, inserire un valore positivo");
    }

    // METODO PER CONTROLLARe LA VALIDITA' DELL'UTENTE
    private void controllaUtente(String nome, String cognome, String codiceFiscale, String email, String telefono) throws FormatoNonValidoException {

        if (nome == null || nome.trim().isEmpty() || cognome == null || cognome.trim().isEmpty()) {
            throw new FormatoNonValidoException("Nome e Cognome sono campi obbligatori.");
        }

        // Gestione del formato del Codice Fiscale (16 caratteri)
        String regexCF = "^[A-Z0-9]{16}$";
        if (codiceFiscale == null || !codiceFiscale.toUpperCase().matches(regexCF)) {
            throw new FormatoNonValidoException("Formato Codice Fiscale errato. Deve contenere esattamente 16 caratteri alfanumerici.");
        }

        // Gestione del formato dell e-mail (utente@dominio.com)
        if (email != null && !email.trim().isEmpty()) {
            String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,4}$";
            if (!email.matches(regexEmail)) {
                throw new FormatoNonValidoException("Formato Email non valido. Esempio corretto: utente@dominio.it");
            }
        }

        // Gestione del formato del telefono (9-11 numeri)
        if (telefono != null && !telefono.trim().isEmpty()) {
            String regexTelefono = "^[0-9]{9,11}$";
            if (!telefono.matches(regexTelefono)) {
                throw new FormatoNonValidoException("Formato Telefono non valido. Inserire solo numeri (es. 3471234567).");
            }
        }
    }

    // METODO PER REGISTRARE UN NUOVO OPERATORE
    public boolean registraNuovoOperatore(String matricola, String nome, String cognome, String codiceFiscale, String email, String telefono) throws FormatoNonValidoException {

        controllaUtente(nome, cognome, codiceFiscale, email, telefono);
        OperatoreDAO operatoreDAO = new OperatoreImplementazionePostgresDAO();

        // Verifica registrazione operatore e aggiunta alla lista degli operatori
        boolean successoDB = operatoreDAO.registaOperatoreDB(matricola, nome, cognome, codiceFiscale, email, telefono);
        if (successoDB) {
            Operatore nuovoOp = new Operatore(matricola, nome, cognome, codiceFiscale, email, telefono);
            listaOperatori.add(nuovoOp);
            return true;
        }
        return false;
    }

    // METODO PER REGISTRARE UN NUOVO CLIENTE
    public boolean registraNuovoCliente(String numeroPatente, String scadenzaPatente, String categoria, String nome, String cognome, String codiceFiscale, String email, String telefono) throws FormatoNonValidoException {

        controllaUtente(nome, cognome, codiceFiscale, email, telefono);
        ClienteDAO clienteDAO = new ClienteImplementazionePostgresDAO();

        try {
            // Inizializzazione del formato della data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataConvertita = LocalDate.parse(scadenzaPatente, formatter);

            // Verifica registrazione cliente e aggiunta alla lista dei clienti
            boolean successoDB = clienteDAO.registraClienteDB(numeroPatente, dataConvertita, categoria, nome, cognome, codiceFiscale, email, telefono);
            if (successoDB) {
                Cliente nuovoCliente = new Cliente(numeroPatente, scadenzaPatente, categoria, nome, cognome, codiceFiscale, email, telefono);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Errore durante il salvataggio del cliente: " + e.getMessage());
        }

        return false;
    }

    // METODO PER VERIFICARE LA VALIDITA' DELLA PATENTE
    public boolean verificaPatente(String patente) {
        ClienteDAO clienteDAO = new ClienteImplementazionePostgresDAO();
        return clienteDAO.verificaPatenteDB(patente);
    }

    // METODO PER VERIFICARE L'INSERIMENTO DI UNA MATRICOLA ESISTENTE
    public boolean verificaMatricola(String matricola) {
        OperatoreDAO operatoreDAO = new OperatoreImplementazionePostgresDAO();

        // Verifica dell'esistenza della matricola nel DB
        boolean esiste = operatoreDAO.verificaMatricolaDB(matricola);
        if (esiste) {
            Operatore operatoreLoggato = new Operatore(matricola, "Operatore", "Attuale", "CF_GENERICO", "email@test.com", "000000");

            listaOperatori.clear();
            listaOperatori.add(operatoreLoggato);
        }

        return esiste;
    }

    // METODO PER LA GENERAZIONE CASUALE DELLA MATRICOLA
    public String generaMatricolaCasuale() {
        Random random = new Random();
        String nuovaMatricola;
        do {
            int numero = 10000 + random.nextInt(90000);
            nuovaMatricola = String.valueOf(numero);
        } while (verificaMatricola(nuovaMatricola));

        return nuovaMatricola;
    }

    // METODO PER REGISTRARE UN NUOVO NOLEGGIO
    public boolean registraNuovoNoleggio(Noleggio nuovoNoleggio) throws FormatoNonValidoException {

        // Definizione del formato della data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate dataInizioDB = LocalDate.parse(nuovoNoleggio.getDataInizio(), formatter);
        LocalDate dataFineDB = LocalDate.parse(nuovoNoleggio.getDataFinePrevista(), formatter);

        // Controllo della validità della data
        if (dataFineDB.isBefore(dataInizioDB)) {
            throw new FormatoNonValidoException("La data di fine prevista non può essere precedente alla data di inizio.");
        }

        // Acquisizione dati cliente e controllo della scadenza della patente
        Cliente cliente = nuovoNoleggio.getCliente();
        if (cliente != null) {
            LocalDate scadenzaPatente = cliente.getDataScadenzaPatente();
            if (scadenzaPatente != null && scadenzaPatente.isBefore(dataFineDB)) {
                throw new FormatoNonValidoException("Impossibile procedere: la patente scade il "
                        + scadenzaPatente.format(formatter)
                        + ", prima del termine del noleggio (" + dataFineDB.format(formatter) + ")!");
            }
        }

        NoleggioDAO noleggioDAO = new NoleggioImplementazionePostgresDAO();

        // Associazione veicolo e operatore al noleggio
        Veicolo veicoloScelto = nuovoNoleggio.getVeicolo();
        Operatore operatoreScelto = nuovoNoleggio.getListaOperatori().get(0);

        // Generazione dell'ID del noleggio
        int idGenerato = noleggioDAO.registraNoleggioDB(
                dataInizioDB,
                dataFineDB,
                "In Corso",
                nuovoNoleggio.getCliente().getNumeroPatente(),
                veicoloScelto.getTarga(),
                operatoreScelto.getMatricola()
        );

        // Verifica della corretta generazione dell ID
        if (idGenerato != -1) {
            nuovoNoleggio.setIdNoleggio(idGenerato);

            // Associazione e passaggio dello stato del veicolo in 'occupato'
            VeicoloDAO veicoloDAO = new VeicoloImplementazionePostgresDAO();
            veicoloDAO.impostaStatoVeicoloDB(veicoloScelto.getTarga(), true);

            // Creazione dell'istanza SchedaIspezione
            SchedaIspezioneDAO ispezioneDAO = new SchedaIspezioneImplementazionePostgresDAO();
            if (nuovoNoleggio.getSchedaUscitaNoleggio() != null) {
                ispezioneDAO.registraUscitaDB(
                        idGenerato,
                        dataInizioDB,
                        nuovoNoleggio.getSchedaUscitaNoleggio().getChilometriSegnati(),
                        nuovoNoleggio.getSchedaUscitaNoleggio().getLivelloCarburante(),
                        nuovoNoleggio.getSchedaUscitaNoleggio().isLavata(),
                        nuovoNoleggio.getSchedaUscitaNoleggio().getNoteDanni()
                );
            }

            System.out.println("Noleggio salvato con successo. ID generato: " + idGenerato);
            return true;
        }

        System.err.println("Impossibile salvare il noleggio nel DB.");
        return false;
    }

    //Metodo per l'acquisizione dei dati nel noleggio mediante gli ArrayList
    public ArrayList<Object[]> ottieniDatiNoleggi() {

        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> inizio = new ArrayList<>();
        ArrayList<String> fine = new ArrayList<>();
        ArrayList<String> riconsegna = new ArrayList<>();
        ArrayList<String> stato = new ArrayList<>();
        ArrayList<String> patente = new ArrayList<>();
        ArrayList<String> targa = new ArrayList<>();
        ArrayList<Integer> posti = new ArrayList<>();
        ArrayList<Integer> porte = new ArrayList<>();
        ArrayList<String> matricola = new ArrayList<>();

        // Creazione dell'istanza del DAO
        NoleggioDAO noleggioDAO = new NoleggioImplementazionePostgresDAO();
        // Invocazione del metodo per recuperare la lista dei noleggi dal DB
        noleggioDAO.getListaNoleggiDB(id, inizio, fine, riconsegna, stato, patente, targa, posti, porte, matricola);

        ArrayList<Object[]> righeTabella = new ArrayList<>();

        //Inizializzazione del formato della data
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate oggi = LocalDate.now();

        //Verifica dello stato del noleggio
        for (int i = 0; i < id.size(); i++) {
            String dataInizioStr = inizio.get(i);
            String dataFineStr = fine.get(i);
            String riconsegnaStr = riconsegna.get(i);

            String statoReale;

            //VERIFICA DELLO STATO DEL NOLEGGIO
            // 1.Controlla se lo stato non è 'In Corso'e che non vi siano spazi bianchi
            if (riconsegnaStr != null && !riconsegnaStr.equals("In Corso") && !riconsegnaStr.trim().isEmpty()) {
                // -Se tutte le condizioni sono soddisfatte, automaticamente lo stato è 'Concluso'
                statoReale = "Concluso";
            } else {
                //Assegnazione della data di inizio e della data di fine
                LocalDate dInizio = LocalDate.parse(dataInizioStr, fmt);
                LocalDate dFine = LocalDate.parse(dataFineStr, fmt);

                // 2.Controllo se lo stato è 'In Programma'
                if (oggi.isBefore(dInizio)) {
                    statoReale = "In Programma";
                // 3.Controllo se lo stato è 'In Ritardo'
                } else if (oggi.isAfter(dFine)) {
                    statoReale = "In Ritardo";
                // 4.Se tutte le altre condizioni sono false, impostiamo automaticamente lo stato a 'In Corso'
                } else {
                    statoReale = "In Corso";
                }
            }

            //Raggruppa tutti i dati in un singolo array Object
            Object[] singolaRiga = new Object[] {
                    id.get(i),
                    inizio.get(i),
                    fine.get(i),
                    riconsegnaStr,
                    statoReale,
                    patente.get(i),
                    targa.get(i),
                    posti.get(i),
                    porte.get(i),
                    matricola.get(i)
            };

            //Inserisce l' array Object alla lista principale (righeTabella)
            righeTabella.add(singolaRiga);
        }

        return righeTabella;
    }

    //METODO PER LA GESTIONE DEL CLIENTE
    public boolean gestisciCliente(Cliente clienteTemp) {

        // Creazione dell'istanza del DAO
        ClienteDAO clienteDAO = new ClienteImplementazionePostgresDAO();

        try {
            // Chiamata al metodo per controllare i campi inseriti dell'utente
            controllaUtente(clienteTemp.getNome(), clienteTemp.getCognome(), clienteTemp.getCodiceFiscale(), clienteTemp.getEmail(), clienteTemp.getTelefono());

            // Invocazione al metodo per verificare l'esistenza del cliente nel DB tramite Codice Fiscale
            boolean esistePerCF = clienteDAO.verificaPatenteDB(clienteTemp.getCodiceFiscale());
            LocalDate dataScadenza = clienteTemp.getDataScadenzaPatente();

            // VERIFICA ESISTENZA CLIENTE
            if (esistePerCF) {
                // Se il cliente esiste, stampa il messaggio e aggiorna i suoi dati
                System.out.println("Cliente trovato nel DB tramite Codice Fiscale. Procedo con l'aggiornamento dei dati e il noleggio.");

                return clienteDAO.aggiornaClienteDB(
                        clienteTemp.getNumeroPatente(),
                        dataScadenza,
                        clienteTemp.getCategoriaPatente(),
                        clienteTemp.getNome(),
                        clienteTemp.getCognome(),
                        clienteTemp.getCodiceFiscale(),
                        clienteTemp.getEmail(),
                        clienteTemp.getTelefono()
                );
            }

            // Altrimenti avvia la registrazione
            System.out.println("Cliente non trovato. Avvio registrazione...");

            // Registrazione del cliente sul DB
            return clienteDAO.registraClienteDB(
                    clienteTemp.getNumeroPatente(),
                    dataScadenza,
                    clienteTemp.getCategoriaPatente(),
                    clienteTemp.getNome(),
                    clienteTemp.getCognome(),
                    clienteTemp.getCodiceFiscale(),
                    clienteTemp.getEmail(),
                    clienteTemp.getTelefono()
            );
            // GESTIONE ERRORI
        } catch (FormatoNonValidoException ex) {
            System.err.println("Errore di validazione cliente: " + ex.getMessage());
            return false;
        }
    }

    // METODO PER REGISTRARE IL RIENTRO DI UN NOLEGGIO
    public boolean registraRientroNoleggio(int idNoleggio, String targaVeicolo, LocalDate dataRientro, double kmRientro, float carburanteRientro, String nuoviDanni) throws FormatoNonValidoException {

        // Creazione dell'istanza del DAO
        VeicoloDAO veicoloDAO = new VeicoloImplementazionePostgresDAO();
        // Invocazione del metodo per recuperare i chilometri iniziali del veicolo dal DB (tramite la targa)
        double kmAttuali = veicoloDAO.getChilometriVeicoloDB(targaVeicolo);

        // Controllo sull'inserimento dei kilometri
        if (kmRientro < kmAttuali) {
            throw new FormatoNonValidoException("I chilometri di rientro (" + kmRientro + ") non possono essere inferiori a quelli attuali (" + kmAttuali + ")!");
        }

        // Creazione dell'istanza del DAO
        SchedaIspezioneDAO ispezioneDAO = new SchedaIspezioneImplementazionePostgresDAO();
        // Invocazione al metodo per registrare il rientro sul DB
        boolean ispezioneOk = ispezioneDAO.registraRientroDB(idNoleggio, dataRientro, kmRientro, carburanteRientro, nuoviDanni);

        // Controllo che non vi siano stati errori nel corso delle istruzioni precedenti
        if (!ispezioneOk) {
            System.err.println("Errore: Impossibile aggiornare la scheda ispezione al rientro.");
            return false;
        }

        // Creazione dell'istanza del DAO
        NoleggioDAO noleggioDAO = new NoleggioImplementazionePostgresDAO();
        // Invocazione al metodo per chiudere il noleggio sul DB
        boolean noleggioOk = noleggioDAO.chiudiNoleggioDB(idNoleggio, dataRientro);

        // Controllo che non vi siano stati errori nel corso delle istruzioni precedenti
        if (!noleggioOk) {
            System.err.println("Errore: Impossibile chiudere la pratica di noleggio nel DB.");
            return false;
        }

        // Creazione dell'istanza del DAO
        veicoloDAO.aggiornaChilometriVeicoloDB(targaVeicolo, kmRientro);
        // Invocazione al metodo che imposta lo stato del veicolo sul DB
        veicoloDAO.impostaStatoVeicoloDB(targaVeicolo, false);

        // Messaggio informativo per l'utente
        System.out.println("Check-In completato. Il veicolo " + targaVeicolo + " è di nuovo disponibile!");
        return true;
    }

    // METODO PER GENERARE LA FATTURA
    public boolean generaFattura(int idNoleggio, String targa, String dataInizioStr, String dataFinePrevistaStr, LocalDate dataRientro, String metodoPagamento) {

        // Inizializzazione del formato della data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInizio = LocalDate.parse(dataInizioStr, formatter);
        LocalDate dataFinePrevista = LocalDate.parse(dataFinePrevistaStr, formatter);

        // Creazione dell' istanza del DAO
        VeicoloDAO veicoloDAO = new VeicoloImplementazionePostgresDAO();
        // Invocazione al metodo per acquisire la tariffa di un determinato veicolo (targa)
        double tariffaGiornaliera = veicoloDAO.getTariffaVeicoloDB(targa);

        // Calcolo della durata del noleggio (dataInizio - dataRientro)
        long giorniEffettivi = ChronoUnit.DAYS.between(dataInizio, dataRientro);

        // Noleggio durato un giorno
        if (giorniEffettivi <= 0) giorniEffettivi = 1;

        // Calcolo dell' importo base del noleggio
        double importoBase = giorniEffettivi * tariffaGiornaliera;
        double importoPenali = 0.0;

        // Controllo se il noleggio è stato consegnato dopo la data prevista
        if (dataRientro.isAfter(dataFinePrevista)) {
            long giorniRitardo = ChronoUnit.DAYS.between(dataFinePrevista, dataRientro);
            // Calcolo penali
            importoPenali = giorniRitardo * (tariffaGiornaliera * 2);
            System.out.println("Attenzione: Rilevati " + giorniRitardo + " giorni di ritardo. Penale applicata: " + importoPenali);
        }
        // Calcolo importo totale
        double importoTotale = importoBase + importoPenali;

        // Creazione dell'istanza del DAO
        FatturaDAO fatturaDAO = new FatturaImplementazionePostgresDAO();
        // Invocazione al metodo per emettere la fattura sul DB
        return fatturaDAO.emettiFatturaDB(idNoleggio, LocalDate.now(), importoBase, importoPenali, importoTotale, metodoPagamento);
    }

    //METODO PER OTTENERE I DATI DELLA FATTURA
    public String[] ottieniDatiFattura(int idNoleggio) {
        // Creazione dell' istanza del DAO
        FatturaDAO fatturaDAO = new FatturaImplementazionePostgresDAO();
        // Invocazione al metodo per prelevare i dati della fattura sul DB per visualizzarli sulla GUI
        return fatturaDAO.getDatiFatturaPerGUI(idNoleggio);
    }

    // METODO PER DISATTIVARE UN VEICOLO
    public boolean disattivaVeicolo(Veicolo v) {
        if (v == null) return false;
        // Creazione istanza del DAO
        VeicoloDAO veicoloDAO = new VeicoloImplementazionePostgresDAO();
        // Invocazione al metodo che permette di disattivare un veicolo dal DB
        boolean successo = veicoloDAO.disattivaVeicoloDB(v.getTarga());

        // Stampa dei risultati
        if (successo) {
            System.out.println("Veicolo con targa " + v.getTarga() + " disattivato dal database.");
        } else {
            System.err.println("Impossibile eliminare il veicolo dal database.");
        }

        return successo;
    }

    // METODO PER LA RESTITUZIONE DELLA LISTA DEGLI OPERATORI
    public ArrayList<Operatore> getListaOperatori() {
        return listaOperatori;
    }
}