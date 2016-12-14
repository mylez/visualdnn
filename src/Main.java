import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PrimaryFrame primaryFrame = new PrimaryFrame();
            primaryFrame.setVisible(true);
        });
    }
}

