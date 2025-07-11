package service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class EnderecoService {
	private static final String VIA_CEP_URL = "https://viacep.com.br/ws/";
	private final HttpClient httpClient;

	public EnderecoService() {
		this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
	}
	
	
	// Consulta o endereço através do CEP na API do ViaCEP
	public Map<String, String> consultarCep(String cep) throws IOException, InterruptedException {
        if (cep == null || cep.length() != 8 || !cep.matches("\\d{8}")) {
            throw new IllegalArgumentException("CEP deve conter exatamente 8 dígitos numéricos");
        }

        String url = VIA_CEP_URL + cep + "/json/";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Erro na consulta do CEP. Status: " + response.statusCode());
        }

        try {
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            
            // Verifica se o CEP foi encontrado
            if (jsonObject.has("erro") && jsonObject.get("erro").getAsBoolean()) {
                throw new IllegalArgumentException("CEP não encontrado");
            }
            
            // Converte JSON para Map
            Map<String, String> endereco = new HashMap<>();
            endereco.put("logradouro", getStringOrEmpty(jsonObject, "logradouro"));
            endereco.put("bairro", getStringOrEmpty(jsonObject, "bairro"));
            endereco.put("localidade", getStringOrEmpty(jsonObject, "localidade"));
            endereco.put("uf", getStringOrEmpty(jsonObject, "uf"));
            endereco.put("complemento", getStringOrEmpty(jsonObject, "complemento"));
            
            return endereco;
        } catch (JsonSyntaxException e) {
            throw new IOException("Erro ao processar resposta da API: " + e.getMessage());
        }
    }
	
	
    private String getStringOrEmpty(JsonObject json, String key) {
        return json.has(key) && !json.get(key).isJsonNull() ? 
               json.get(key).getAsString() : "";
    }

    
    public boolean validarCep(String cep) {
        if (cep == null) return false;
        
        // Remove caracteres não numéricos
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        
        if (cepLimpo.length() != 8) return false;
        
        // Verifica se não é um CEP com todos os dígitos iguais (ex: 00000000)
        return !cepLimpo.matches("(\\d)\\1{7}");
    }
    

}
