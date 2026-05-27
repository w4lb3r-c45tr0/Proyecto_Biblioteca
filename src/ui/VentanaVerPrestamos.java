package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import servicios.Biblioteca;

/**
 * Ventana secundaria: Historial de préstamos activos.
 * Hereda helpers y paleta de VentanaInventario / VentanaBiblioteca.
 */
public class VentanaVerPrestamos extends JFrame {

    private static final long serialVersionUID = 1L;

    // Color de acento: cian para diferenciar del inventario (ámbar)
    private static final Color C_CYAN = new Color(0, 210, 255);

    public VentanaVerPrestamos(Biblioteca biblioteca) {
        setTitle("Préstamos Activos — Biblioteca UMG");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(620, 460);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(VentanaBiblioteca.C_BG);
        setContentPane(root);

        // ── Header ───────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0,
                    new Color(0, 80, 140), getWidth(), 0, new Color(5, 20, 40));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(0, 72));
        header.setBorder(new EmptyBorder(14, 28, 14, 28));

        JLabel ico = new JLabel("📋");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        JLabel lTitle = new JLabel("Historial de Préstamos");
        lTitle.setFont(new Font("Garamond", Font.BOLD, 22));
        lTitle.setForeground(Color.WHITE);
        JLabel lSub = new JLabel("Registros activos en el sistema");
        lSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lSub.setForeground(new Color(160, 220, 255));
        titleGroup.add(lTitle);
        titleGroup.add(lSub);

        // Badge contador (estático por ahora)
        JLabel badge = new JLabel(" ACTIVOS ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(C_CYAN);
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_CYAN, 1),
            new EmptyBorder(3, 8, 3, 8)
        ));

        header.add(ico, BorderLayout.WEST);
        header.add(titleGroup, BorderLayout.CENTER);
        header.add(badge, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // ── Área de contenido ────────────────────────────────────────────────
        JTextArea area = VentanaInventario.buildTextArea(C_CYAN);
        JScrollPane scroll = VentanaInventario.styledScroll(area);

        JPanel bodyWrap = new JPanel(new BorderLayout());
        bodyWrap.setBackground(VentanaBiblioteca.C_BG);
        bodyWrap.setBorder(new EmptyBorder(20, 24, 20, 24));
        bodyWrap.add(scroll, BorderLayout.CENTER);
        root.add(bodyWrap, BorderLayout.CENTER);

        // ── Footer ───────────────────────────────────────────────────────────
        root.add(VentanaInventario.buildFooter("Préstamos activos  •  Solo lectura"), BorderLayout.SOUTH);

        // ── Cargar datos ─────────────────────────────────────────────────────
        String resultado = VentanaInventario.capturarSysOut(biblioteca::mostrarPrestamos);
        area.setText(resultado.trim().isEmpty()
            ? "> No existen préstamos activos en este momento."
            : resultado);
        area.setCaretPosition(0);
    }
}
