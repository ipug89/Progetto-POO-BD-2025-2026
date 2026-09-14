package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CheckIn {
    private JPanel mainPanel;
    private JTextField campoKmRientro;
    private JTextField campoCarburante;
    private JTextArea campoNuoviDanni;
    private JButton concludiNoleggioButton;
    private JButton annullaButton;

    public static JFrame frameCheckIn;
    private Controller controller;
    private int idNoleggio;

    private String targaVeicolo;
    private String dataInizio;
    private String dataFine;

    public CheckIn(Controller controller, int idNoleggio, String targaVeicolo, String dataInizio, String dataFine) {
        this.controller = controller;

        this.idNoleggio = idNoleggio;
        this.targaVeicolo = targaVeicolo;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;

        frameCheckIn = new JFrame("Check-In: Riconsegna Veicolo");
        frameCheckIn.setContentPane(mainPanel);
        frameCheckIn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameCheckIn.setSize(500, 400);
        frameCheckIn.setLocationRelativeTo(null);
        frameCheckIn.setVisible(true);

        // LISTENER BOTTONE CHIUSURA
        concludiNoleggioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    // Conversione delle stringhe in dati numerici
                    double kmRientro = Double.parseDouble(campoKmRientro.getText());
                    float carburante = Float.parseFloat(campoCarburante.getText());

                    // Controllo del valore del carburante
                    if (carburante < 0 || carburante > 1) {
                        JOptionPane.showMessageDialog(null, "Il carburante deve essere un valore tra 0 e 1.", "Errore", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String nuoviDanni = campoNuoviDanni.getText();
                    LocalDate dataRientroOggi = LocalDate.now();

                    // Registrazione del noleggio
                    boolean successo = controller.registraRientroNoleggio(
                            idNoleggio,
                            targaVeicolo,
                            dataRientroOggi,
                            kmRientro,
                            carburante,
                            nuoviDanni
                    );

                    // Se il noleggio viene registrato correttamente si genera la fattura
                    if (successo) {
                        controller.generaFattura(idNoleggio, targaVeicolo, dataInizio, dataFine, dataRientroOggi, "Carta di Credito");

                        String[] datiFattura = controller.ottieniDatiFattura(idNoleggio);

                        if (datiFattura != null) {
                            // Assegnazione del formato della data
                            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                            // Calcolo dei giorni effettivi del noleggio
                            long giorniEffettivi = ChronoUnit.DAYS.between(LocalDate.parse(dataInizio, fmt), dataRientroOggi);
                            if (giorniEffettivi <= 0) giorniEffettivi = 1;

                            // Passaggio alla schermata 'FatturaGUI' con i rispettivi dati
                            new FatturaGUI(
                                    controller,
                                    datiFattura[0], // Nome
                                    datiFattura[1], // Cognome
                                    datiFattura[2], // CF
                                    datiFattura[3], // Patente
                                    (int) giorniEffettivi,
                                    datiFattura[7], // Totale
                                    datiFattura[4], // Marca
                                    datiFattura[5], // Modello
                                    datiFattura[6], // Targa
                                    null
                            );
                        }

                        // Chiusura della schermata 'CheckIn' e 'VisualizzaNoleggi'
                        frameCheckIn.dispose();
                        if (VisualizzaNoleggi.frameVisualizza != null) {
                            VisualizzaNoleggi.frameVisualizza.dispose();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Errore durante la chiusura del noleggio nel DB.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }

                    // Gestione delle eccezioni
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Inserisci valori numerici validi per Km e Carburante!");
                } catch (exception.FormatoNonValidoException ex) {
                    JOptionPane.showMessageDialog(frameCheckIn, ex.getMessage(), "Incongruenza Chilometri", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // LISTENER BOTTONE ANNULLA
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ritorno alla schermata 'VisualizzaNoleggi'
                frameCheckIn.dispose();
                VisualizzaNoleggi.frameVisualizza.setVisible(true);
            }
        });
    }
}