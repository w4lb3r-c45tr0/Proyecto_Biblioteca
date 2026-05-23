package modelo;

public abstract class Usuario {

    private String carnet;
    private String nombre;

    public Usuario(String carnet, String nombre) {

        this.carnet = carnet;
        this.nombre = nombre;
    }

    public abstract int maxPrestamos();

    public String getCarnet() {
        return carnet;
    }

    public String getNombre() {
        return nombre;
    }

}