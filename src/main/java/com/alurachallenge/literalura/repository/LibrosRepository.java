package com.alurachallenge.literalura.repository;

import com.alurachallenge.literalura.model.Idioma;
import com.alurachallenge.literalura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//Este repositorio es para realizar operaciones CRUD(Create, Read, Update, Delete)
//se llama a la clase con la entidad que vamos a querer mapear
public interface LibrosRepository extends JpaRepository<Libros, Long> {
    List<Libros> findByIdioma(Idioma idioma);
    Optional<Libros> findByTitulo(String titulo);




}
