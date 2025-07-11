package service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArquivoService {
    
    private static final String NOME_ARQUIVO = "EnderecosBuscados.txt";
    private static final String SEPARADOR = "=" + "=".repeat(50);
    private static final String DIRETORIO_BASE = "C:\\Users\\livil\\git\\searchAddress\\src\\main\\resources\\Adress";
    private final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    
    public void salvarEndereco(String cep, Map<String, String> endereco) throws IOException {
        if (cep == null || endereco == null) {
            throw new IllegalArgumentException("CEP e endereço não podem ser nulos");
        }
        
        Path caminhoArquivo = getCaminhoCompleto();
        
        StringBuilder conteudo = new StringBuilder();
        
        // Cabeçalho da consulta
        conteudo.append(SEPARADOR).append("\n");
        conteudo.append("CONSULTA DE CEP - ").append(LocalDateTime.now().format(formatoData)).append("\n");
        conteudo.append(SEPARADOR).append("\n");
        
        // Dados do endereço
        conteudo.append("CEP: ").append(formatarCep(cep)).append("\n");
        conteudo.append("Logradouro: ").append(endereco.getOrDefault("logradouro", "")).append("\n");
        conteudo.append("Bairro: ").append(endereco.getOrDefault("bairro", "")).append("\n");
        conteudo.append("Cidade: ").append(endereco.getOrDefault("localidade", "")).append("\n");
        conteudo.append("UF: ").append(endereco.getOrDefault("uf", "")).append("\n");
        conteudo.append("Complemento: ").append(endereco.getOrDefault("complemento", "")).append("\n");
        conteudo.append("\n");
        
        
        Files.write(caminhoArquivo, conteudo.toString().getBytes(), 
                   StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    
   
    public void salvarEnderecos(List<Map<String, String>> enderecos) throws IOException {
        if (enderecos == null || enderecos.isEmpty()) {
            return;
        }
        
        for (Map<String, String> endereco : enderecos) {
            String cep = endereco.get("cep");
            if (cep != null) {
                salvarEndereco(cep, endereco);
            }
        }
    }
    
    
    public String lerHistorico() throws IOException {
        Path caminhoArquivo = getCaminhoCompleto();
        
        if (!Files.exists(caminhoArquivo)) {
            return "Nenhum histórico de consultas encontrado.";
        }
        
        return Files.readString(caminhoArquivo);
    }
    
    
    public void limparHistorico() throws IOException {
        Path caminhoArquivo = getCaminhoCompleto();
        Files.deleteIfExists(caminhoArquivo);
    }
    
    
    public boolean arquivoExiste() {
        return Files.exists(getCaminhoCompleto());
    }
    
    
    public String getCaminhoArquivo() {
        return getCaminhoCompleto().toAbsolutePath().toString();
    }
    
    
    public void exportarPara(String caminhoDestino) throws IOException {
        if (caminhoDestino == null || caminhoDestino.trim().isEmpty()) {
            throw new IllegalArgumentException("Caminho de destino não pode ser nulo ou vazio");
        }
        
        Path origem = getCaminhoCompleto();
        Path destino = Paths.get(caminhoDestino);
        
        if (Files.exists(origem)) {
            // Cria diretórios pai se não existirem
            if (destino.getParent() != null) {
                Files.createDirectories(destino.getParent());
            }
            Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
        } else {
            throw new FileNotFoundException("Arquivo de histórico não encontrado: " + origem);
        }
    }
    
   
    private String formatarCep(String cep) {
        if (cep != null && cep.length() == 8) {
            return cep.substring(0, 5) + "-" + cep.substring(5);
        }
        return cep != null ? cep : "";
    }
    
    
    public int contarConsultas() throws IOException {
        Path caminhoArquivo = getCaminhoCompleto();
        
        if (!Files.exists(caminhoArquivo)) {
            return 0;
        }
        
        List<String> linhas = Files.readAllLines(caminhoArquivo);
        int contador = 0;
        
        for (String linha : linhas) {
            if (linha.startsWith("CONSULTA DE CEP")) {
                contador++;
            }
        }
        
        return contador;
    }
    
    
    public List<String> obterUltimasConsultas(int quantidade) throws IOException {
        if (quantidade <= 0) {
            return new ArrayList<>();
        }
        
        Path caminhoArquivo = getCaminhoCompleto();
        List<String> ultimasConsultas = new ArrayList<>();
        
        if (!Files.exists(caminhoArquivo)) {
            return ultimasConsultas;
        }
        
        List<String> linhas = Files.readAllLines(caminhoArquivo);
        List<String> consultas = new ArrayList<>();
        StringBuilder consultaAtual = new StringBuilder();
        
        for (String linha : linhas) {
            if (linha.startsWith(SEPARADOR) && consultaAtual.length() > 0) {
                consultas.add(consultaAtual.toString());
                consultaAtual = new StringBuilder();
            }
            consultaAtual.append(linha).append("\n");
        }
        
       
        if (consultaAtual.length() > 0) {
            consultas.add(consultaAtual.toString());
        }
        
      
        int inicio = Math.max(0, consultas.size() - quantidade);
        return consultas.subList(inicio, consultas.size());
    }
    
    
    private Path getCaminhoCompleto() {
        try {
            Path dir = Paths.get(DIRETORIO_BASE);
            Files.createDirectories(dir);
            return dir.resolve(NOME_ARQUIVO);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar diretório: " + DIRETORIO_BASE, e);
        }
    }
}