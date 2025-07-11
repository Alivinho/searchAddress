package view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

public class PanelsearchAddress extends JFrame {
    private JFormattedTextField cepField; 
    private JTextField logradouroField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField ufField;
    private JTextField complementoField;
    private JLabel statusLabel;
    
    private JButton btnConsultar;
    private JButton btnSalvar;
    private JButton btnHistorico;
    private JButton btnLimparHistorico;
    
    private boolean salvamentoAutomatico = false;
    
    public PanelsearchAddress() {
        inicializarInterface();
    }
    
    private void inicializarInterface() {
        setTitle("Consulta de CEP - Sistema de Endereços");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        
        // Painel principal com bordas
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // === SEÇÃO DE CONSULTA ===
        // CEP 
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("CEP:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        try {
            MaskFormatter cepMask = new MaskFormatter("#####-###");
            cepMask.setPlaceholderCharacter('_');
            cepField = new JFormattedTextField(cepMask);
            cepField.setColumns(15);
        } catch (ParseException e) {
            cepField = new JFormattedTextField();
            cepField.setColumns(15);
        }
        mainPanel.add(cepField, gbc);

        // Botão Consultar
        gbc.gridx = 2; gbc.gridy = 0;
        btnConsultar = new JButton("Consultar");
        btnConsultar.setBackground(new Color(0, 123, 255));
        btnConsultar.setForeground(Color.WHITE);
        mainPanel.add(btnConsultar, gbc);

        // === SEÇÃO DE RESULTADOS ===
        // Logradouro
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Logradouro:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 2;
        logradouroField = new JTextField(30);
        logradouroField.setEditable(false);
        logradouroField.setBackground(Color.WHITE);
        mainPanel.add(logradouroField, gbc);

        // Bairro
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Bairro:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 2;
        bairroField = new JTextField(30);
        bairroField.setEditable(false);
        bairroField.setBackground(Color.WHITE);
        mainPanel.add(bairroField, gbc);

        // Cidade
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Cidade:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        cidadeField = new JTextField(20);
        cidadeField.setEditable(false);
        cidadeField.setBackground(Color.WHITE);
        mainPanel.add(cidadeField, gbc);

        // UF
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("UF:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        ufField = new JTextField(5);
        ufField.setEditable(false);
        ufField.setBackground(Color.WHITE);
        mainPanel.add(ufField, gbc);

        // Complemento
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Complemento:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.gridwidth = 2;
        complementoField = new JTextField(30);
        complementoField.setEditable(true);
        mainPanel.add(complementoField, gbc);

        // === SEÇÃO DE BOTÕES DE AÇÃO ===
        // Painel para os botões
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.insets = new Insets(5, 10, 5, 10);
        btnGbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Botão Salvar
        btnGbc.gridx = 0; btnGbc.gridy = 0;
        btnSalvar = new JButton("💾 Salvar Endereço");
        btnSalvar.setEnabled(false);
        btnSalvar.setBackground(new Color(40, 167, 69));
        btnSalvar.setForeground(Color.WHITE);
        buttonPanel.add(btnSalvar, btnGbc);
        
        // Botão Histórico
        btnGbc.gridx = 1; btnGbc.gridy = 0;
        btnHistorico = new JButton("📋 Ver Histórico");
        btnHistorico.setBackground(new Color(108, 117, 125));
        btnHistorico.setForeground(Color.WHITE);
        buttonPanel.add(btnHistorico, btnGbc);
        
        // Botão Limpar Histórico
        btnGbc.gridx = 2; btnGbc.gridy = 0;
        btnLimparHistorico = new JButton("🗑️ Limpar Histórico");
        btnLimparHistorico.setBackground(new Color(220, 53, 69));
        btnLimparHistorico.setForeground(Color.WHITE);
        buttonPanel.add(btnLimparHistorico, btnGbc);
        
        // Adiciona o painel de botões ao painel principal
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buttonPanel, gbc);

        // === BARRA DE STATUS ===
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        statusLabel = new JLabel("Pronto para consulta");
        statusLabel.setForeground(Color.GRAY);
        statusLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        mainPanel.add(statusLabel, gbc);

        // Adiciona o painel principal à janela
        add(mainPanel);

        // Configurações da janela
        pack();
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(getSize());
    }
    
    // === MÉTODOS AUXILIARES ===
    public String getCepSemFormatacao() {
        String cep = cepField.getText();
        return cep.replaceAll("[^0-9]", ""); 
    }
    
    public boolean isCepValido() {
        String cepLimpo = getCepSemFormatacao();
        return cepLimpo.length() == 8;
    }
    
    public void setStatusMessage(String mensagem) {
        statusLabel.setText(mensagem);
    }
    
    public boolean isSalvamentoAutomatico() {
        return salvamentoAutomatico;
    }
    
    public void setSalvamentoAutomatico(boolean salvamentoAutomatico) {
        this.salvamentoAutomatico = salvamentoAutomatico;
        if (salvamentoAutomatico) {
            setStatusMessage("Salvamento automático ativado");
        } else {
            setStatusMessage("Pronto para consulta");
        }
    }
    
    public JFormattedTextField getCepField() {
        return cepField;
    }

    public void setCepField(JFormattedTextField cepField) {
        this.cepField = cepField;
    }

    public JTextField getLogradouroField() {
        return logradouroField;
    }

    public void setLogradouroField(JTextField logradouroField) {
        this.logradouroField = logradouroField;
    }

    public JTextField getBairroField() {
        return bairroField;
    }

    public void setBairroField(JTextField bairroField) {
        this.bairroField = bairroField;
    }

    public JTextField getCidadeField() {
        return cidadeField;
    }

    public void setCidadeField(JTextField cidadeField) {
        this.cidadeField = cidadeField;
    }

    public JTextField getUfField() {
        return ufField;
    }

    public void setUfField(JTextField ufField) {
        this.ufField = ufField;
    }

    public JTextField getComplementoField() {
        return complementoField;
    }

    public void setComplementoField(JTextField complementoField) {
        this.complementoField = complementoField;
    }

    public JButton getBtnConsultar() {
        return btnConsultar;
    }

    public void setBtnConsultar(JButton btnConsultar) {
        this.btnConsultar = btnConsultar;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public void setBtnSalvar(JButton btnSalvar) {
        this.btnSalvar = btnSalvar;
    }

    public JButton getBtnHistorico() {
        return btnHistorico;
    }

    public void setBtnHistorico(JButton btnHistorico) {
        this.btnHistorico = btnHistorico;
    }

    public JButton getBtnLimparHistorico() {
        return btnLimparHistorico;
    }

    public void setBtnLimparHistorico(JButton btnLimparHistorico) {
        this.btnLimparHistorico = btnLimparHistorico;
    }
}