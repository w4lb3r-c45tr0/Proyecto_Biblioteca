package vista;

import modelo.Docente;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Revista;
import servicios.Biblioteca;

public class Main {

    public static void main(String[] args) {

        Biblioteca biblioteca = new Biblioteca();

        biblioteca.registrarMaterial(
                new Libro("L1", "Java POO", 2020, "Juan Perez"));

        biblioteca.registrarMaterial(
                new Revista("R1", "Tecnologia Hoy", 2024, 5));

        biblioteca.registrarUsuario(
                new Estudiante("2025001", "Carlos"));

        biblioteca.registrarUsuario(
                new Docente("D01", "Maria"));

        System.out.println(
                biblioteca.prestarMaterial("L1", "2025001"));

        System.out.println(
                biblioteca.prestarMaterial("R1", "D01"));

        System.out.println("\nPRESTAMOS ACTIVOS");
        biblioteca.mostrarPrestamos();

        System.out.println("\nDEVOLUCION");

        System.out.println(
                biblioteca.devolverMaterial("L1"));

    }

}