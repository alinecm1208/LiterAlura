package com.aline.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "linguagem")
public class Linguagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sigla")
    private String sigla;

    @ManyToMany(mappedBy = "linguagem")
    private List<Livro> livros;

    public Linguagem() {

    }

    public Linguagem(Long id, String sigla) {
        this.id = id;
        this.sigla = sigla;
    }

    public Linguagem(Long id, String sigla, List<Livro> livros) {
        this.id = id;
        this.sigla = sigla;
        this.livros = livros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    @Override
    public String toString() {
        return "Linguagem{" +
                "id=" + id +
                ", sigla='" + sigla + '\'' +
                ", livros=" + livros +
                '}';
    }
}
