package modelo;

public class Estudiante extends Usuario {

    public Estudiante(String carnet, String nombre) {
        super(carnet, nombre);
    }

    @Override
    public int maxPrestamos() {
        return 3;
    }

}