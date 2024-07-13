package com.aline.literalura.service;

import com.aline.literalura.model.Autor;
import com.aline.literalura.model.Linguagem;
import com.aline.literalura.model.Livro;
import com.aline.literalura.repositories.AutorRepository;
import com.aline.literalura.repositories.LinguagemRepository;
import com.aline.literalura.repositories.LivroRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ApiService {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Autowired
    LinguagemRepository linguagemRepository;

    public void listarLivrosPorIdioma(String idioma) {

        String idiomaTratado = idioma.toLowerCase(Locale.ROOT).trim();

        if (idiomaTratado.equals("pt") || idiomaTratado.equals("en") || idiomaTratado.equals("fr") || idiomaTratado.equals("es")) {
            List<Linguagem> linguagemList = linguagemRepository.findBySigla(idiomaTratado);
            List<Livro> livros = livroRepository.findByLinguagem(linguagemList);
            livros.forEach(livro -> imprimirLivro(livro));
        }
         else {
            System.out.println("escolha entre as opções de idiomas validos en, fr, pt e es");
        }
    }

    public void listarAutoresVivosPorAno(int intAno) {
        List<Autor> autores = autorRepository.findAutoreVivosByAno(intAno);
        if (autores != null && !autores.isEmpty()) {
            for (Autor autor: autores){
                imprimirAutor(autor);
            }
        } else {
            System.out.println("Não temos autores vivos para o ano "+ intAno +" informado");
        }
    }

    public void listarAutores() {
        List<Autor> autors = autorRepository.findAll();
        autors.forEach(autor -> imprimirAutor(autor));

    }

    private void imprimirAutor(Autor autor) {
        List<String> titulosLivros = new ArrayList<>();

        System.out.println("Autor: "+autor.getName());
        System.out.println("Ano de nascimento: "+autor.getBirthYear());
        System.out.println("Ano de falecimento: "+autor.getDeathYear());
        autor.getLivros().forEach(livro -> titulosLivros.add(livro.getTitle()));
        System.out.println("Livros: " + titulosLivros);
        System.out.println("");
    }

    public void listarLivros() {
        List<Livro> livros = livroRepository.findAll();
        livros.forEach(livro -> imprimirLivro(livro));
    }

    public void buscarLivro(String nomeLivro) throws URISyntaxException, UnsupportedEncodingException, JsonProcessingException {

        HttpResponse<String> response = getStringHttpResponse(nomeLivro);
        JsonNode resultsNode = getResultsNode(response);

        JsonNode jsonNode = resultsNode.get(0);

        if (!jsonNode.isEmpty()) {
            Livro livro = new Livro();
            Autor autor = new Autor();
            Linguagem linguagem = new Linguagem();

            livro.setTitle(jsonNode.get("title").toString().replace("\"",""));
            livro.setDownloadCount(Double.parseDouble(jsonNode.get("download_count").toString()));
            livro.setAutor(getAutor(jsonNode,autor));
            livro.setLinguagem(getLanguagem(jsonNode,linguagem));
            salvarLivro(livro);

            imprimirLivro(livro);
        }

    }

    private void imprimirLivro(Livro livro) {
        System.out.println("----- LIVRO -----");
        System.out.println("Título: "+ livro.getTitle());
        System.out.println("Autor: "+ livro.getAutor().getName().replace("\"",""));
        System.out.println("Idioma: "+ livro.getLinguagem().get(0).getSigla().replace("\"",""));
        System.out.println("Número de downloads: "+ livro.getDownloadCount());
        System.out.println("------------------------");
    }

    private void salvarLivro(Livro livro) {

        // se já houver no banco de dados o título do livro salvo ele não salva de novo
        boolean livroExiste = livroRepository.existsByTitle(livro.getTitle());

        if (livroExiste) {
            System.out.println("Livro já esta salvo na base de dados");
        } else {
            livroRepository.save(livro);
        }
    }

    private List<Linguagem> getLanguagem(JsonNode jsonNode, Linguagem linguagem) {
        List<Linguagem> linguagems = new ArrayList<>();
        jsonNode.get("languages").forEach(jsonNode1 -> {
            linguagem.setSigla(jsonNode1.toString().replace("\"",""));
            linguagems.add(linguagem);

        });
        return linguagems;
    }

    private Autor getAutor(JsonNode json, Autor autor) {
        json.get("authors").forEach(jsonNode -> {
            autor.setName(jsonNode.get("name").toString().replace("\"",""));
            autor.setBirthYear(Integer.parseInt(jsonNode.get("birth_year").toString()));
            autor.setDeathYear(Integer.parseInt(jsonNode.get("death_year").toString()));
        });
        return autor;
    }

    private static JsonNode getResultsNode(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.body());
        JsonNode resultsNode = rootNode.path("results");
        return resultsNode;
    }

    private HttpResponse<String> getStringHttpResponse(String nomeLivro) throws UnsupportedEncodingException, URISyntaxException {
        String encodedNomeLivro = URLEncoder.encode(nomeLivro, StandardCharsets.UTF_8.toString());

        // Construa a URL de forma segura
        String baseUrl = "https://gutendex.com/books/?search=";
        String fullUrl = baseUrl + encodedNomeLivro;

        URI path = new URI(fullUrl);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(path)
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient
                    .newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
