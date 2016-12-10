import javax.swing.*;

public class Main {


    public static void main(String[] args) {

        Train train = new Train();
        train.loadData_braille();

        Network network = new Network(train, new int[]{12, 12});

        SwingUtilities.invokeLater(() -> {
            PrimaryFrame primaryFrame = new PrimaryFrame(network);
            primaryFrame.setVisible(true);
            primaryFrame.networkGraphicsPanel.trainAnimated(network, train, 0.005);
        });

    }
}

