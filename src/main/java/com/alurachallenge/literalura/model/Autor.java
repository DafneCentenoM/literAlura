package com.alurachallenge.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private String fechaDeNacimiento;
    private String fechaMuerte;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libros> libro;

    //CONSTRUCTORES
    public Autor(){}

    public Autor(DatosAutor datosAutor){
        this.nombre=datosAutor.nombre();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaMuerte = datosAutor.fechaMuerte();
    }

    @Override
    public String toString() {
        return "---------- Autor ----------" + '\n' +
                "Nombre: " + nombre + '\n' +
                "Fecha de nacimiento: " + fechaDeNacimiento + '\n' +
                "Fecha de muerte: " + fechaMuerte + '\n' +
                "Libros: " + libro.stream().map(Libros::getTitulo).collect(Collectors.toUnmodifiableList()) + '\n' +
                "---------------------------" + '\n';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(String fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public List<Libros> getLibro() {
        return libro;
    }

    public void setLibro(List<Libros> libro) {
        this.libro = libro;
    }
}
