package com.alurachallenge.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) //para ignorar propierdades que no vamos a mapear
public record DatosBusqueda(
        @JsonAlias("results") List<DatosLibros> resultados) {
}
