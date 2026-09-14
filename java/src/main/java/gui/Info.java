package gui;

import controller.Controller;
import model.veicoli.Auto;
import model.veicoli.Furgone;
import model.veicoli.Veicolo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Info {
    private JPanel infoPanel;
    private JList<Veicolo> listaVeicoliDisponibili;
    private JRadioButton autoRadioButton;
    private JRadioButton furgoniRadioButton;
    private JButton aggiungiVeicoloButton;
    private JButton rimuoviVeicoloButton;
    private JButton noleggiaButton;
    private JButton visualizzaNoleggiButton;
    private JButton tornaAllaHomeButton;
    private JLabel marcaField;
    private JLabel targaField;
    private JLabel kmField;
    private JLabel modelloField;
    private JLabel marcaLabel;
    private JLabel modelloLabel;
    private JLabel targaLabel;
    private JLabel kmLabel;
    private ButtonGroup gruppoVeicoli;

    private Controller controller;
    private static DefaultListModel<Veicolo> modelloListaVeicoli;
    public static JFrame frameInfo;

    public Info(Controller controller) {
        this.controller = controller;

        frameInfo = new JFrame("Drive Experience - Hub Control Flotta");
        frameInfo.setContentPane(infoPanel);
        frameInfo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameInfo.setSize(800, 400);
        frameInfo.setLocationRelativeTo(null);

        modelloListaVeicoli = new DefaultListModel<>();
        listaVeicoliDisponibili.setModel(modelloListaVeicoli);

        gruppoVeicoli = new ButtonGroup();
        gruppoVeicoli.add(autoRadioButton);
        gruppoVeicoli.add(furgoniRadioButton);

        autoRadioButton.setSelected(true);

        // LISTENER BOTTONE TornaAllaHome
        tornaAllaHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameInfo.dispose();
                if (Home.frameHome != null) {
                    Home.frameHome.setVisible(true);
                }
            }
        });

        // LISTENER BOTTONE AggiungiVeicolo
        aggiungiVeicoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameInfo.setVisible(false);
                new Aggiunta_veicoli(controller, Info.this);
            }
        });

        // LISTENER BOTTONE VisualizzaNoleggi
        visualizzaNoleggiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameInfo.setVisible(false);
                new VisualizzaNoleggi(controller);
            }
        });

        // LISTENER SELEZIONE VEICOLO
        listaVeicoliDisponibili.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // CONTROLLO DELLA SELEZIONE DEL VEICOLO SCELTO NELLA LISTA
                if (!e.getValueIsAdjusting()) {
                    // Assegnazione dei dati del veicolo selezionato
                    Veicolo veicoloSelezionato = listaVeicoliDisponibili.getSelectedValue();

                    // Aggiornamento dei campi con i valori prelevati
                    if (veicoloSelezionato != null) {
                        marcaField.setText(veicoloSelezionato.getMarca());
                        modelloField.setText(veicoloSelezionato.getModello());
                        targaField.setText(veicoloSelezionato.getTarga());
                        kmField.setText(veicoloSelezionato.getChilometraggioAttuale() + " km");

                        marcaLabel.setVisible(true);
                        marcaField.setVisible(true);

                        modelloLabel.setVisible(true);
                        modelloField.setVisible(true);

                        targaLabel.setVisible(true);
                        targaField.setVisible(true);

                        kmLabel.setVisible(true);
                        kmField.setVisible(true);

                        rimuoviVeicoloButton.setEnabled(true);
                        noleggiaButton.setEnabled(true);
                    }
                }
            }
        });

        // LISTENER BOTTONE NOLEGGIA
        noleggiaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Veicolo veicoloSelezionato = listaVeicoliDisponibili.getSelectedValue();
                if (veicoloSelezionato != null) {
                    frameInfo.setVisible(false);
                    new NoleggioGUI(controller, veicoloSelezionato);
                }
            }
        });

        // LISTENER BOTTONE RIMUOVI
        rimuoviVeicoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Veicolo veicoloSelezionato = listaVeicoliDisponibili.getSelectedValue();

                // Conferma dell'eliminazione del veicolo
                if (veicoloSelezionato != null) {
                    int scelta = JOptionPane.showConfirmDialog(frameInfo,
                            "Sei sicuro di voler eliminare definitivamente il veicolo targato " + veicoloSelezionato.getTarga() + "?",
                            "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);

                    if (scelta == JOptionPane.YES_OPTION) {
                        boolean successo = controller.disattivaVeicolo(veicoloSelezionato);

                        if (successo) {
                            JOptionPane.showMessageDialog(frameInfo, "Veicolo rimosso con successo dal sistema.", "Successo", JOptionPane.INFORMATION_MESSAGE);
                            aggiornaGraficaLista();
                        } else {
                            JOptionPane.showMessageDialog(frameInfo, "Impossibile rimuovere il veicolo.\nPotrebbe essere associato a noleggi passati o fatture.", "Errore Database", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // LISTENER RagioButton AUTO
        autoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aggiornaGraficaLista();
            }
        });

        // LISTENER RagioButton FURGONE
        furgoniRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aggiornaGraficaLista();
            }
        });

        aggiornaGraficaLista();
        frameInfo.setVisible(true);
    }

    // METODO PER AGGIORNARE LA LISTA
    public void aggiornaGraficaLista() {
        DefaultListModel<Veicolo> modelloLista = new DefaultListModel<>();

        // Aggiornamento della lista in base al RadioButton
        if (autoRadioButton.isSelected()) {
            ArrayList<Auto> listaAuto = controller.getAutoDisponibili();
            for (Auto a : listaAuto) {
                modelloLista.addElement(a);
            }
        } else if (furgoniRadioButton.isSelected()) {
            ArrayList<Furgone> listaFurgoni = controller.getFurgoniDisponibili();
            for (Furgone f : listaFurgoni) {
                modelloLista.addElement(f);
            }
        }

        listaVeicoliDisponibili.setModel(modelloLista);

        // Gestione visualizzazione delle caratteristiche
        marcaLabel.setVisible(false);
        marcaField.setText("");
        marcaField.setVisible(false);

        modelloLabel.setVisible(false);
        modelloField.setText("");
        modelloField.setVisible(false);

        targaLabel.setVisible(false);
        targaField.setText("");
        targaField.setVisible(false);

        kmLabel.setVisible(false);
        kmField.setText("");
        kmField.setVisible(false);

        rimuoviVeicoloButton.setEnabled(false);
        noleggiaButton.setEnabled(false);
    }
}