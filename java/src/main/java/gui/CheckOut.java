package gui;

import controller.Controller;
import exception.FormatoNonValidoException;
import model.noleggio.Noleggio;
import model.schede.SchedaUscita;
import model.veicoli.Veicolo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class CheckOut {
    private JPanel mainPanel;
    private JTextField campoKmUscita;
    private JTextField campoCarburante;
    private JCheckBox checkLavaggio;
    private JTextArea campoNoteDanni;
    private JButton confermaCheckOutButton;
    private JButton annullaButton;

    public static JFrame frameCheckOut;
    private Controller controller;
    private Noleggio noleggioInCorso;

    public CheckOut(Controller controller, Noleggio noleggioDaGestire) {
        this.controller = controller;
        this.noleggioInCorso = noleggioDaGestire;

        frameCheckOut = new JFrame("Check-Out: Consegna Veicolo");
        frameCheckOut.setContentPane(mainPanel);
        frameCheckOut.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameCheckOut.setSize(500, 400);
        frameCheckOut.setLocationRelativeTo(null);
        frameCheckOut.setVisible(true);


        // Verifica che ci sia un noleggio associato al veicolo
        Veicolo veicoloSelezionato = noleggioInCorso.getVeicolo();

        if (veicoloSelezionato != null) {
            double kmAttuali = veicoloSelezionato.getChilometraggioAttuale();
            campoKmUscita.setText(String.valueOf((int) kmAttuali));
        }

        // LISTENER BOTTONE CONFERMA
        confermaCheckOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Recupero i dati inseriti dall'operatore
                    double kmUscita = Double.parseDouble(campoKmUscita.getText().replace(",", "."));
                    float carburante = Float.parseFloat(campoCarburante.getText().replace(",", "."));

                    // Controllo del valore del carburante
                    if (carburante < 0 || carburante > 1) {
                        JOptionPane.showMessageDialog(frameCheckOut, "Il carburante deve essere un valore compreso tra 0 e 1 (es. 0.5 per metà serbatoio).", "Errore", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String noteDanni = campoNoteDanni.getText();
                    boolean isLavato = checkLavaggio.isSelected();

                    // Creazione della Scheda di Uscita (con la data di oggi)
                    SchedaUscita schedaUscita = new SchedaUscita(LocalDate.now(), kmUscita, carburante, noteDanni);

                    if (isLavato) {
                        schedaUscita.registraLavaggio();
                    }

                    // Associazione della scheda al noleggio
                    noleggioInCorso.setSchedaUscitaNoleggio(schedaUscita);

                    // --- CONTROLLO DI SICUREZZA ---
                    if (noleggioInCorso.getVeicolo() == null || noleggioInCorso.getListaOperatori().isEmpty()) {
                        JOptionPane.showMessageDialog(frameCheckOut,
                                "Errore: Nessun veicolo o operatore associato a questo noleggio.\nVerifica la selezione nella schermata precedente.",
                                "Errore di Associazione",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Modifica dello stato del veicolo in 'occupato'
                    Veicolo v = noleggioInCorso.getVeicolo();
                    v.setStato(true);

                    // Registrazione del noleggio nel DB
                    boolean successoDB = controller.registraNuovoNoleggio(noleggioInCorso);

                    if (successoDB) {
                        JOptionPane.showMessageDialog(frameCheckOut,
                                "Check-out completato! Il veicolo è stato consegnato al cliente.",
                                "Operazione Riuscita",
                                JOptionPane.INFORMATION_MESSAGE);

                        frameCheckOut.dispose();
                        if (Info.frameInfo != null) {
                            Info.frameInfo.dispose();
                        }
                        new Info(controller);
                    } else {
                        JOptionPane.showMessageDialog(frameCheckOut,
                                "Errore di salvataggio nel Database!\nVerifica che il cliente (Patente) esista già nel sistema.",
                                "Errore Database",
                                JOptionPane.ERROR_MESSAGE);

                        v.setStato(false);
                    }
                } catch (FormatoNonValidoException ex) {
                    // In caso di errore, passaggio dello stato del veicolo a 'libero'
                    if (noleggioInCorso.getVeicolo() == null) {
                        noleggioInCorso.getVeicolo().setStato(false);
                    }
                    JOptionPane.showMessageDialog(frameCheckOut,
                            ex.getMessage(),
                            "Validazione Fallita",
                            JOptionPane.WARNING_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frameCheckOut,
                            "Inserisci valori numerici validi per Chilometri e Carburante (es. 12000 e 1.0)",
                            "Errore Compilazione",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // LISTENER BOTTONE ANNULLA
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCheckOut.dispose();
                Info.frameInfo.setVisible(true);
            }
        });
    }
}