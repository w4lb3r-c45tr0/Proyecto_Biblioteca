package ui;
 
import java.awt.*;
import java.awt.event.*;
 
import javax.swing.*;
import javax.swing.border.*;
 
import modelo.Docente;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Revista;
import persistencia.GestorDatos;
import servicios.Biblioteca;
 
/**
 * Ventana principal del sistema de Biblioteca UMG.
 * Diseño oscuro/premium con sidebar, tarjetas de acción y terminal.
 */
public class VentanaBiblioteca extends JFrame {
 
    private static final long serialVersionUID = 1L;
 
    // ─── Paleta ──────────────────────────────────────────────────────────────
    public static final Color C_BG         = new Color(12, 17, 30);
    public static final Color C_SIDEBAR    = new Color(8, 12, 22);
    public static final Color C_CARD       = new Color(20, 28, 48);
    public static final Color C_GOLD       = new Color(212, 175, 95);
    public static final Color C_GOLD_DIM   = new Color(212, 175, 95, 40);
    public static final Color C_WHITE      = new Color(235, 232, 220);
    public static final Color C_GREY       = new Color(110, 120, 145);
    public static final Color C_FIELD_BG   = new Color(15, 22, 40);
    public static final Color C_FIELD_BD   = new Color(45, 55, 80);
    public static final Color C_TERM_BG    = new Color(8,  12, 22);
    public static final Color C_TERM_FG    = new Color(57, 230, 100);
 
    // Colores de botones de acción
    public static final Color C_BTN_BLUE   = new Color(59,  130, 246);
    public static final Color C_BTN_RED    = new Color(239, 68,  68);
    public static final Color C_BTN_AMBER  = new Color(245, 158, 11);
    public static final Color C_BTN_GREEN  = new Color(34,  197, 94);
 
