package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import servicios.Biblioteca;

/**
 * Ventana secundaria: Inventario de materiales.
 * Hereda la paleta de VentanaBiblioteca para coherencia visual.
 */
public class VentanaInventario extends JFrame {

    private static final long serialVersionUID = 1L;

    public VentanaInventario(Biblioteca biblioteca) {
        setTitle("Inventario — Biblioteca UMG");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(620, 460);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(VentanaBiblioteca.C_BG);
        root.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(root);

        // ── Header de color ──────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Degradado horizontal ámbar-oscuro
                GradientPaint gp = new GradientPaint(0,0,
                    new Color(180,100,0), getWidth(), 0, new Color(30,20,5));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(0, 72));
        header.setBorder(new EmptyBorder(14, 28, 14, 28));

        JLabel ico   = new JLabel("📦");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        JLabel lTitle = new JLabel("Inventario de Materiales");
        lTitle.setFont(new Font("Garamond", Font.BOLD, 22));
        lTitle.setForeground(Color.WHITE);
        JLabel lSub   = new JLabel("Stock actual en estanterías");
        lSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lSub.setForeground(new Color(255,220,160));
        titleGroup.add(lTitle);
        titleGroup.add(lSub);

        header.add(ico, BorderLayout.WEST);
        header.add(titleGroup, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);

        // ── Área de contenido ────────────────────────────────────────────────
        JTextArea area = buildTextArea(VentanaBiblioteca.C_BTN_AMBER);
        JScrollPane scroll = styledScroll(area);
        scroll.setBorder(new EmptyBorder(0,0,0,0));

        JPanel bodyWrap = new JPanel(new BorderLayout());
        bodyWrap.setBackground(VentanaBiblioteca.C_BG);
        bodyWrap.setBorder(new EmptyBorder(20, 24, 20, 24));
        bodyWrap.add(scroll, BorderLayout.CENTER);
        root.add(bodyWrap, BorderLayout.CENTER);

        // ── Footer ───────────────────────────────────────────────────────────
        root.add(buildFooter("Inventario cargado  •  Solo lectura"), BorderLayout.SOUTH);

        // ── Cargar datos ─────────────────────────────────────────────────────
        String resultado = capturarSysOut(biblioteca::mostrarMateriales);
        area.setText(resultado.trim().isEmpty()
            ? "> No hay materiales registrados en el sistema."
            : resultado);
        area.setCaretPosition(0);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Helpers compartidos
    // ─────────────────────────────────────────────────────────────────────────

    static JTextArea buildTextArea(Color fgColor) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(VentanaBiblioteca.C_CARD);
        area.setForeground(fgColor);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setMargin(new Insets(16, 18, 16, 18));
        area.setCaretColor(fgColor);
        area.setLineWrap(false);
        return area;
    }

    static JScrollPane styledScroll(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBackground(VentanaBiblioteca.C_CARD);
        scroll.getViewport().setBackground(VentanaBiblioteca.C_CARD);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 52, 80), 1),
            BorderFactory.createEmptyBorder()
        ));
        // Scrollbar minimalista
        scroll.getVerticalScrollBar().setBackground(new Color(15, 20, 35));
        scroll.getVerticalScrollBar().setForeground(new Color(60, 75, 110));
        return scroll;
    }

    static JPanel buildFooter(String text) {
        JPanel foot = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(8, 12, 22));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        foot.setPreferredSize(new Dimension(0, 36));
        foot.setBorder(new EmptyBorder(0, 24, 0, 24));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lbl.setForeground(new Color(60, 70, 95));
        foot.add(lbl, BorderLayout.WEST);
        JLabel umg = new JLabel("BIBLIOTECA UMG");
        umg.setFont(new Font("Segoe UI", Font.BOLD, 10));
        umg.setForeground(new Color(212, 175, 95, 80));
        foot.add(umg, BorderLayout.EAST);
        return foot;
    }

    /** Captura la salida estándar de un Runnable y la devuelve como String */
    static String capturarSysOut(Runnable accion) {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream old = System.out;
        System.setOut(ps);
        try { accion.run(); } finally {
            System.out.flush();
            System.setOut(old);
        }
        return baos.toString();
    }
}