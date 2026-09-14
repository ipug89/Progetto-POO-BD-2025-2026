package gui;

import controller.Controller;
import exception.FormatoNonValidoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Registrazione {
    private JPanel registrazione;
    private JTextField campoNome;
    private JTextField campoCognome;
    private JTextField campoCodiceFiscale;
    private JTextField campoEmail;
    private JTextField campoTelefono;
    private JButton confermaButton;
    private JButton tornaAllaHomeButton;
    private JTextField matricolaField;

    private Controller controller;
    public static JFrame frameRegistrazione;

    public Registrazione(Controller controller) {

        this.controller = controller;
        frameRegistrazione = new JFrame("Drive Experience - Registrazione Operatore");
        frameRegistrazione.setContentPane(registrazione);
        frameRegistrazione.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameRegistrazione.setSize(800, 440); // Dimensione ottimizzata per i margini scuri
        frameRegistrazione.setLocationRelativeTo(null);
        frameRegistrazione.setVisible(true);

        // LISTENER BOTTONE TornaAllaHome
        tornaAllaHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameRegistrazione.dispose();
                Home.frameHome.setVisible(true);
            }
        });

        // LISTENER BOTTONE CONFERMA
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Assegnazione dei valori inseriti
                String matricola = matricolaField.getText();
                String nome = campoNome.getText();
                String cognome = campoCognome.getText();
                String codiceFiscale = campoCodiceFiscale.getText();
                String email = campoEmail.getText();
                String telefono = campoTelefono.getText();

                try {
                    // Registrazione nuovo operatore
                    boolean successo = controller.registraNuovoOperatore(matricola, nome, cognome, codiceFiscale, email, telefono);

                    if (successo)
                    {
                        JOptionPane.showMessageDialog(null, "Registrazione effettuata con successo!\nLa tua matricola è: " + matricola, "Registrazione Completa", JOptionPane.INFORMATION_MESSAGE);
                        frameRegistrazione.dispose();
                        Home.frameHome.setVisible(true);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "ERRORE, impossibile completare la registrazione!", "Errore DB!", JOptionPane.WARNING_MESSAGE);
                    }
                    // Gestione errori
                } catch (FormatoNonValidoException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERRORE di compilazione!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Genera la matricola e la mostra nel campo di testo appena si apre la GUI
        String matricolaGenerata = controller.generaMatricolaCasuale();
        matricolaField.setText(matricolaGenerata);
        matricolaField.setEditable(false); // Evita che l'utente la modifichi
    }
}