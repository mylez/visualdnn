import javax.swing.*;

public class Main {

    /**
     * entrypoint for the program. create a new
     * PrimaryFrame object and make it visible.
     *
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PrimaryFrame primaryFrame = new PrimaryFrame();
            primaryFrame.setVisible(true);
        });
    }
}

