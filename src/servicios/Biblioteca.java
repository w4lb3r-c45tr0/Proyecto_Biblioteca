package servicios;
 
import java.util.ArrayList;
import modelo.Material;
import modelo.Prestamo;
import modelo.Usuario;
 
public class Biblioteca {
 
    private ArrayList<Material> materiales;
    private ArrayList<Usuario>  usuarios;
    private ArrayList<Prestamo> prestamos;
 
    public Biblioteca() {
        materiales = new ArrayList<>();
        usuarios   = new ArrayList<>();
        prestamos  = new ArrayList<>();
    }
 
    // ── Registro ──────────────────────────────────────────────────────────────
 
    public void registrarMaterial(Material material) { materiales.add(material); }
    public void registrarUsuario(Usuario usuario)    { usuarios.add(usuario);   }
 
    /** Usado por GestorDatos al cargar desde CSV (no modifica stock). */
    public void registrarPrestamo(Prestamo prestamo) { prestamos.add(prestamo); }
 
    // ── Búsqueda ──────────────────────────────────────────────────────────────
 
    public Material buscarMaterial(String codigo) {
        for (Material m : materiales)
            if (m.getCodigo().equalsIgnoreCase(codigo)) return m;
        return null;
    }
 
    public Usuario buscarUsuario(String carnet) {
        for (Usuario u : usuarios)
            if (u.getCarnet().equalsIgnoreCase(carnet)) return u;
        return null;
    }
 
    // ── Lógica de negocio ─────────────────────────────────────────────────────
 
    public int prestamosActivosUsuario(Usuario usuario) {
        int cont = 0;
        for (Prestamo p : prestamos)
            if (p.getUsuario().equals(usuario) && p.isActivo()) cont++;
        return cont;
    }
 
    public String prestarMaterial(String codigo, String carnet) {
        Material material = buscarMaterial(codigo);
        Usuario  usuario  = buscarUsuario(carnet);
        if (material == null) return "Material no encontrado";
        if (usuario  == null) return "Usuario no encontrado";
        if (!material.estaDisponible()) return "Material no disponible (sin copias en stock)";
        if (prestamosActivosUsuario(usuario) >= usuario.maxPrestamos()) return "Límite de préstamos alcanzado";
        prestamos.add(new Prestamo(material, usuario));
        material.prestar();
        return "Préstamo realizado";
    }
 
    public String devolverMaterial(String codigo) {
        for (Prestamo p : prestamos)
            if (p.getMaterial().getCodigo().equalsIgnoreCase(codigo) && p.isActivo()) {
                p.cerrarPrestamo(); p.getMaterial().devolver();
                return "Material devuelto";
            }
        return "Préstamo no encontrado";
    }
 
    // ── Eliminar ──────────────────────────────────────────────────────────────
 
    /**
     * Elimina un material del catálogo.
     * No permite eliminarlo si tiene préstamos activos.
     */
    public String eliminarMaterial(String codigo) {
        Material mat = buscarMaterial(codigo);
        if (mat == null) return "Material no encontrado.";
 
        for (Prestamo p : prestamos)
            if (p.getMaterial().getCodigo().equalsIgnoreCase(codigo) && p.isActivo())
                return "No se puede eliminar: el material tiene préstamos activos.";
 
        materiales.remove(mat);
        return "Material eliminado correctamente.";
    }
 
    /**
     * Elimina un usuario del sistema.
     * No permite eliminarlo si tiene préstamos activos.
     */
    public String eliminarUsuario(String carnet) {
        Usuario usr = buscarUsuario(carnet);
        if (usr == null) return "Usuario no encontrado.";
 
        for (Prestamo p : prestamos)
            if (p.getUsuario().getCarnet().equalsIgnoreCase(carnet) && p.isActivo())
                return "No se puede eliminar: el usuario tiene préstamos activos.";
 
        usuarios.remove(usr);
        return "Usuario eliminado correctamente.";
    }
 
    // ── Mostrar (usado por ventanas de inventario) ────────────────────────────
 
    public void mostrarMateriales() {
        for (Material m : materiales) System.out.println(m);
    }
 
    public void mostrarPrestamos() {
        for (Prestamo p : prestamos) if (p.isActivo()) System.out.println(p);
    }
 
    // ── Getters para GestorDatos ──────────────────────────────────────────────
 
    public ArrayList<Material> getMateriales() { return materiales; }
    public ArrayList<Usuario>  getUsuarios()   { return usuarios;   }
    public ArrayList<Prestamo> getPrestamos()  { return prestamos;  }
}
 