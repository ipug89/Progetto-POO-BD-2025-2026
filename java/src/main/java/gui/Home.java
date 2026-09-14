package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home {
    private JPanel homePanel;
    private JTextField campoMatricola;
    private JButton invioMatricola;
    private JButton registratiButton;
    private JLabel labelMatricola;
    private JLabel labelRegistrati;

    public static JFrame frameHome;
    private Controller controller;

    public Home(Controller controller) {

        this.controller = controller;

        // LISTENER BOTTONE REGISTRAZIONE
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Home.frameHome != null) {
                    Home.frameHome.setVisible(false);
                }
                new Registrazione(controller);
            }
        });

        // LISTENER BOTTONE INVIO
        invioMatricola.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Controllo della matricola ed eventuali spazi vuoti
                String matricolaInserita = campoMatricola.getText().trim();

                if (controller.verificaMatricola(matricolaInserita)) {
                    new Info(controller);
                    campoMatricola.setText("");
                    Home.frameHome.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(frameHome, "Matricola non corretta o inesistente. Riprova o effettua la registrazione.",
                            "Accesso Negato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        Controller controllerCondiviso = new Controller();
        frameHome = new JFrame("Drive Experience - Login");
        frameHome.setContentPane(new Home(controllerCondiviso).homePanel);
        frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameHome.setSize(650, 450); // Dimensioni scalate e proporzionate alla form
        frameHome.setLocationRelativeTo(null);
        frameHome.setVisible(true);
    }
}