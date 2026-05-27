package boot;
 
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vista.PantallaBienvenida;
 
/**
 * Punto de entrada principal del sistema Biblioteca UMG.
 * Separado del paquete vista para respetar separación de responsabilidades.
 */
public class Main {
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new PantallaBienvenida().setVisible(true);
        });
    }
}