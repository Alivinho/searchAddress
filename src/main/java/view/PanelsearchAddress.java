package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class PanelsearchAddress extends JFrame {
    private JFormattedTextField cepField; 
    private JTextField logradouroField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField ufField;
    private JTextField complementoField;
    
    private JButton BtnConsultar;
    
    public PanelsearchAddress() {
        inicializarInterface();
    }
    
    private void inicializarInterface() {
        setTitle("Consulta de CEP");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        
        // CEP 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("CEP:"), gbc);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 0;
        
        
        try {
            MaskFormatter cepMask = new MaskFormatter("#####-###");
            cepMask.setPlaceholderCharacter('_');
            cepField = new JFormattedTextField(cepMask);
            cepField.setColumns(15);
        } catch (ParseException e) {
            // Fallback caso haja erro na criação da máscara
            cepField = new JFormattedTextField();
            cepField.setColumns(15);
        }
        add(cepField, gbc);

        // Botão Consultar
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 2; gbc.gridy = 0;
        BtnConsultar = new JButton("Consultar");
        add(BtnConsultar, gbc);

        // Logradouro
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Logradouro:"), gbc);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 2;
        logradouroField = new JTextField(30);
        logradouroField.setEditable(false);
        add(logradouroField, gbc);

        // Bairro
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Bairro:"), gbc);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 2;
        bairroField = new JTextField(30);
        bairroField.setEditable(false);
        add(bairroField, gbc);

        // Cidade
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Cidade:"), gbc);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 3;
        cidadeField = new JTextField(20);
        cidadeField.setEditable(false);
        add(cidadeField, gbc);

        // UF
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("UF:"), gbc);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 4;
        ufField = new JTextField(5);
        ufField.setEditable(false);
        add(ufField, gbc);

        // Complemento
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Complemento:"), gbc);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.gridwidth = 2;
        complementoField = new JTextField(30);
        add(complementoField, gbc);

        // Configurações da janela
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    
    public String getCepSemFormatacao() {
        String cep = cepField.getText();
        return cep.replaceAll("[^0-9]", ""); 
    }
    
    
    public boolean isCepValido() {
        String cepLimpo = getCepSemFormatacao();
        return cepLimpo.length() == 8;
    }
}