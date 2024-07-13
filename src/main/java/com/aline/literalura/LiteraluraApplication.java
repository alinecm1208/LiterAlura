package com.aline.literalura;

import com.aline.literalura.menu.Menu;
import com.aline.literalura.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.http.HttpResponse;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    @Autowired
    ApiService apiService;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        boolean loop = true;

        while (loop) {

            System.out.println("-------------");
            System.out.println("Escolha o número de sua opção:");
            System.out.println("1- buscar livro pelo título");
            System.out.println("2- listar livros registrados");
            System.out.println("3- listar autores registrados");
            System.out.println("4- listar autores vivos em um determinado ano");
            System.out.println("5- listar livros em um determinado idioma");
            System.out.println("0- sair");

            //pega o valor que vc digitou entre as opções
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    //1- buscar livro pelo título
                    System.out.println("Insira o nome do livro que você deseja procurar");
                    String nomelivro = scanner.nextLine();
                    apiService.buscarLivro(nomelivro);
                    break;
                case 2:
                    //2- listar livros registrados
                    apiService.listarLivros();
                    break;
                case 3:
                    //3- listar autores registrados
                    apiService.listarAutores();
                    break;
                case 4:
                    //4- listar autores vivos em um determinado ano
                    System.out.println("Insira o ano que deseja pesquisar");
                    String ano = scanner.nextLine();
                    int intAno = Integer.parseInt(ano);
                    apiService.listarAutoresVivosPorAno(intAno);
                    break;
                case 5:
                    //5- listar livros em um determinado idioma
                    System.out.println("Insira o idioma para realizar a busca:");
                    System.out.println("es - espanhol");
                    System.out.println("en - inglês");
                    System.out.println("fr - francês");
                    System.out.println("pt - português");
                    String idioma = scanner.nextLine();
                    apiService.listarLivrosPorIdioma(idioma);
                    break;
                case 0:
                    //0- sair
                    loop = false;
                    System.out.println("Saindo do sistema");
                    continue; // Continue para evitar o fechamento do scanner antes do loop terminar
                default:
                    System.out.println("Digite um valor valido que seja entre 0 e 5");
                    continue; // Continue para evitar o fechamento do scanner antes do loop terminar
            }

        }

    }
}
