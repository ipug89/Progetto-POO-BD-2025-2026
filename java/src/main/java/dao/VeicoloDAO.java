package dao;

import model.veicoli.Auto;
import model.veicoli.Furgone;

import java.util.ArrayList;

public interface VeicoloDAO {

    // FIRMA DEL METODO PER REGISTRARE UN AUTO SUL DB
    public boolean registraAuto(String targa, String marca, String modello, double km, double tariffaGg, int numeroPosti, int numeroPorte, String categoria);

    // FIRMA DEL METODO PER REGISTRARE UN FURGONE SUL DB
    public boolean registraFurgone(String targa, String marca, String modello, double km, double tariffaGg,
                                   int nPosti, int nPorte, double capacitaM3, double volume, double altezza);

    // FIRMA DEL METODO PER LA RESTITUZIONIE DELLE AUTO DISPONIBILI A NOLEGGI SUL DB
    public void getAutoDisponibiliDB(ArrayList<String> targhe, ArrayList<String> marche, ArrayList<String> modelli,
                                     ArrayList<Double> chilometri, ArrayList<Double> tariffe,
                                     ArrayList<Integer> posti, ArrayList<Integer> porte, ArrayList<String> categorie);

    // FIRMA DEL METODO PER LA RESTITUZIONIE DEI FURGONI DISPONIBILI A NOLEGGI SUL DB
    public void getFurgoniDisponibiliDB(ArrayList<String> targhe, ArrayList<String> marche, ArrayList<String> modelli,
                                        ArrayList<Double> chilometri, ArrayList<Double> tariffe,
                                        ArrayList<Integer> posti, ArrayList<Integer> porte,
                                        ArrayList<Double> capacita, ArrayList<Double> volumi, ArrayList<Double> altezze);

    // FIRMA DEL METODO PER IMPOSTARE LO STATO DI UN VEICOLO SUL DB
    public boolean impostaStatoVeicoloDB(String targa, boolean statoOccupato);

    // FIRMA DEL METODO PER AGGIORNARE I KILOMETRI DI UN VEICOLO SUL DB
    public boolean aggiornaChilometriVeicoloDB(String targa, double nuoviKm);

    // FIRMA DEL METODO PER LA RESTITUZIONE DELLA TARIFFA DI UN VEICOLO SUL DB
    public double getTariffaVeicoloDB(String targa);

    // FIRMA DEL METODO PER DISATTIVARE UN VEICOLO SUL DB
    public boolean disattivaVeicoloDB(String targa);

    // FIRMA DEL METODO PER LA RESTITUZIONE DEI KILOMETRI DI UN VEICOLO SUL DB
    public double getChilometriVeicoloDB(String targa);
}