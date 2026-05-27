package persistencia;
 
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
 
import modelo.*;
import servicios.Biblioteca;
 

public class GestorDatos {
 
    // ── Rutas de archivos ─────────────────────────────────────────────────────
    private static final String CARPETA           = "datos";
    private static final String ARCHIVO_MATERIALES = CARPETA + "/materiales.csv";
    private static final String ARCHIVO_USUARIOS   = CARPETA + "/usuarios.csv";
    private static final String ARCHIVO_PRESTAMOS  = CARPETA + "/prestamos.csv";
 
    // Separador: usamos | para evitar conflictos con comas en títulos/nombres
    private static final String SEP = "|";
 
    // =========================================================================
    //  GUARDAR TODO
    // =========================================================================
 
   
    public static void guardar(Biblioteca biblioteca) {
        crearCarpeta();
        guardarMateriales(biblioteca.getMateriales());
        guardarUsuarios(biblioteca.getUsuarios());
        guardarPrestamos(biblioteca.getPrestamos());
    }
 
    // =========================================================================
    //  CARGAR TODO
    // =========================================================================
 
    
    public static void cargar(Biblioteca biblioteca) {
        if (!existenArchivos()) return;
 
        ArrayList<Material> materiales = cargarMateriales();
        ArrayList<Usuario>  usuarios   = cargarUsuarios();
        ArrayList<Prestamo> prestamos  = cargarPrestamos(materiales, usuarios);
 
        for (Material m : materiales) biblioteca.registrarMaterial(m);
        for (Usuario  u : usuarios)   biblioteca.registrarUsuario(u);
        for (Prestamo p : prestamos)  biblioteca.registrarPrestamo(p);
    }
 
    // =========================================================================
    //  GUARDAR — métodos privados por entidad
    // =========================================================================
 
