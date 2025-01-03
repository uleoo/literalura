package com.leonardo.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String linguagem;
    private Long downloads;

    public Livro() {
    }

    @Override
    public String toString() {
        return "\n------\nTitulo    = " + titulo + "\n" +
                "Autores   = " + autores.getFirst().getName() + "\n" +
                "Linguagem = " + linguagem + "\n" +
                "Downloads = " + downloads + "\n" + "------\n\n";
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "livros_autores",
            joinColumns = @JoinColumn(name = "livros_id"),
            inverseJoinColumns = @JoinColumn(name = "autores_id")
    )
    private List<Autor> autores;

    public Livro(DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();
        this.autores = new ArrayList<>();
        this.linguagem = String.valueOf(dadosLivro.linguagem());
        this.downloads = dadosLivro.downloads();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLinguagem() {
        return linguagem;
    }

    public void setLinguagem(String linguagem) {
        this.linguagem = linguagem;
    }

    public Long getDownloads() {
        return downloads;
    }

    public void setDownloads(Long downloads) {
        this.downloads = downloads;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }
}