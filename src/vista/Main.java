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
                new Libro("L1", "Java POO", 2020, "Juan Perez", 3)); 

        biblioteca.registrarMaterial(
                new Libro("L2", "El arte de la guerra", 1910, "Sun Tzu", 5)); 

        biblioteca.registrarMaterial(
                new Revista("R1", "Tecnologia Hoy", 2024, 5, 2)); 

        biblioteca.registrarUsuario(
                new Estudiante("2025001", "Carlos"));
        
        biblioteca.registrarUsuario(
                new Estudiante("2025002", "Sara"));

        biblioteca.registrarUsuario(
                new Docente("D01", "Maria"));
        
        biblioteca.registrarUsuario(
                new Docente("D02", "Jose")); 

        System.out.println("SOLICITUDES DE PRÉSTAMO:");
        System.out.println(
                biblioteca.prestarMaterial("L1", "2025001"));

        System.out.println(
                biblioteca.prestarMaterial("R1", "D01"));

        System.out.println("\n📋 PRESTAMOS ACTIVOS");
        biblioteca.mostrarPrestamos();

        System.out.println("\n⏪ DEVOLUCION");
        System.out.println(
                biblioteca.devolverMaterial("L1"));
        
        System.out.println("\n📊 ESTADO DEL STOCK ACTUAL EN CONSOLA:");
        biblioteca.mostrarMateriales();
    }
}