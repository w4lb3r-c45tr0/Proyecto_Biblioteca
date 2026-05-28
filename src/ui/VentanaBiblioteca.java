package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import modelo.Docente;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Revista;
import modelo.Usuario;
import persistencia.GestorDatos;
import servicios.Biblioteca;

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

    public static final Color C_BTN_BLUE   = new Color(59,  130, 246);
    public static final Color C_BTN_RED    = new Color(239, 68,  68);
    public static final Color C_BTN_AMBER  = new Color(245, 158, 11);
    public static final Color C_BTN_GREEN  = new Color(34,  197, 94);
    public static final Color C_BTN_PURPLE = new Color(139, 92,  246);
    public static final Color C_BTN_TEAL   = new Color(20,  184, 166);
    public static final Color C_BTN_INDIGO = new Color(99,  102, 241);

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
        inicializarDatos();
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                GestorDatos.guardar(biblioteca);
            }
        });
    }

    // ── Datos ─────────────────────────────────────────────────────────────────

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

    // ── UI principal ──────────────────────────────────────────────────────────

    private void buildUI() {
        setTitle("Sistema de Biblioteca UMG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(940, 720);
        setMinimumSize(new Dimension(860, 640));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(C_BG);
        setContentPane(root);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMain(),    BorderLayout.CENTER);
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────

    private JPanel buildSidebar() {
        JPanel side = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(C_SIDEBAR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(C_GOLD_DIM);
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
            }
        };
        side.setPreferredSize(new Dimension(210, 0));
        side.setOpaque(false);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(30, 0, 30, 0));

        JLabel logo = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int s = 52, ox = (getWidth()-s)/2, oy = 2;
                g2.setColor(new Color(212,175,95,25));
                g2.fillOval(ox-6, oy-6, s+12, s+12);
                g2.setColor(new Color(160,130,60));
                g2.fillRoundRect(ox+2, oy+2, s-4, s-2, 5, 5);
                g2.setColor(C_GOLD);
                g2.fillRoundRect(ox+8, oy, s-6, s-2, 5, 5);
                g2.setColor(new Color(255,255,255,60));
                g2.setStroke(new BasicStroke(1f));
                for (int i=0; i<5; i++) g2.drawLine(ox+12, oy+12+i*6, ox+s-4, oy+12+i*6);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(210, 70));
        logo.setMaximumSize(new Dimension(210, 70));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        side.add(logo);
        side.add(Box.createVerticalStrut(6));
        side.add(sideLabel("BIBLIOTECA", new Font("Garamond", Font.BOLD, 19), C_WHITE));
        side.add(sideLabel("UMG",        new Font("Garamond", Font.BOLD, 14), C_GOLD));
        side.add(Box.createVerticalStrut(16));
        side.add(sideLabel("─────────────", new Font("Segoe UI", Font.PLAIN, 11), C_GOLD_DIM));
        side.add(Box.createVerticalStrut(20));

        String[][] infos = {
            {"📚", "Materiales", "Libros y Revistas"},
            {"👥", "Usuarios",   "Estudiantes y Docentes"},
            {"🕐", "Sistema",    "En línea"},
        };
        for (String[] info : infos) {
            side.add(buildInfoRow(info[0], info[1], info[2]));
            side.add(Box.createVerticalStrut(12));
        }

        side.add(Box.createVerticalGlue());
        side.add(sideLabel("v2.0  •  2025", new Font("Segoe UI", Font.PLAIN, 10), new Color(60,65,90)));
        return side;
    }

    private JPanel buildInfoRow(String icon, String title, String value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(200, 44));
        row.setBorder(new EmptyBorder(6, 20, 6, 16));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        JLabel lT = new JLabel(title);
        lT.setFont(new Font("Segoe UI", Font.BOLD, 11)); lT.setForeground(C_WHITE);
        JLabel lV = new JLabel(value);
        lV.setFont(new Font("Segoe UI", Font.PLAIN, 10)); lV.setForeground(C_GREY);
        col.add(lT); col.add(lV);

        row.add(ico, BorderLayout.WEST);
        row.add(col, BorderLayout.CENTER);
        return row;
    }

    // ── Panel principal ───────────────────────────────────────────────────────

    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout(0, 14));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(24, 28, 24, 28));
        main.add(buildHeader(),          BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.add(buildTransactionCard());
        body.add(Box.createVerticalStrut(14));
        body.add(buildActionButtons());   // ← ahora tiene 2 filas
        body.add(Box.createVerticalStrut(14));
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

        JLabel sub = new JLabel("Gestión de préstamos, devoluciones y usuarios");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(C_GREY);

        JPanel badge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        badge.setOpaque(false);
        JLabel dot  = new JLabel("●"); dot.setForeground(C_BTN_GREEN); dot.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        JLabel live = new JLabel("SISTEMA ACTIVO"); live.setFont(new Font("Segoe UI", Font.BOLD, 10)); live.setForeground(C_BTN_GREEN);
        badge.add(dot); badge.add(live);

        JPanel left = new JPanel(); left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(title); left.add(sub);

        h.add(left, BorderLayout.WEST);
        h.add(badge, BorderLayout.EAST);

        JPanel wrap = new JPanel(new BorderLayout(0, 8)); wrap.setOpaque(false);
        JSeparator sep = new JSeparator();
        sep.setForeground(C_GOLD_DIM); sep.setBackground(C_GOLD_DIM);
        wrap.add(h,   BorderLayout.CENTER);
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
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setBorder(new EmptyBorder(16, 24, 16, 24));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;

        g.gridy = 0; g.gridx = 0; card.add(fieldLabel("🏷  Código del Material"), g);
        g.gridx = 1;               card.add(fieldLabel("🎓  Carnet del Usuario"), g);

        txtCodigo = buildField("Ej: L1, L2, R1...");
        txtCarnet = buildField("Ej: 201, D01...");

        g.gridy = 1; g.gridx = 0; card.add(txtCodigo, g);
        g.gridx = 1;               card.add(txtCarnet, g);
        return card;
    }

    // ── Botones de acción (DOS FILAS) ─────────────────────────────────────────

    private JPanel buildActionButtons() {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));

        // Fila 1 — operaciones del día a día
        JPanel fila1 = new JPanel(new GridLayout(1, 4, 12, 0));
        fila1.setOpaque(false);
        fila1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        fila1.add(buildActionBtn("▶️  Préstamo",    C_BTN_BLUE,   e -> accionPrestar()));
        fila1.add(buildActionBtn("⏪  Devolver",    C_BTN_RED,    e -> accionDevolver()));
        fila1.add(buildActionBtn("📦  Inventario",  C_BTN_AMBER,  e -> abrirInventario()));
        fila1.add(buildActionBtn("📋  Préstamos",   C_BTN_GREEN,  e -> abrirPrestamos()));

        // Fila 2 — gestión del catálogo
        JPanel fila2 = new JPanel(new GridLayout(1, 3, 12, 0));
        fila2.setOpaque(false);
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        fila2.add(buildActionBtn("📖  Agregar Material", C_BTN_PURPLE, e -> accionAgregarMaterial()));
        fila2.add(buildActionBtn("👤  Agregar Usuario",  C_BTN_TEAL,   e -> accionAgregarUsuario()));
        fila2.add(buildActionBtn("👥  Ver Usuarios",     C_BTN_INDIGO, e -> abrirVerUsuarios()));

        wrap.add(fila1);
        wrap.add(Box.createVerticalStrut(10));
        wrap.add(fila2);
        return wrap;
    }

    // ── Terminal ──────────────────────────────────────────────────────────────

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

    // =========================================================================
    //  ACCIONES — préstamo y devolución
    // =========================================================================

    private void accionPrestar() {
        String codigo = txtCodigo.getText().trim();
        String carnet = txtCarnet.getText().trim();
        if (codigo.isEmpty() || carnet.isEmpty()) {
            showWarn("Complete el Código y el Carnet para efectuar un préstamo.");
            return;
        }
        String res = biblioteca.prestarMaterial(codigo, carnet);
        log("PRÉSTAMO → " + res);
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
        GestorDatos.guardar(biblioteca);
        log("> 💾 Guardado en disco.");
        limpiar();
    }

    // =========================================================================
    //  ACCIÓN — Agregar Material
    // =========================================================================

    private void accionAgregarMaterial() {

        // ── Campos del formulario ─────────────────────────────────────────────
        JComboBox<String> cmbTipo   = new JComboBox<>(new String[]{"Libro", "Revista"});
        JTextField fCodigo  = new JTextField(16);
        JTextField fTitulo  = new JTextField(16);
        JTextField fAnio    = new JTextField(16);
        JTextField fStock   = new JTextField(16);
        JLabel     lblExtra = new JLabel("Autor:");
        JTextField fExtra   = new JTextField(16);
        JLabel     hintExtra = new JLabel("Nombre del autor");
        hintExtra.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        hintExtra.setForeground(Color.GRAY);

        // Actualizar label/hint cuando cambia el tipo
        cmbTipo.addActionListener(e -> {
            boolean esLibro = cmbTipo.getSelectedItem().equals("Libro");
            lblExtra.setText(esLibro ? "Autor:" : "Núm. Edición:");
            hintExtra.setText(esLibro ? "Nombre del autor" : "Solo números, ej: 5");
        });

        // ── Panel del diálogo ─────────────────────────────────────────────────
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));
        panel.add(new JLabel("Tipo:"));            panel.add(cmbTipo);
        panel.add(new JLabel("Código:"));          panel.add(fCodigo);
        panel.add(new JLabel("Título:"));          panel.add(fTitulo);
        panel.add(new JLabel("Año (1900-2026):")); panel.add(fAnio);
        panel.add(new JLabel("Stock (copias):"));  panel.add(fStock);
        panel.add(lblExtra);                       panel.add(fExtra);
        panel.add(new JLabel(""));                 panel.add(hintExtra);

        int res = JOptionPane.showConfirmDialog(
            this, panel, "Agregar Material",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res != JOptionPane.OK_OPTION) return;

        // ── Validaciones ──────────────────────────────────────────────────────
        String tipo   = (String) cmbTipo.getSelectedItem();
        String codigo = fCodigo.getText().trim();
        String titulo = fTitulo.getText().trim();
        String extra  = fExtra.getText().trim();

        if (codigo.isEmpty() || titulo.isEmpty() || extra.isEmpty()) {
            showWarn("Todos los campos son obligatorios."); return;
        }
        if (biblioteca.buscarMaterial(codigo) != null) {
            showWarn("Ya existe un material con el código \"" + codigo + "\"."); return;
        }

        int anio, stock;
        try {
            anio  = Integer.parseInt(fAnio.getText().trim());
            stock = Integer.parseInt(fStock.getText().trim());
        } catch (NumberFormatException e) {
            showWarn("Año y Stock deben ser números enteros."); return;
        }
        if (anio < 1900 || anio > 2026) {
            showWarn("El año debe estar entre 1900 y 2026."); return;
        }
        if (stock <= 0) {
            showWarn("El stock debe ser mayor a 0."); return;
        }

        // ── Crear y registrar ─────────────────────────────────────────────────
        if (tipo.equals("Libro")) {
            biblioteca.registrarMaterial(new Libro(codigo, titulo, anio, extra, stock));
            log("> Material agregado: LIBRO [" + codigo + "] " + titulo + " — " + extra + " | Stock: " + stock);
        } else {
            int edicion;
            try {
                edicion = Integer.parseInt(extra);
                if (edicion <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                showWarn("El número de edición debe ser un número mayor a 0."); return;
            }
            biblioteca.registrarMaterial(new Revista(codigo, titulo, anio, edicion, stock));
            log("> Material agregado: REVISTA [" + codigo + "] " + titulo + " Ed." + edicion + " | Stock: " + stock);
        }
        GestorDatos.guardar(biblioteca);
        log("> 💾 Guardado en disco.");
    }

    // =========================================================================
    //  ACCIÓN — Agregar Usuario
    // =========================================================================

    private void accionAgregarUsuario() {

        // ── Campos del formulario ─────────────────────────────────────────────
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Estudiante", "Docente"});
        JTextField fCarnet  = new JTextField(16);
        JTextField fNombre  = new JTextField(16);

        JLabel hintCarnet = new JLabel("Solo 3 dígitos, ej: 203");
        hintCarnet.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        hintCarnet.setForeground(Color.GRAY);

        // Actualizar hint cuando cambia el tipo
        cmbTipo.addActionListener(e -> {
            if (cmbTipo.getSelectedItem().equals("Docente")) {
                hintCarnet.setText("D + números, ej: D03");
            } else {
                hintCarnet.setText("Solo 3 dígitos, ej: 203");
            }
        });

        // ── Panel del diálogo ─────────────────────────────────────────────────
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));
        panel.add(new JLabel("Tipo:"));    panel.add(cmbTipo);
        panel.add(new JLabel("Carnet:"));  panel.add(fCarnet);
        panel.add(new JLabel(""));         panel.add(hintCarnet);
        panel.add(new JLabel("Nombre:"));  panel.add(fNombre);

        int res = JOptionPane.showConfirmDialog(
            this, panel, "Agregar Usuario",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res != JOptionPane.OK_OPTION) return;

        // ── Validaciones ──────────────────────────────────────────────────────
        String tipo   = (String) cmbTipo.getSelectedItem();
        String carnet = fCarnet.getText().trim();
        String nombre = fNombre.getText().trim();

        if (carnet.isEmpty() || nombre.isEmpty()) {
            showWarn("El carnet y el nombre son obligatorios."); return;
        }
        if (biblioteca.buscarUsuario(carnet) != null) {
            showWarn("Ya existe un usuario con el carnet \"" + carnet + "\"."); return;
        }

        // Validar formato del carnet según el tipo
        if (tipo.equals("Docente")) {
            // Debe ser D seguido de uno o más dígitos  →  D01, D02, D123 ...
            if (!carnet.matches("D\\d+")) {
                showWarn("Carnet de Docente inválido.\n"
                       + "Formato requerido: D seguido de números (ej: D03, D10).");
                return;
            }
        } else {
            // Debe ser exactamente 3 dígitos  →  201, 202, 999 ...
            if (!carnet.matches("\\d{3}")) {
                showWarn("Carnet de Estudiante inválido.\n"
                       + "Formato requerido: exactamente 3 dígitos (ej: 203, 150).");
                return;
            }
        }

        // ── Crear y registrar ─────────────────────────────────────────────────
        if (tipo.equals("Docente")) {
            biblioteca.registrarUsuario(new Docente(carnet, nombre));
        } else {
            biblioteca.registrarUsuario(new Estudiante(carnet, nombre));
        }
        GestorDatos.guardar(biblioteca);
        log("> Usuario agregado: " + tipo.toUpperCase() + " [" + carnet + "] " + nombre);
        log("> 💾 Guardado en disco.");
    }

    // =========================================================================
    //  ABRIR VENTANAS SECUNDARIAS
    // =========================================================================

    private void abrirInventario()   { new VentanaInventario(biblioteca).setVisible(true); }
    private void abrirPrestamos()    { new VentanaVerPrestamos(biblioteca).setVisible(true); }
    private void abrirVerUsuarios()  { new VentanaVerUsuarios(biblioteca).setVisible(true); }
// aca en VentanaVerUsuario falta crear la clase VentanaVerUsuarios, que es la encargada
// de interactuar con el usuario para mostrar las listas de los usuarios registrados
// pero la agregamos desde ya para ir ubicando la funcionalidad, aunque no se haya implementado aun
    
    // =========================================================================
    //  HELPERS DE UI
    // =========================================================================

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
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(70, 80, 105));
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
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? color.brighter() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);
        return btn;
    }

    private JLabel sideLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font); l.setForeground(color);
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