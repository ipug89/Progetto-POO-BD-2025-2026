package gui;

import controller.Controller;
import exception.CampoNonPresente;
import exception.FormatoNonValidoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Aggiunta_veicoli {
    private JTextField textMarca;
    private JTextField modelloTextField;
    private JTextField textTarga;
    private JTextField textKm;
    private JComboBox comboTipologia;
    private JTextField textTariffa;
    private JButton aggiungiButton;
    private JButton annullaButton;
    private JPanel aggiuntaPanel;
    private JTextField chilometriTextField;
    private JComboBox comboCategoria;
    private JTextField nPostiTextField;
    private JTextField nPorteTextField;
    private JTextField capacitaTextField;
    private JTextField volumeTextField;
    private JTextField altezzaTextField;

    private JLabel labelTipologia;
    private JLabel labelTarga;
    private JLabel labelMarca;
    private JLabel labelModello;
    private JLabel labelKm;
    private JLabel labelTariffa;
    private JLabel labelPosti;
    private JLabel labelPorte;
    private JLabel labelCategoria;
    private JLabel labelCapacita;
    private JLabel labelVolume;
    private JLabel labelAltezza;
    private JLabel titolo;

    public static JFrame frameAggiunta;
    private Controller controller;
    private Info finestraInfo;

    public Aggiunta_veicoli(Controller controller, Info finestraInfo) {

        frameAggiunta = new JFrame("Aggiunta Veicoli");
        frameAggiunta.setContentPane(aggiuntaPanel);
        frameAggiunta.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameAggiunta.setSize(750, 520);
        frameAggiunta.setLocationRelativeTo(null);

        this.controller = controller;
        this.finestraInfo = finestraInfo;

        // LISTENER BOTTONE ANNULLA
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameAggiunta.dispose();
                Info.frameInfo.setVisible(true);
            }
        });

        // LISTENER COMBO BOX: Gestione dinamica dei campi
        comboTipologia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selezione = (String) comboTipologia.getSelectedItem();

                if ("Auto".equals(selezione)) {
                    // Campi esclusivi del furgone disabilitati e azzerati
                    capacitaTextField.setEnabled(false);
                    capacitaTextField.setText(null);
                    volumeTextField.setEnabled(false);
                    volumeTextField.setText(null);
                    altezzaTextField.setEnabled(false);
                    altezzaTextField.setText(null);

                    // Campi auto abilitati
                    nPorteTextField.setEnabled(true);
                    nPostiTextField.setEnabled(true);
                    comboCategoria.setEnabled(true);
                } else if ("Furgone".equals(selezione)) {
                    // Solo la categoria auto viene disabilitata
                    comboCategoria.setEnabled(false);

                    // Campi furgone abilitati
                    nPorteTextField.setEnabled(true);
                    nPostiTextField.setEnabled(true);
                    capacitaTextField.setEnabled(true);
                    volumeTextField.setEnabled(true);
                    altezzaTextField.setEnabled(true);
                }
            }
        });

        // LISTENER BOTTONE AGGIUNGI
        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String selezione = (String) comboTipologia.getSelectedItem();

                    // Assegnazione dei dati inseriti nelle variabili per effettuarne il controllo
                    String targa = textTarga.getText();
                    String marca = textMarca.getText();
                    String modello = modelloTextField.getText();

                    // Conversione delle stringhe in dati numerici
                    double km = Double.parseDouble(chilometriTextField.getText().replace(",", "."));
                    double tariffa = Double.parseDouble(textTariffa.getText().replace(",", "."));

                    boolean successo = false;

                    // Assegnazione e controllo dei dati inseriti in base alla categoria
                    if ("Auto".equals(selezione)) {
                        int nPosti = Integer.parseInt(nPostiTextField.getText());
                        int nPorte = Integer.parseInt(nPorteTextField.getText());
                        String categoria = (String) comboCategoria.getSelectedItem();

                        // Passaggio dei dati al controller per la verifica
                        successo = controller.aggiungiAuto(targa, marca, modello, km, tariffa, nPosti, nPorte, categoria);
                    } else if ("Furgone".equals(selezione)) {
                        int nPosti = Integer.parseInt(nPostiTextField.getText());
                        int nPorte = Integer.parseInt(nPorteTextField.getText());
                        double capacita = Double.parseDouble(capacitaTextField.getText().replace(",", "."));
                        double volume = Double.parseDouble(volumeTextField.getText().replace(",", "."));
                        double altezza = Double.parseDouble(altezzaTextField.getText().replace(",", "."));

                        // Passaggio dei dati al controller per la verifica
                        successo = controller.aggiungiFurgone(targa, marca, modello, km, tariffa, nPosti, nPorte, capacita, volume, altezza);
                    }

                    if (successo) {
                        // Se i dati inseriti sono giusti, aggiorna la lista dei veicoli e passa alla schermata 'Info'
                        finestraInfo.aggiornaGraficaLista();

                        JOptionPane.showMessageDialog(null, "Veicolo inserito con successo nel database!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                        frameAggiunta.dispose();
                        Info.frameInfo.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "ERRORE, impossibile registrare il veicolo.\nVerificare che la targa non sia già presente nel sistema!", "Errore Database", JOptionPane.WARNING_MESSAGE);
                    }

                    // Gestione delle eccezioni
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Errore: Inserisci numeri validi per Tariffa, Km e caratteristiche.",
                            "Errore di Formato", JOptionPane.ERROR_MESSAGE);
                } catch (CampoNonPresente | FormatoNonValidoException ex) {
                    JOptionPane.showMessageDialog(null, "Errore sui campi: " + ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Errore imprevisto di sistema: " + ex.toString());
                }
            }

        });

        comboTipologia.setSelectedIndex(0);
        frameAggiunta.setVisible(true);
    }
}