    private static void guardarMateriales(ArrayList<Material> materiales) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_MATERIALES))) {
 
            pw.println("tipo|codigo|titulo|anio|totalCopias|copiasDisponibles|extra");
 
            for (Material m : materiales) {
                if (m instanceof Libro) {
                    Libro l = (Libro) m;
                    pw.printf("LIBRO%s%s%s%s%s%d%s%d%s%d%s%s%n",
                        SEP, l.getCodigo(),
                        SEP, l.getTitulo(),
                        SEP, l.getAnio(),
                        SEP, l.getTotalCopias(),
                        SEP, l.getCopiasDisponibles(),
                        SEP, l.getAutor());
 
                } else if (m instanceof Revista) {
                    Revista r = (Revista) m;
                    pw.printf("REVISTA%s%s%s%s%s%d%s%d%s%d%s%d%n",
                        SEP, r.getCodigo(),
                        SEP, r.getTitulo(),
                        SEP, r.getAnio(),
                        SEP, r.getTotalCopias(),
                        SEP, r.getCopiasDisponibles(),
                        SEP, r.getNumeroEdicion());
                }
            }
 
        } catch (IOException e) {
            System.err.println("[GestorDatos] Error al guardar materiales: " + e.getMessage());
        }
    }
 
    private static void guardarUsuarios(ArrayList<Usuario> usuarios) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_USUARIOS))) {
 
            pw.println("tipo|carnet|nombre");
 
            for (Usuario u : usuarios) {
                String tipo = (u instanceof Docente) ? "DOCENTE" : "ESTUDIANTE";
                pw.printf("%s%s%s%s%s%n",
                    tipo,
                    SEP, u.getCarnet(),
                    SEP, u.getNombre());
            }
 
        } catch (IOException e) {
            System.err.println("[GestorDatos] Error al guardar usuarios: " + e.getMessage());
        }
    }
 
    private static void guardarPrestamos(ArrayList<Prestamo> prestamos) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_PRESTAMOS))) {
 
            pw.println("codigoMaterial|carnetUsuario|fechaPrestamo|activo");
 
            for (Prestamo p : prestamos) {
                pw.printf("%s%s%s%s%s%s%b%n",
                    p.getMaterial().getCodigo(),
                    SEP, p.getUsuario().getCarnet(),
                    SEP, p.getFechaPrestamo().toString(),
                    SEP, p.isActivo());
            }
 
        } catch (IOException e) {
            System.err.println("[GestorDatos] Error al guardar préstamos: " + e.getMessage());
        }
    }
 
    // =========================================================================
    //  CARGAR — métodos privados por entidad
    // =========================================================================
 
    private static ArrayList<Material> cargarMateriales() {
        ArrayList<Material> lista = new ArrayList<>();
 
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_MATERIALES))) {
            br.readLine(); // saltar cabecera
 
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
 
                String[] p = linea.split("\\" + SEP);
                if (p.length < 7) continue;
 
                String tipo              = p[0].trim();
                String codigo            = p[1].trim();
                String titulo            = p[2].trim();
                int    anio              = Integer.parseInt(p[3].trim());
                int    totalCopias       = Integer.parseInt(p[4].trim());
                int    copiasDisponibles = Integer.parseInt(p[5].trim());
                String extra             = p[6].trim();
 
                if (tipo.equals("LIBRO")) {
                    Libro l = new Libro(codigo, titulo, anio, extra, totalCopias);
                    // Ajustar copias disponibles (prestar tantas veces como indique el CSV)
                    int prestadas = totalCopias - copiasDisponibles;
                    for (int i = 0; i < prestadas; i++) l.prestar();
                    lista.add(l);
 
                } else if (tipo.equals("REVISTA")) {
                    int edicion = Integer.parseInt(extra);
                    Revista r = new Revista(codigo, titulo, anio, edicion, totalCopias);
                    int prestadas = totalCopias - copiasDisponibles;
                    for (int i = 0; i < prestadas; i++) r.prestar();
                    lista.add(r);
                }
            }
 
        } catch (IOException e) {
            System.err.println("[GestorDatos] Error al cargar materiales: " + e.getMessage());
        }
 
        return lista;
    }
 
    private static ArrayList<Usuario> cargarUsuarios() {
        ArrayList<Usuario> lista = new ArrayList<>();
 
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_USUARIOS))) {
            br.readLine(); // saltar cabecera
 
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
 
                String[] p = linea.split("\\" + SEP);
                if (p.length < 3) continue;
 
                String tipo   = p[0].trim();
                String carnet = p[1].trim();
                String nombre = p[2].trim();
 
                if (tipo.equals("DOCENTE")) {
                    lista.add(new Docente(carnet, nombre));
                } else {
                    lista.add(new Estudiante(carnet, nombre));
                }
            }
 
        } catch (IOException e) {
            System.err.println("[GestorDatos] Error al cargar usuarios: " + e.getMessage());
        }
 
        return lista;
    }
 
    private static ArrayList<Prestamo> cargarPrestamos(
            ArrayList<Material> materiales,
            ArrayList<Usuario> usuarios) {
 
        ArrayList<Prestamo> lista = new ArrayList<>();
 
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PRESTAMOS))) {
            br.readLine(); // saltar cabecera
 
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
 
                String[] p = linea.split("\\" + SEP);
                if (p.length < 4) continue;
 
                String  codigoMat = p[0].trim();
                String  carnet    = p[1].trim();
                LocalDate fecha   = LocalDate.parse(p[2].trim());
                boolean activo    = Boolean.parseBoolean(p[3].trim());
 
                // Buscar el material y usuario correspondientes
                Material mat = null;
                Usuario  usr = null;
 
                for (Material m : materiales)
                    if (m.getCodigo().equalsIgnoreCase(codigoMat)) { mat = m; break; }
 
                for (Usuario u : usuarios)
                    if (u.getCarnet().equalsIgnoreCase(carnet)) { usr = u; break; }
 
                if (mat != null && usr != null) {
                    lista.add(new Prestamo(mat, usr, fecha, activo));
                }
            }
 
        } catch (IOException e) {
            System.err.println("[GestorDatos] Error al cargar préstamos: " + e.getMessage());
        }
 
        return lista;
    }
 
    // =========================================================================
    //  UTILIDADES
    // =========================================================================
 
    private static void crearCarpeta() {
        File carpeta = new File(CARPETA);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }
 
    /** Retorna true si los archivos de datos ya existen en disco */
    public static boolean existenArchivos() {
        return new File(ARCHIVO_MATERIALES).exists()
            && new File(ARCHIVO_USUARIOS).exists();
    }
}
 