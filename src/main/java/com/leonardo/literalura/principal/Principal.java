package com.leonardo.literalura.principal;

import com.leonardo.literalura.model.DadosBusca;
import com.leonardo.literalura.model.DadosLivro;
import com.leonardo.literalura.model.Livro;
import com.leonardo.literalura.service.AutorService;
import com.leonardo.literalura.service.ConsumoAPI;
import com.leonardo.literalura.service.ConverteDados;
import com.leonardo.literalura.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {
    Scanner scanner = new Scanner(System.in);
    ConsumoAPI consumoAPI = new ConsumoAPI();
    ConverteDados converteDados = new ConverteDados();

    private String endereco = "https://gutendex.com/books/?search=";

    @Autowired
    private LivroService livroService;
    @Autowired
    private AutorService autorService;

    public void Menu() {
        int op = -1;
        while (op != 0) {
            System.out.printf("""
                                        
                                        
                       
                    *********** Menu ***********
                                    
                    1 - Buscar livro pelo titulo
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                                    
                    Insira a opção desejada:            
                    """);
            op = scanner.nextInt();
            scanner.nextLine();
            switch (op) {
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEmDeterminadoAno();
                    break;
                case 5:
                    listarLivrosEmDeterminadoIdioma();
                    break;
            }
        }
    }

    private void listarLivrosEmDeterminadoIdioma() {
        System.out.println(""" 
                
                Digite o idioma escolhido:
                pt = português
                es = espanhol
                fr = francês
                en = inglês
                
                """);
        String idioma = scanner.nextLine();
        System.out.println(livroService.listarlivrosPorIdioma(idioma));
    }

    private void listarAutoresVivosEmDeterminadoAno() {
        System.out.println("Digite o ano: ");
        var ano = scanner.nextInt();
        System.out.println(autorService.listarAutoresVivosAno(ano));
    }

    private void listarAutoresRegistrados() {
        System.out.println(livroService.listarAutores());
    }

    private void listarLivrosRegistrados() {
        System.out.println(livroService.listarLivros());
    }

    private void buscarLivro() {
        System.out.println("Digite o nome do livro: ");
        String buscaLivro = scanner.nextLine().trim();
        var json = consumoAPI.chamarAPI(endereco + buscaLivro.replace(" ", "+"));
        DadosBusca dadosBusca = converteDados.obterDados(json, DadosBusca.class);
        if (dadosBusca.results().isEmpty()){
            System.out.println("Não encontrado");
        }
        for (DadosLivro dadosLivro : dadosBusca.results()) {
            System.out.println(dadosLivro);
            livroService.salvarLivro(dadosLivro);
        }
    }

}