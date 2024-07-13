package com.aline.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "titulo")
    private String title;
    @Column(name = "numeroDownload")
    private Double downloadCount;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Autor autor;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "livro_linguagem",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "linguagem_id")
    )
    private List<Linguagem> linguagem;

    public Livro() {
    }

    public Livro(Long id, String title, Double downloadCount, Autor autor, List<Linguagem> linguagem) {
        this.id = id;
        this.title = title;
        this.downloadCount = downloadCount;
        this.autor = autor;
        this.linguagem = linguagem;
    }

    public Livro(Long id, String title, Double downloadCount) {
        this.id = id;
        this.title = title;
        this.downloadCount = downloadCount;
    }

    public Livro(Long id, String title, Double downloadCount, Autor autor) {
        this.id = id;
        this.title = title;
        this.downloadCount = downloadCount;
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Double downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<Linguagem> getLinguagem() {
        return linguagem;
    }

    public void setLinguagem(List<Linguagem> linguagem) {
        this.linguagem = linguagem;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", downloadCount=" + downloadCount +
                ", autor=" + autor +
                ", linguagem=" + linguagem +
                '}';
    }
}
