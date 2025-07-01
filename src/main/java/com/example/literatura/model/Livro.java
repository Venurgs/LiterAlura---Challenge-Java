package com.example.literatura.model;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private String idioma;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Autor autor;

    public Livro() {}

    public Livro(String titulo, String idioma, Autor autor) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "------ LIBRO ------\n" +
                "Título: " + titulo + "\n" +
                "Autor: " + (autor != null ? autor.getNome() : "Desconocido") + "\n" +
                "Idioma: " + idioma + "\n" +
                "-------------------";
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }
}