package com.aluracursos.libros.desafio.principal;

import com.aluracursos.libros.desafio.model.DatosLibros;
import com.aluracursos.libros.desafio.model.DatosAutor;
import com.aluracursos.libros.desafio.services.BuscarLibro;
import com.aluracursos.libros.desafio.services.BuscarBiografia;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private static final Scanner teclado = new Scanner(System.in);
    private final BuscarLibro buscarLibro = new BuscarLibro();
    private final BuscarBiografia buscarBiografia = new BuscarBiografia();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private final List<String> historialBusqueda = new ArrayList<>();

    public static void main(String[] args) {
        new Principal().muestraElMenu();
    }

    public void muestraElMenu() {
        int opcion;
        do {
            mostrarOpcionesMenu();
            opcion = obtenerOpcionMenu();

            switch (opcion) {
                case 1 -> buscarLibro();
                case 2 -> buscarAutor();
                case 3 -> buscarBiografia();
                case 4 -> mostrarHistorialBusqueda();
                case 5 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 5);
    }

    private void mostrarOpcionesMenu() {
        System.out.println("\n===== MENÚ =====");
        System.out.println("1. Buscar libro");
        System.out.println("2. Buscar autor");
        System.out.println("3. Buscar biografía de autor");
        System.out.println("4. Mostrar historial de búsqueda");
        System.out.println("5. Salir");
    }

    private int obtenerOpcionMenu() {
        System.out.print("Ingrese una opción: ");
        try {
            return Integer.parseInt(teclado.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida. Ingrese un número.");
            return -1; // Valor no válido
        }
    }

    private void buscarLibro() {
        String busqueda = obtenerBusqueda("Ingrese el título o tema del libro: ");
        String url = URL_BASE + "?search=" + normalize(busqueda);
        realizarBusquedaLibros(url, "No se encontraron libros para la búsqueda.");
    }

    private void buscarAutor() {
        String busqueda = obtenerBusqueda("Ingrese el nombre del autor: ");
        String url = URL_BASE + "?search=" + normalize(busqueda);
        realizarBusquedaLibros(url, "No se encontraron libros para el autor.");
    }

    private String obtenerBusqueda(String mensaje) {
        System.out.print(mensaje);
        String busqueda = teclado.nextLine().trim();
        historialBusqueda.add(busqueda); // Agregar al historial
        return busqueda;
    }

    private void realizarBusquedaLibros(String url, String mensajeNoEncontrado) {
        try {
            List<DatosLibros> libros = buscarLibro.buscar(url);
            if (libros.isEmpty()) {
                System.out.println(mensajeNoEncontrado);
            } else {
                System.out.println("\nLibros encontrados:");
                libros.forEach(this::imprimirDetallesLibro);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar: " + e.getMessage());
        }
    }

    private void buscarBiografia() {
        String nombreAutor = obtenerBusqueda("Ingrese el nombre del autor para buscar su biografía: ");
        String url = "https://gutendex.com/books/?search=" + normalize(nombreAutor);

        try {
            DatosAutor autor = buscarBiografia.buscar(url); // Ajustado para recibir un solo objeto
            if (autor == null) {
                System.out.println("No se encontró biografía para el autor: " + nombreAutor);
            } else {
                mostrarBiografia(autor); // Muestra la biografía si existe
            }
        } catch (Exception e) {
            System.err.println("Error al buscar biografía: " + e.getMessage());
        }
    }




    private void mostrarBiografia(DatosAutor autor) {
        if (autor == null) {
            System.out.println("No se encontró información de biografía.");
            return;
        }

        System.out.println("Biografía de " + (autor.nombre() != null ? autor.nombre() : "desconocido"));
        System.out.println("----------------------------");
        System.out.printf("Fecha de Nacimiento: %s%n", formatFecha(autor.fechaNacimiento()));
        System.out.printf("Fecha de Muerte: %s%n", formatFecha(autor.fechaMuerte()));
        System.out.println("Biografía: " + (autor.biografia() != null ? autor.biografia() : "Información no disponible"));
        System.out.println("----------------------------");
    }


    private String formatFecha(String fecha) {
        return fecha != null && !fecha.isEmpty() ? fecha : "Información no disponible";
    }

    private void mostrarHistorialBusqueda() {
        System.out.println("\nHistorial de Búsqueda:");
        if (historialBusqueda.isEmpty()) {
            System.out.println("No hay historial de búsquedas.");
        } else {
            historialBusqueda.forEach(System.out::println);
        }
    }

    private void imprimirDetallesLibro(DatosLibros libro) {
        System.out.println("----------------------------");
        System.out.println("Título: " + libro.titulo());
        libro.autor().forEach(a -> System.out.println("Autor: " + a.nombre()));
        System.out.println("Idiomas: " + String.join(", ", libro.idiomas()));
        System.out.println("Descargas: " + libro.numeroDeDescargas());
        System.out.println("----------------------------");
    }

    private String normalize(String input) {
        return input.trim().toLowerCase().replaceAll("[^a-zA-Z0-9 ]", " "); // Elimina caracteres especiales
    }
}
