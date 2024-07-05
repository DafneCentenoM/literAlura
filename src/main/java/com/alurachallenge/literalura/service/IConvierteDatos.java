package com.alurachallenge.literalura.service;

public interface IConvierteDatos {

    //LAs T son tipos de datos genericos
    <T> T obtenerDatos(String json, Class<T> clase);
}
