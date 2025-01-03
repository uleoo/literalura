package com.leonardo.literalura.service;

import com.leonardo.literalura.model.Autor;
import com.leonardo.literalura.model.DadosAutor;
import com.leonardo.literalura.model.DadosLivro;
import com.leonardo.literalura.model.Livro;
import com.leonardo.literalura.repository.AutorRepository;
import com.leonardo.literalura.repository.LivroRepository;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivroService {
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public void salvarLivro(DadosLivro dadosLivro) {
        if (livroRepository.existsByTitulo(dadosLivro.titulo())) {
            System.out.println("Este livro já foi cadastrado");
            System.exit(0);
            return;
        }
        Livro livro = new Livro(dadosLivro);
        List<Autor> autores = dadosLivro.autor().stream().map(dadosAutor -> buscarOuSalvarAutor(dadosAutor)).collect(Collectors.toList());
        livro.setAutores(autores);
        livroRepository.save(livro);
        System.out.println("Livro cadastrado com sucesso: " + livro.getTitulo());
    }
    private Autor buscarOuSalvarAutor(DadosAutor dadosAutor) {
        return autorRepository.findByNameAndBirthYearAndDeathYear(dadosAutor.nome(), dadosAutor.nascimento(), dadosAutor.morte()).orElseGet(() -> {
            Autor novoAutor = new Autor();
            novoAutor.setName(dadosAutor.nome());
            novoAutor.setBirth_year(dadosAutor.nascimento());
            novoAutor.setDeath_year(dadosAutor.morte());
            return autorRepository.save(novoAutor);
        });
    }

    @Transactional
    public List<String> listarLivros() {
        List<Livro> livros = livroRepository.findAll();
        return livros.stream().map(Livro::toString).collect(Collectors.toList());
    }

    @Transactional
    public List<String> listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        for (Autor autor : autores) {
            List<Livro> livrosObtidos = obterLivrosPorAutor(autor.getName());
            System.out.println("Autor = " + autor.getName());
            System.out.println("Nascimento = " + autor.getBirth_year());
            System.out.println("Falecimento = " + autor.getDeath_year());
            if (!livrosObtidos.isEmpty()) {
                for (Livro livro : livrosObtidos) {
                    System.out.println("Livros = " + livro.getTitulo());
                }
            } else {
                System.out.println("Nenhum livro encontrado deste autor.");
            }
        }
        return Collections.singletonList(" ");
    }

    @Transactional
    public List<Livro> obterLivrosPorAutor(String autorNome) {
        Autor autor = autorRepository.findByName(autorNome).orElseThrow(() -> new RuntimeException("Autor não encontrado"));
        return livroRepository.findLivrosByAutorId(autor.getId());
    }


    public String listarlivrosPorIdioma(String idioma) {
        List<Livro> livros = livroRepository.findByLinguagem(idioma);
        for (Livro livro : livros) {
            System.out.println("\nLivro = " + livro.getTitulo() + "\n" +
                    "Autor = " + livro.getAutores().get(0).getName() + "\n" +
                    "Linguagem = " + livro.getLinguagem() + "\n" +
                    "Downloads = " + livro.getDownloads());
        }
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para este idioma");
        }
        return " ";
    }
}