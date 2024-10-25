package com.aluracursos.libros.desafio.services;

import com.aluracursos.libros.desafio.model.DatosLibros;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class BuscarLibro {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<DatosLibros> buscar(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parsear la respuesta JSON
            JsonNode root = mapper.readTree(response.body());
            JsonNode results = root.get("results");

            List<DatosLibros> libros = new ArrayList<>();
            if (results != null && results.isArray()) {
                for (JsonNode node : results) {
                    DatosLibros libro = mapper.treeToValue(node, DatosLibros.class);
                    libros.add(libro);
                }
            }

            return libros;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al buscar el libro: " + e.getMessage(), e);
        }
    }
}