    // ─── Componentes ─────────────────────────────────────────────────────────
    private JTextField txtCodigo;
    private JTextField txtCarnet;
    private JTextArea  txtTerminal;
    private Biblioteca biblioteca;
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new PantallaBienvenida().setVisible(true);
        });
    }
 
    public VentanaBiblioteca() {
        biblioteca = new Biblioteca();
        buildUI();
        // Cargar datos DESPUÉS de construir la UI para poder usar log()
        inicializarDatos();
        // Guardar al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GestorDatos.guardar(biblioteca);
            }
        });
    }
 
    // ── Inicialización de datos ───────────────────────────────────────────────
 
    /**
     * Si ya existen archivos CSV en disco, los carga.
     * Si no existen (primera vez), carga los datos de prueba y los guarda.
     */
    private void inicializarDatos() {
        if (GestorDatos.existenArchivos()) {
            GestorDatos.cargar(biblioteca);
            log("> Datos cargados desde archivo (datos/).");
        } else {
            cargarDatosPrueba();
            GestorDatos.guardar(biblioteca);
            log("> Primera ejecución: datos de prueba guardados en datos/.");
        }
    }
 
    private void cargarDatosPrueba() {
        biblioteca.registrarMaterial(new Libro("L1", "Java POO", 2020, "Juan Perez", 3));
        biblioteca.registrarMaterial(new Libro("L2", "El arte de la guerra", 1910, "Sun Tzu", 5));
        biblioteca.registrarMaterial(new Revista("R1", "Tecnología Hoy", 2024, 5, 2));
        biblioteca.registrarUsuario(new Estudiante("201", "Jery"));
        biblioteca.registrarUsuario(new Estudiante("202", "Walber"));
        biblioteca.registrarUsuario(new Docente("D01", "Maria"));
        biblioteca.registrarUsuario(new Docente("D02", "Jose"));
    }
 
    // ── Construcción de UI ────────────────────────────────────────────────────
 
    private void buildUI() {
        setTitle("Sistema de Biblioteca UMG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(940, 660);
        setMinimumSize(new Dimension(860, 580));
        setLocationRelativeTo(null);
 
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(C_BG);
        setContentPane(root);
 
        root.add(buildSidebar(),  BorderLayout.WEST);
        root.add(buildMain(),     BorderLayout.CENTER);
    }
 
    // ── Sidebar izquierdo ────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel side = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(C_SIDEBAR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Línea dorada derecha
                g2.setColor(C_GOLD_DIM);
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
            }
        };
        side.setPreferredSize(new Dimension(210, 0));
        side.setOpaque(false);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(30, 0, 30, 0));
 
        // Logo / Icono
        JLabel logo = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int s = 52;
                int ox = (getWidth()-s)/2, oy = 2;
                // Círculo fondo
                g2.setColor(new Color(212,175,95,25));
                g2.fillOval(ox-6, oy-6, s+12, s+12);
                // Libro simple
                g2.setColor(new Color(160,130,60));
                g2.fillRoundRect(ox+2, oy+2, s-4, s-2, 5,5);
                g2.setColor(C_GOLD);
                g2.fillRoundRect(ox+8, oy, s-6, s-2, 5,5);
                g2.setColor(new Color(255,255,255,60));
                g2.setStroke(new BasicStroke(1f));
                for (int i=0;i<5;i++) g2.drawLine(ox+12, oy+12+i*6, ox+s-4, oy+12+i*6);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(210, 70));
        logo.setMaximumSize(new Dimension(210, 70));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel lblName = label("BIBLIOTECA", new Font("Garamond", Font.BOLD, 19), C_WHITE);
        JLabel lblUMG  = label("UMG", new Font("Garamond", Font.BOLD, 14), C_GOLD);
        JLabel divider = label("─────────────", new Font("Segoe UI", Font.PLAIN, 11), C_GOLD_DIM);
 
        side.add(logo);
        side.add(Box.createVerticalStrut(6));
        side.add(lblName);
        side.add(lblUMG);
        side.add(Box.createVerticalStrut(16));
        side.add(divider);
        side.add(Box.createVerticalStrut(20));
 
        // Íconos de info
        String[][] infos = {
            {"📚", "Materiales", "4 registrados"},
            {"👥", "Usuarios",   "4 activos"},
            {"🕐", "Sistema",   "En línea"},
        };
        for (String[] info : infos) {
            side.add(buildInfoRow(info[0], info[1], info[2]));
            side.add(Box.createVerticalStrut(12));
        }
 
        side.add(Box.createVerticalGlue());
 
        JLabel lblVer = label("v2.0  •  2025", new Font("Segoe UI", Font.PLAIN, 10), new Color(60,65,90));
        side.add(lblVer);
 
        return side;
    }
 
    private JPanel buildInfoRow(String icon, String title, String value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(200, 44));
        row.setBorder(new EmptyBorder(6, 20, 6, 16));
 
        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
 
        JPanel textCol = new JPanel();
        textCol.setOpaque(false);
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));
        JLabel lTitle = new JLabel(title);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lTitle.setForeground(C_WHITE);
        JLabel lVal = new JLabel(value);
        lVal.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lVal.setForeground(C_GREY);
        textCol.add(lTitle);
        textCol.add(lVal);
 
        row.add(ico, BorderLayout.WEST);
        row.add(textCol, BorderLayout.CENTER);
        return row;
    }
 
    // ── Panel principal (derecha) ─────────────────────────────────────────────
    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(24, 28, 24, 28));
 
        // Header
        main.add(buildHeader(), BorderLayout.NORTH);
 
        // Cuerpo (formulario + botones + terminal)
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
 
        body.add(buildTransactionCard());
        body.add(Box.createVerticalStrut(16));
        body.add(buildActionButtons());
        body.add(Box.createVerticalStrut(16));
        body.add(buildTerminal());
 
        main.add(body, BorderLayout.CENTER);
        return main;
    }
 
    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setOpaque(false);
        h.setBorder(new EmptyBorder(0, 0, 8, 0));
 
        JLabel title = new JLabel("Panel de Control");
        title.setFont(new Font("Garamond", Font.BOLD, 26));
        title.setForeground(C_WHITE);
 
        JLabel sub = new JLabel("Gestión de préstamos y devoluciones");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(C_GREY);
 
        // Indicador ONLINE
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        badge.setOpaque(false);
        JLabel dot = new JLabel("●");
        dot.setForeground(C_BTN_GREEN);
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        JLabel live = new JLabel("SISTEMA ACTIVO");
        live.setFont(new Font("Segoe UI", Font.BOLD, 10));
        live.setForeground(C_BTN_GREEN);
        badge.add(dot); badge.add(live);
 
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(title); left.add(sub);
 
        h.add(left, BorderLayout.WEST);
        h.add(badge, BorderLayout.EAST);
 
        // Línea separadora dorada
        JPanel wrap = new JPanel(new BorderLayout(0,8));
        wrap.setOpaque(false);
        wrap.add(h, BorderLayout.CENTER);
        JSeparator sep = new JSeparator();
        sep.setForeground(C_GOLD_DIM);
        sep.setBackground(C_GOLD_DIM);
        wrap.add(sep, BorderLayout.SOUTH);
        return wrap;
    }
 
    private JPanel buildTransactionCard() {
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(C_GOLD_DIM);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        card.setBorder(new EmptyBorder(18, 24, 18, 24));
 
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
 
        // Fila 0: etiquetas
        g.gridy = 0; g.weightx = 0.5;
        g.gridx = 0; card.add(fieldLabel("🏷  Código del Material"), g);
        g.gridx = 1; card.add(fieldLabel("🎓  Carnet del Usuario"), g);
 
        // Fila 1: campos
        g.gridy = 1;
        txtCodigo = buildField("Ej: L1, L2, R1...");
        txtCarnet = buildField("Ej: 201, D01...");
        g.gridx = 0; card.add(txtCodigo, g);
        g.gridx = 1; card.add(txtCarnet, g);
 
        return card;
    }
 
    private JPanel buildActionButtons() {
        JPanel grid = new JPanel(new GridLayout(1, 4, 12, 0));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
 
        grid.add(buildActionBtn("▶  Préstamo",    C_BTN_BLUE,  e -> accionPrestar()));
        grid.add(buildActionBtn("⏪  Devolver",    C_BTN_RED,   e -> accionDevolver()));
        grid.add(buildActionBtn("📦  Inventario",  C_BTN_AMBER, e -> abrirInventario()));
        grid.add(buildActionBtn("📋  Préstamos",   C_BTN_GREEN, e -> abrirPrestamos()));
 
        return grid;
    }
 
    private JPanel buildTerminal() {
        JPanel wrap = new JPanel(new BorderLayout(0, 8));
        wrap.setOpaque(false);
 
        JLabel lbl = new JLabel("  💻  Terminal del Sistema");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(C_GREY);
        wrap.add(lbl, BorderLayout.NORTH);
 
        txtTerminal = new JTextArea();
        txtTerminal.setEditable(false);
        txtTerminal.setBackground(C_TERM_BG);
        txtTerminal.setForeground(C_TERM_FG);
        txtTerminal.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtTerminal.setMargin(new Insets(14, 16, 14, 16));
        txtTerminal.setCaretColor(C_TERM_FG);
        log("> Sistema iniciado correctamente.");
        log("> Esperando transacciones...");
 
        JScrollPane scroll = new JScrollPane(txtTerminal);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 40, 65), 1),
            BorderFactory.createEmptyBorder()
        ));
        scroll.setBackground(C_TERM_BG);
        scroll.getViewport().setBackground(C_TERM_BG);
        wrap.add(scroll, BorderLayout.CENTER);
        return wrap;
    }
 
    // ── Acciones ──────────────────────────────────────────────────────────────
 
    private void accionPrestar() {
        String codigo = txtCodigo.getText().trim();
        String carnet  = txtCarnet.getText().trim();
        if (codigo.isEmpty() || carnet.isEmpty()) {
            showWarn("Complete el Código y el Carnet para efectuar un préstamo.");
            return;
        }
        String res = biblioteca.prestarMaterial(codigo, carnet);
        log("PRÉSTAMO → " + res);
        // Guardar cambios en disco automáticamente
        GestorDatos.guardar(biblioteca);
        log("> 💾 Guardado en disco.");
        limpiar();
    }
 
    private void accionDevolver() {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            showWarn("Ingrese el Código del material a devolver.");
            return;
        }
        String res = biblioteca.devolverMaterial(codigo);
        log("DEVOLUCIÓN → " + res);
        // Guardar cambios en disco automáticamente
        GestorDatos.guardar(biblioteca);
        log("> 💾 Guardado en disco.");
        limpiar();
    }
 
    private void abrirInventario() {
        new VentanaInventario(biblioteca).setVisible(true);
    }
 
    private void abrirPrestamos() {
        new VentanaVerPrestamos(biblioteca).setVisible(true);
    }
 
    // ── Helpers de UI ─────────────────────────────────────────────────────────
 
    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(C_GREY);
        return l;
    }
 
    private JTextField buildField(String placeholder) {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(C_FIELD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(isFocusOwner() ? C_GOLD : C_FIELD_BD);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
                // Placeholder
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(70,80,105));
                    g2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                    g2.drawString(placeholder, 12, getHeight()/2 + 5);
                    g2.dispose();
                }
            }
        };
        f.setOpaque(false);
        f.setBorder(new EmptyBorder(8, 12, 8, 12));
        f.setForeground(C_WHITE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setCaretColor(C_GOLD);
        f.setPreferredSize(new Dimension(0, 40));
        return f;
    }
 
    private JButton buildActionBtn(String text, Color color, ActionListener al) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered=true; repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered=false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = hovered ? color.brighter() : color;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(0, 46));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);
        return btn;
    }
 
    private JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }
 
    private void log(String msg) {
        txtTerminal.append(msg + "\n");
        txtTerminal.setCaretPosition(txtTerminal.getDocument().getLength());
    }
 
    private void limpiar() {
        txtCodigo.setText(""); txtCarnet.setText(""); txtCodigo.requestFocus();
    }
 
    private void showWarn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}