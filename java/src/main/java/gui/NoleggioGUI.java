package gui;

import controller.Controller;
import exception.FormatoNonValidoException;
import model.noleggio.Noleggio;
import model.utenti.Cliente;
import model.veicoli.Veicolo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.Info.frameInfo;

public class NoleggioGUI {
    private JPanel noleggioPanel;
    private JPanel jPanelSx;
    private JPanel jPanelDx;
    private JButton confermaNoleggioButton;
    private JTextField campoNomeNoleggio;
    private JTextField campoCognomeNoleggio;
    private JTextField campoCdfNoleggio;
    private JTextField campoNPatenteNoleggio;
    private JButton noleggioHomeButton;
    private JComboBox tipoPatenteNoleggio;
    private JLabel labelTarga;
    private JLabel labelMarca;
    private JLabel labelModello;
    private JTextField giorniNoleggio;
    private JTextField campoDataInizio;
    private JTextField campoDataFinePrevista;
    private JTextField campoScadenzaPatente;
    private JTextField campoEmailNoleggio;
    private JTextField campoTelefonoNoleggio;

    public static JFrame frameNoleggio;
    private Controller controller;
    private Veicolo veicoloSelezionato;

    public NoleggioGUI(Controller controller, Veicolo veicoloSelezionato) {
        this.controller = controller;
        this.veicoloSelezionato = veicoloSelezionato;

        frameNoleggio = new JFrame("Drive Experience - Nuovo Noleggio");
        frameNoleggio.setContentPane(noleggioPanel);
        frameNoleggio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameNoleggio.setSize(850, 480);
        frameNoleggio.setLocationRelativeTo(null);

        // Popolamento label del veicolo selezionato
        if (veicoloSelezionato != null) {
            labelTarga.setText(veicoloSelezionato.getTarga());
            labelMarca.setText(veicoloSelezionato.getMarca());
            labelModello.setText(veicoloSelezionato.getModello());
        } else {
            labelTarga.setText("-");
            labelMarca.setText("-");
            labelModello.setText("-");
        }

        // LISTENER CONFERMA NOLEGGIO
        confermaNoleggioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Controllo selezione veicolo
                    if (veicoloSelezionato == null) {
                        JOptionPane.showMessageDialog(frameNoleggio, "Errore: Nessun veicolo selezionato per il noleggio!", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Lettura campi anagrafici e date
                    String nome = campoNomeNoleggio.getText().trim();
                    String cognome = campoCognomeNoleggio.getText().trim();
                    String cf = campoCdfNoleggio.getText().trim();
                    String nPatente = campoNPatenteNoleggio.getText().trim();
                    String scadenzaPatenteStr = campoScadenzaPatente.getText().trim();
                    String email = campoEmailNoleggio.getText().trim();
                    String telefono = campoTelefonoNoleggio.getText().trim();
                    String categoria = tipoPatenteNoleggio.getSelectedItem() != null ? tipoPatenteNoleggio.getSelectedItem().toString() : "B";

                    String dataInizioStr = campoDataInizio.getText().trim();
                    String dataFineStr = campoDataFinePrevista.getText().trim();

                    // Controllo inserimento dei dati
                    if (dataInizioStr.isEmpty() || dataFineStr.isEmpty() || scadenzaPatenteStr.isEmpty()) {
                        JOptionPane.showMessageDialog(frameNoleggio, "Compilare tutti i campi data (Inizio, Fine prevista, Scadenza Patente).", "Campi Mancanti", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    // Conversione date (String --> LocalDate)
                    LocalDate dataInizio = LocalDate.parse(dataInizioStr, formatter);
                    LocalDate dataFine = LocalDate.parse(dataFineStr, formatter);

                    // Assegnazione data odierna
                    LocalDate oggi = LocalDate.now();

                    // Controllo validità data
                    if (dataInizio.isBefore(oggi)) {
                        JOptionPane.showMessageDialog(frameNoleggio, "La data di inizio noleggio non può essere nel passato!", "Errore Data", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!dataFine.isAfter(dataInizio)) {
                        JOptionPane.showMessageDialog(frameNoleggio, "La data di fine prevista deve essere successiva alla data di inizio!", "Errore Date", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Calcolo della durata del noleggio
                    long giorniCalcolati = ChronoUnit.DAYS.between(dataInizio, dataFine);
                    if (giorniNoleggio != null) {
                        giorniNoleggio.setText(String.valueOf(giorniCalcolati));
                    }

                    // Creazione cliente
                    Cliente cliente = new Cliente(
                            nPatente,
                            scadenzaPatenteStr,
                            categoria,
                            nome,
                            cognome,
                            cf,
                            email,
                            telefono
                    );

                    // CONTROLLO SCADENZA PATENTE
                    LocalDate dataScadenzaPatente = cliente.getDataScadenzaPatente();

                    // 1. Verifica se già scaduta oggi
                    if (dataScadenzaPatente.isBefore(oggi)) {
                        JOptionPane.showMessageDialog(frameNoleggio,
                                "Impossibile procedere: la patente risulta scaduta il " + dataScadenzaPatente.format(formatter) + "!",
                                "Patente Scaduta", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // 2. Verifica se scadrà durante il periodo di noleggio
                    if (dataScadenzaPatente.isBefore(dataFine)) {
                        JOptionPane.showMessageDialog(frameNoleggio,
                                "Impossibile procedere: la patente scadrà il " + dataScadenzaPatente.format(formatter)
                                        + ", prima della fine prevista del noleggio (" + dataFine.format(formatter) + ")!",
                                "Scadenza Patente Non Valida", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Registrazione/Verifica cliente nel database
                    boolean clienteValido = controller.gestisciCliente(cliente);
                    if (!clienteValido) {
                        JOptionPane.showMessageDialog(frameNoleggio,
                                "Dati cliente non validi. Verifica che Nome, Cognome, Codice Fiscale (16 caratteri) ed Email siano corretti.",
                                "Errore Dati Cliente", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Creazione istanza Noleggio
                    Noleggio nuovoNoleggio = new Noleggio(
                            1,
                            dataInizioStr,
                            dataFineStr,
                            null,
                            null,
                            null,
                            cliente
                    );

                    // Associazione del veicolo al noleggio
                    nuovoNoleggio.setVeicolo(veicoloSelezionato);

                    // Associazione dell'operatore loggato al noleggio
                    if (!controller.getListaOperatori().isEmpty()) {
                        nuovoNoleggio.addOperatore(controller.getListaOperatori().get(0));
                    } else {
                        JOptionPane.showMessageDialog(frameNoleggio,
                                "Errore critico: Nessun operatore loggato nel sistema. Effettua il login dall'Home.",
                                "Operatore Mancante", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    //  Passaggio al CheckOut
                    frameNoleggio.setVisible(false);
                    new CheckOut(controller, nuovoNoleggio);

                    // Gestione degli errori
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(frameNoleggio,
                            "Formato data non valido. Inserire le date nel formato GG/MM/AAAA (es. 15/09/2026).",
                            "Errore Formato Data", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frameNoleggio,
                            "Errore imprevisto durante l'elaborazione del noleggio: " + ex.getMessage(),
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // LISTENER TORNA ALLA HOME
        noleggioHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameNoleggio.dispose();
                if (frameInfo != null) {
                    frameInfo.setVisible(true);
                }
            }
        });

        frameNoleggio.setVisible(true);
    }
}