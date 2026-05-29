package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.Docente;
import modelo.Estudiante;
import modelo.Usuario;
import servicios.Biblioteca;
import java.util.ArrayList;

/**
 * Ventana que muestra todos los usuarios del sistema,
 * separados por tipo: Estudiantes y Docentes.
 */
public class VentanaVerUsuarios extends JFrame {

    private static final long serialVersionUID = 1L;

    public VentanaVerUsuarios(Biblioteca biblioteca) {
        setTitle("Usuarios Registrados");
        setSize(520, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        Color acento = VentanaBiblioteca.C_BTN_INDIGO;

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(VentanaBiblioteca.C_CARD);
        header.setBorder(new EmptyBorder(16, 22, 16, 22));

        JLabel lblTitulo = new JLabel("👥  Usuarios Registrados");
        lblTitulo.setFont(new Font("Garamond", Font.BOLD, 21));
        lblTitulo.setForeground(VentanaBiblioteca.C_WHITE);

        JLabel lblSub = new JLabel("Estudiantes y Docentes en el sistema");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(VentanaBiblioteca.C_GREY);

        JPanel colTitulo = new JPanel();
        colTitulo.setOpaque(false);
        colTitulo.setLayout(new BoxLayout(colTitulo, BoxLayout.Y_AXIS));
        colTitulo.add(lblTitulo);
        colTitulo.add(lblSub);
        header.add(colTitulo, BorderLayout.WEST);

        // ── Contenido del área de texto ───────────────────────────────────────
        JTextArea area = VentanaInventario.buildTextArea(acento);
        ArrayList<Usuario> todos = biblioteca.getUsuarios();

        StringBuilder sb = new StringBuilder();

        // ── Sección Estudiantes ───────────────────────────────────────────────
        sb.append("── ESTUDIANTES ──────────────────────────────────────\n\n");

        int cntEst = 0;
        for (Usuario u : todos) {
            if (u instanceof Estudiante) {
                cntEst++;
                sb.append(String.format("  🎓  Carnet: %-8s   Nombre: %s%n", u.getCarnet(), u.getNombre()));
                sb.append(String.format("       Límite de préstamos: %d simultáneos%n%n", u.maxPrestamos()));
            }
        }
        if (cntEst == 0) sb.append("  (Sin estudiantes registrados)\n\n");

        // ── Sección Docentes ──────────────────────────────────────────────────
        sb.append("── DOCENTES ─────────────────────────────────────────\n\n");

        int cntDoc = 0;
        for (Usuario u : todos) {
            if (u instanceof Docente) {
                cntDoc++;
                sb.append(String.format("  👨‍🏫  Carnet: %-8s   Nombre: %s%n", u.getCarnet(), u.getNombre()));
                sb.append(String.format("       Límite de préstamos: %d simultáneos%n%n", u.maxPrestamos()));
            }
        }
        if (cntDoc == 0) sb.append("  (Sin docentes registrados)\n\n");

        // ── Resumen ───────────────────────────────────────────────────────────
        sb.append("─────────────────────────────────────────────────────\n");
        sb.append(String.format(
            "  Estudiantes: %d   |   Docentes: %d   |   Total: %d%n",
            cntEst, cntDoc, todos.size()));

        area.setText(sb.toString());
        area.setCaretPosition(0);   // scroll al inicio

        JScrollPane scroll = VentanaInventario.styledScroll(area);
        JPanel footer = VentanaInventario.buildFooter(
            "Total: " + todos.size() + " usuario(s) registrado(s)");

        // ── Layout final ──────────────────────────────────────────────────────
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(VentanaBiblioteca.C_BG);
        content.add(header, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);
        content.add(footer, BorderLayout.SOUTH);

        setContentPane(content);
    }
}