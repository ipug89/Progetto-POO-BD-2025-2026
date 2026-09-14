package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FatturaGUI {
    private JPanel fatturaPanel;
    private JButton tornaAllaHomeButton;
    private JPanel Fattura;
    private JLabel nomeRicevuto;
    private JLabel cognomeRicevuto;
    private JLabel codiceFricevuto;
    private JLabel nPatenteRicevuto;
    private JLabel ngiorniRicevuto;

    private JLabel prezzoRicevuto;
    private JLabel marcaRicevuta;
    private JLabel modelloRicevuto;
    private JLabel targaRicevuta;
    private JLabel testoNome;
    private JLabel testoCognome;
    private JLabel testoCf;
    private JLabel nPatenteTesto;
    private JLabel nGiorniTesto;
    private JLabel prezzotesto;
    private JLabel marcaTesto;
    private JLabel targaTesto;
    private JLabel titolo;
    private JLabel modelloTesto;


    private Controller controller;
    public static JFrame frameFattura;

    public FatturaGUI(Controller controller, String nomeRicevuto, String cognomeRicevuto, String codiceFiscaleRicevuto,
                      String nPatenteRicevuta, int ngiorniRicevuti, String costoTotale,
                      String marca, String modello, String targa, NoleggioGUI noleggioPrecedente) {

        this.controller = controller;

        frameFattura = new JFrame("Riepilogo Fattura");
        frameFattura.setContentPane(fatturaPanel);
        frameFattura.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameFattura.setSize(700, 600); // Proporzioni più equilibrate e verticali
        frameFattura.setLocationRelativeTo(null);

        // Mappatura dei dati
        this.nomeRicevuto.setText(nomeRicevuto);
        this.cognomeRicevuto.setText(cognomeRicevuto);
        this.codiceFricevuto.setText(codiceFiscaleRicevuto);
        this.nPatenteRicevuto.setText(nPatenteRicevuta);
        this.ngiorniRicevuto.setText(String.valueOf(ngiorniRicevuti));
        this.prezzoRicevuto.setText(costoTotale + " €");
        this.marcaRicevuta.setText(marca);
        this.targaRicevuta.setText(targa);
        this.modelloRicevuto.setText(modello);

        frameFattura.setVisible(true);

        // LISTENER BOTTONE TornaAllaHome
        tornaAllaHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameFattura.dispose();
                new Info(controller);
            }
        });
    }
}