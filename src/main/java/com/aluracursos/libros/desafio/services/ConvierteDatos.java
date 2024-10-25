package com.aluracursos.libros.desafio.services;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConvierteDatos {

    private final ObjectMapper mapper = new ObjectMapper();

    public ObjectMapper getMapper() {
        return mapper;
    }

    public <T> T obtenerDatos(String url, JavaType tipo) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error en la consulta: " + response.statusCode());
            }

            // Mostrar la respuesta JSON en consola (para depuración)
            System.out.println("Respuesta JSON: " + response.body());

            return mapper.readValue(response.body(), tipo);
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la respuesta de la API: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Error en la ejecución de la solicitud: " + e.getMessage(), e);
        }
    }
}
