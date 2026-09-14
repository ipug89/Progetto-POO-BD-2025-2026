package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VisualizzaNoleggi {
    private JPanel mainPanel;
    private JPanel visualizzaNoleggioSearchPanel;
    private JComboBox<String> comboStato;
    private JButton searchNoleggioButton;
    private JTable tabellaNoleggi;
    private JButton chiudiNoleggioButton;
    private JButton indietroButton;

    public static JFrame frameVisualizza;
    private Controller controller;

    public VisualizzaNoleggi(Controller controller) {
        this.controller = controller;

        frameVisualizza = new JFrame("Drive Experience - Visualizza Noleggi");
        frameVisualizza.setContentPane(mainPanel);
        frameVisualizza.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameVisualizza.setSize(950, 480); // Leggermente allargato per ospitare le nuove colonne
        frameVisualizza.setLocationRelativeTo(null);

        // LISTENER DEL BOTTONE INDIETRO
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameVisualizza.dispose();
                new Info(controller);
            }
        });

        // LISTENER BOTTONE ChiudiNoleggio
        chiudiNoleggioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Acquisizione del noleggio (riga) selezionato
                int rigaSelezionata = tabellaNoleggi.getSelectedRow();

                // Gestione in caso non sia selezionata nessuna riga
                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frameVisualizza, "Seleziona un noleggio dalla tabella prima di procedere.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Conversione indice per supportare correttamente la tabella anche se filtrata
                int modelRow = tabellaNoleggi.convertRowIndexToModel(rigaSelezionata);

                // Prelievo dello stato del noleggio selezionato
                String statoNoleggio = tabellaNoleggi.getModel().getValueAt(modelRow, 4).toString();

                // Gestione selezione noleggio già concluso
                if (statoNoleggio.equals("Concluso")) {
                    JOptionPane.showMessageDialog(frameVisualizza, "Questo noleggio è già stato chiuso e fatturato!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Prelievo dati del noleggio selezionato
                int idNoleggioSelezionato = Integer.parseInt(tabellaNoleggi.getModel().getValueAt(modelRow, 0).toString());
                String targaVeicolo = tabellaNoleggi.getModel().getValueAt(modelRow, 6).toString();
                String dataInizioStr = tabellaNoleggi.getModel().getValueAt(modelRow, 1).toString();
                String dataFineStr = tabellaNoleggi.getModel().getValueAt(modelRow, 2).toString();

                // Passaggio alla schermata CheckIn
                frameVisualizza.dispose();
                new CheckIn(controller, idNoleggioSelezionato, targaVeicolo, dataInizioStr, dataFineStr);
            }
        });

        // LISTENER BOTTONE CERCA
        searchNoleggioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Definizione del modello della tabella
                DefaultTableModel modello = (DefaultTableModel) tabellaNoleggi.getModel();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modello);
                tabellaNoleggi.setRowSorter(sorter);

                // Filtraggio della tabella
                String filtroScelto = (String) comboStato.getSelectedItem();
                if (filtroScelto != null && filtroScelto.equals("Tutti")) {
                    sorter.setRowFilter(null);
                } else if (filtroScelto != null) {
                    sorter.setRowFilter(RowFilter.regexFilter("^" + filtroScelto + "$", 4)); // ^ --> inizio stringa -- $ --> fine stringa
                }
            }
        });

        // Inserimento valori della comboBox
        comboStato.removeAllItems();
        comboStato.addItem("Tutti");
        comboStato.addItem("In Programma");
        comboStato.addItem("In Corso");
        comboStato.addItem("In Ritardo");
        comboStato.addItem("Concluso");

        aggiornaTabella();
        frameVisualizza.setVisible(true);
    }

    // METODO PER AGGIORNARE LA TABELLA
    public void aggiornaTabella() {
        // Definizione delle colonne
        String[] colonne = {"ID Noleggio", "Data Inizio", "Fine Prevista", "Riconsegna", "Stato", "Patente", "Targa", "Posti", "Porte", "Operatore"};

        // Definizione del modello della tabella
        DefaultTableModel modelloTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ArrayList<Object[]> righeDalController = controller.ottieniDatiNoleggi();

        // Aggiunta dei noleggi nella tabella
        for (Object[] riga : righeDalController) {
            modelloTabella.addRow(riga);
        }

        tabellaNoleggi.setModel(modelloTabella);
        tabellaNoleggi.setFillsViewportHeight(true);
    }
}