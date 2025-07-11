package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import service.ArquivoService;
import service.EnderecoService;
import view.PanelsearchAddress;

public class ControllersearchAddress {
	
	private PanelsearchAddress panelsearchAddress; 
	private EnderecoService enderecoService;
	private ArquivoService arquivoService;
	private Map<String, String> ultimoEndereco; 
	private String ultimoCep; 
	
	public ControllersearchAddress(PanelsearchAddress panelsearchAddress) {
        this.panelsearchAddress = panelsearchAddress;
        this.enderecoService = new EnderecoService();
        this.arquivoService = new ArquivoService();
        
        initializeListeners();
    }
	
	private void initializeListeners() {
		
		panelsearchAddress.getBtnConsultar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarCep();
            }
        });
		
		
		panelsearchAddress.getBtnSalvar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarEndereco();
            }
        });
		
		
		panelsearchAddress.getBtnHistorico().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verHistorico();
            }
        });
		
		
		panelsearchAddress.getBtnLimparHistorico().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparHistorico();
            }
        });
    }
	 
	private void consultarCep() {
        try {
            
            if (!panelsearchAddress.isCepValido()) {
                showErrorMessage("CEP inválido! Digite um CEP com 8 dígitos.");
                return;
            }

            String cep = panelsearchAddress.getCepSemFormatacao();
            
           
            if (!enderecoService.validarCep(cep)) {
                showErrorMessage("CEP inválido! Verifique se o CEP está correto.");
                return;
            }

           
            panelsearchAddress.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
            panelsearchAddress.getBtnConsultar().setEnabled(false);
            panelsearchAddress.getBtnConsultar().setText("Consultando...");

         
            SwingWorker<Map<String, String>, Void> worker = new SwingWorker<Map<String, String>, Void>() {
                @Override
                protected Map<String, String> doInBackground() throws Exception {
                    return enderecoService.consultarCep(cep);
                }

                @Override
                protected void done() {
                    try {
                        Map<String, String> endereco = get();
                        ultimoEndereco = endereco; 
                        ultimoCep = cep;
                        preencherCampos(endereco);
                        panelsearchAddress.getBtnSalvar().setEnabled(true); 
                        showSuccessMessage("CEP consultado com sucesso!");
                    } catch (Exception e) {
                        handleError(e);
                        panelsearchAddress.getBtnSalvar().setEnabled(false); 
                    } finally {
                        
                    	panelsearchAddress.setCursor(java.awt.Cursor.getDefaultCursor());
                    	panelsearchAddress.getBtnConsultar().setEnabled(true);
                    	panelsearchAddress.getBtnConsultar().setText("Consultar");
                    }
                }
            };

            worker.execute();

        } catch (Exception e) {
            handleError(e);
        }
    }
	
	private void salvarEndereco() {
        if (ultimoEndereco == null || ultimoCep == null) {
            showErrorMessage("Nenhum endereço para salvar. Consulte um CEP primeiro.");
            return;
        }
        
        try {
            arquivoService.salvarEndereco(ultimoCep, ultimoEndereco);
            
            
            int opcao = JOptionPane.showConfirmDialog(
                panelsearchAddress,
                "Endereço salvo com sucesso em: " + arquivoService.getCaminhoArquivo() + 
                "\n\nDeseja salvar automaticamente as próximas consultas?",
                "Salvo com Sucesso",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );
            
            panelsearchAddress.setSalvamentoAutomatico(opcao == JOptionPane.YES_OPTION);
            
        } catch (IOException e) {
            showErrorMessage("Erro ao salvar endereço: " + e.getMessage());
        }
    }
	
	private void verHistorico() {
        try {
            if (!arquivoService.arquivoExiste()) {
                showInfoMessage("Nenhum histórico encontrado. Consulte e salve alguns endereços primeiro.");
                return;
            }
            
            int totalConsultas = arquivoService.contarConsultas();
            String mensagem = "Total de consultas salvas: " + totalConsultas + 
                            "\nArquivo: " + arquivoService.getCaminhoArquivo() + 
                            "\n\nDeseja ver o histórico completo?";
            
            int opcao = JOptionPane.showConfirmDialog(
                panelsearchAddress,
                mensagem,
                "Histórico de Consultas",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (opcao == JOptionPane.YES_OPTION) {
                String historico = arquivoService.lerHistorico();
                
                // Cria uma janela para exibir o histórico
                javax.swing.JTextArea textArea = new javax.swing.JTextArea(historico);
                textArea.setEditable(false);
                textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
                
                javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);
                scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));
                
                JOptionPane.showMessageDialog(
                    panelsearchAddress,
                    scrollPane,
                    "Histórico Completo",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
            
        } catch (IOException e) {
            showErrorMessage("Erro ao ler histórico: " + e.getMessage());
        }
    }
	
	private void limparHistorico() {
        try {
            if (!arquivoService.arquivoExiste()) {
                showInfoMessage("Não há histórico para limpar.");
                return;
            }
            
            int opcao = JOptionPane.showConfirmDialog(
                panelsearchAddress,
                "Tem certeza que deseja limpar todo o histórico?\nEsta ação não pode ser desfeita.",
                "Confirmar Limpeza",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (opcao == JOptionPane.YES_OPTION) {
                arquivoService.limparHistorico();
                showSuccessMessage("Histórico limpo com sucesso!");
            }
            
        } catch (IOException e) {
            showErrorMessage("Erro ao limpar histórico: " + e.getMessage());
        }
    }
	
	private void preencherCampos(Map<String, String> endereco) {
        panelsearchAddress.getLogradouroField().setText(endereco.get("logradouro"));
        panelsearchAddress.getBairroField().setText(endereco.get("bairro"));
        panelsearchAddress.getCidadeField().setText(endereco.get("localidade"));
        panelsearchAddress.getUfField().setText(endereco.get("uf"));
        panelsearchAddress.getComplementoField().setText(endereco.get("complemento"));
        
        
        if (panelsearchAddress.isSalvamentoAutomatico()) {
            try {
                arquivoService.salvarEndereco(ultimoCep, endereco);
                panelsearchAddress.setStatusMessage("Endereço salvo automaticamente");
            } catch (IOException e) {
                panelsearchAddress.setStatusMessage("Erro ao salvar automaticamente: " + e.getMessage());
            }
        }
    }
	 
	private void handleError(Exception e) {
        String mensagem;
        
        if (e instanceof IllegalArgumentException) {
            mensagem = e.getMessage();
        } else if (e instanceof IOException) {
            mensagem = "Erro de conexão. Verifique sua internet e tente novamente.";
        } else if (e instanceof InterruptedException) {
            mensagem = "Consulta interrompida. Tente novamente.";
        } else {
            mensagem = "Erro inesperado: " + e.getMessage();
        }
        
        showErrorMessage(mensagem);
    }

	private void showErrorMessage(String mensagem) {
        JOptionPane.showMessageDialog(panelsearchAddress, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String mensagem) {
        JOptionPane.showMessageDialog(panelsearchAddress, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showInfoMessage(String mensagem) {
        JOptionPane.showMessageDialog(panelsearchAddress, mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
}