package modelo;

public abstract class Material {

    private String codigo;
    private String titulo;
    private int anio;
    private int totalCopias;
    private int copiasDisponibles;

    public Material(String codigo, String titulo, int anio, int totalCopias) {
        setCodigo(codigo);
        setTitulo(titulo);
        setAnio(anio);
        setTotalCopias(totalCopias);
        this.copiasDisponibles = totalCopias; // Al registrar, todas están disponibles
    }

    public abstract int diasPrestamoMaximo();

    public boolean estaDisponible() {
        return copiasDisponibles > 0;
    }

    public void prestar() {
        if (!estaDisponible()) {
            throw new IllegalStateException("No hay copias disponibles de: " + titulo);
        }
        copiasDisponibles--;
    }

    public void devolver() {
        if (copiasDisponibles >= totalCopias) {
            throw new IllegalStateException("Todas las copias ya están en la estantería: " + titulo);
        }
        copiasDisponibles++;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) {
        if(codigo == null || codigo.isEmpty()) throw new IllegalArgumentException("Codigo invalido");
        this.codigo = codigo;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) {
        if(titulo == null || titulo.isEmpty()) throw new IllegalArgumentException("Titulo invalido");
        this.titulo = titulo;
    }

    public int getAnio() { return anio; }
    public void setAnio(int anio) {
        if(anio < 1900 || anio > 2026) throw new IllegalArgumentException("Anio invalido");
        this.anio = anio;
    }

    public int getTotalCopias() { return totalCopias; }
    public void setTotalCopias(int totalCopias) {
        if(totalCopias <= 0) throw new IllegalArgumentException("Las copias deben ser mayor a 0");
        this.totalCopias = totalCopias;
    }

    public int getCopiasDisponibles() { return copiasDisponibles; }
}