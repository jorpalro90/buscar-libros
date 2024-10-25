package com.aluracursos.libros.desafio.services;

import com.fasterxml.jackson.databind.JavaType;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, JavaType tipo);
}
