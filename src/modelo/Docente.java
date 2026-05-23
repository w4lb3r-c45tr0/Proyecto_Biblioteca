package modelo;

public class Docente extends Usuario {

    public Docente(String carnet, String nombre) {
        super(carnet, nombre);
    }

    @Override
    public int maxPrestamos() {
        return 5;
    }

}