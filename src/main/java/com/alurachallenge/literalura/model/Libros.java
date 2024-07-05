package com.alurachallenge.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private int numeroDescargas;

    //CONSTRUCTORES
    public Libros(){
    }

    public Libros(DatosLibros datosLibros, Autor autor){
        this.titulo = datosLibros.titulo();
        this.autor = autor;
        this.idioma = Idioma.fromString(datosLibros.idiomas().toString().split(",")[0].trim());
        this.numeroDescargas = datosLibros.numeroDeDescargas();
    }


    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(int numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    @Override
    public String toString() {
        String nombreAutor = (autor != null) ? autor.getNombre(): "Autor desconocido";
        return String.format("**********LIBRO*********%n" +
                            "Titulo:" +"%s%n" +
                            "Autor: %s%n" +
                            "Idioma: %s%n" +
                            "Numero de descargas: " +
                            "%s%n *******************************",
                titulo, nombreAutor, idioma, numeroDescargas);
    }
}//termina class
