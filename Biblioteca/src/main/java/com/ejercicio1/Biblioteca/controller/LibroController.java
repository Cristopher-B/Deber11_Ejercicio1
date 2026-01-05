package com.ejercicio1.Biblioteca.controller;

import com.ejercicio1.Biblioteca.model.Libro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/libros")
public class LibroController {

    private List<Libro> listaLibros = new ArrayList<>();

    // Constructor para simular algunos libros de ejemplo
    public LibroController() {
        listaLibros.add(new Libro(1L, "978-3-16-148410-0", "El Quijote", "Miguel de Cervantes", 1605, true));
        listaLibros.add(new Libro(2L, "978-0-7432-7356-5", "Cien años de soledad", "Gabriel García Márquez", 1967, false));
        listaLibros.add(new Libro(3L, "978-1-86197-876-9", "1984", "George Orwell", 1949, true));
    }

    // 1. Obtener el listado completo de libros
    @GetMapping
    public ResponseEntity<List<Libro>> obtenerLibros() {
        return new ResponseEntity<>(listaLibros, HttpStatus.OK);
    }

    // 2. Obtener un libro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibro(@PathVariable Long id) {
        return listaLibros.stream()
                .filter(libro -> libro.getId().equals(id))
                .findFirst()
                .map(libro -> new ResponseEntity<>(libro, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 3. Buscar libros por autor y opcionalmente por año
    @GetMapping("/buscar")
    public ResponseEntity<List<Libro>> buscarLibros(@RequestParam String autor, @RequestParam(required = false) Integer anio) {
        List<Libro> librosFiltrados = listaLibros.stream()
                .filter(libro -> libro.getAutor().equalsIgnoreCase(autor))
                .filter(libro -> anio == null || libro.getAnioPublicacion() == anio)
                .collect(Collectors.toList());

        return new ResponseEntity<>(librosFiltrados, HttpStatus.OK);
    }

    // 4. Registrar un nuevo libro
    @PostMapping
    public ResponseEntity<Libro> registrarLibro(@RequestBody Libro nuevoLibro) {
        nuevoLibro.setId((long) (listaLibros.size() + 1)); // Generar un ID secuencial simple
        listaLibros.add(nuevoLibro);
        return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
    }
}
