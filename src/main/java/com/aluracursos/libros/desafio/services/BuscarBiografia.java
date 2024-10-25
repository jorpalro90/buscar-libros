package com.aluracursos.libros.desafio.services;

import com.aluracursos.libros.desafio.model.DatosAutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class BuscarBiografia {

    private final ObjectMapper conversor = new ObjectMapper(); // Asegúrate de tener esto

    public DatosAutor buscar(String url) throws IOException {
        // Hacer la solicitud HTTP
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 200) { // Si la respuesta es OK
            InputStream responseStream = connection.getInputStream();
            // Deserializar el JSON en un objeto de DatosAutor
            return conversor.readValue(responseStream, DatosAutor.class); // Cambiar a DatosAutor
        } else {
            throw new IOException("Error en la solicitud: " + connection.getResponseCode());
        }
    }
}


