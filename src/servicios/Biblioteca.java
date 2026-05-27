package servicios;

import java.util.ArrayList;
import modelo.Material;
import modelo.Prestamo;
import modelo.Usuario;

public class Biblioteca {

    private ArrayList<Material> materiales;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Prestamo> prestamos;

    public Biblioteca() {
        materiales = new ArrayList<>();
        usuarios = new ArrayList<>();
        prestamos = new ArrayList<>();
    }

    public void registrarMaterial(Material material) {
        materiales.add(material);
    }

    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Material buscarMaterial(String codigo) {
        for(Material m : materiales) {
            if(m.getCodigo().equalsIgnoreCase(codigo)) {
                return m;
            }
        }
        return null;
    }

    public Usuario buscarUsuario(String carnet) {
        for(Usuario u : usuarios) {
            if(u.getCarnet().equalsIgnoreCase(carnet)) {
                return u;
            }
        }
        return null;
    }

    public int prestamosActivosUsuario(Usuario usuario) {
        int contador = 0;
        for(Prestamo p : prestamos) {
            if(p.getUsuario().equals(usuario) && p.isActivo()) {
                contador++;
            }
        }
        return contador;
    }

    public String prestarMaterial(String codigo, String carnet) {
        Material material = buscarMaterial(codigo);
        Usuario usuario = buscarUsuario(carnet);

        if(material == null) return "Material no encontrado";
        if(usuario == null) return "Usuario no encontrado";
        
        // 🔥 CAMBIO: Verifica si quedan copias usando el nuevo método numérico
        if(!material.estaDisponible()) {
            return "Material no disponible (Sin copias en stock)";
        }

        if(prestamosActivosUsuario(usuario) >= usuario.maxPrestamos()) {
            return "Limite de prestamos alcanzado";
        }

        Prestamo prestamo = new Prestamo(material, usuario);
        prestamos.add(prestamo);

        material.prestar();

        return "Prestamo realizado";
    }

    public String devolverMaterial(String codigo) {
        for(Prestamo p : prestamos) {
            if(p.getMaterial().getCodigo().equalsIgnoreCase(codigo) && p.isActivo()) {
                p.cerrarPrestamo();
                
                p.getMaterial().devolver();

                return "Material devuelto";
            }
        }
        return "Prestamo no encontrado";
    }

    public void mostrarMateriales() {
        for(Material m : materiales) {
            System.out.println(m);
        }
    }

    public void mostrarPrestamos() {
        for(Prestamo p : prestamos) {
            if(p.isActivo()) {
                System.out.println(p);
            }
        }
    }
}