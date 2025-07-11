package main;

import javax.swing.SwingUtilities;

import controller.ControllersearchAddress;
import view.PanelsearchAddress;

public class Main {
    
    public static void main(String[] args) {
       
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    PanelsearchAddress panelsearchAddress = new PanelsearchAddress();
              
                    ControllersearchAddress controllersearchAddress = new ControllersearchAddress(panelsearchAddress);
                    
                    panelsearchAddress.setVisible(true);
                    
                    System.out.println("Aplicação de Consulta de CEP iniciada com sucesso!");
                    
                } catch (Exception e) {
                    System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}