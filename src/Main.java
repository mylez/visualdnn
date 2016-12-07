import cern.colt.matrix.tdouble.DoubleMatrix2D;

import javax.swing.*;

public class Main {
    static int testNum = 0;
    public static void main(String args[]) {
        PrimaryFrame primaryFrame = new PrimaryFrame();

        Network network = new Network(new int[]{2, 2, 2, 1});

        network.learningRate = 0.1;

        Train.train(network);

        primaryFrame.networkGraphicsPanel.setWeights(network.getWeights());
        primaryFrame.networkGraphicsPanel.setBiases(network.getBiases());
        primaryFrame.networkGraphicsPanel.setActivations(network.activation(Train.dataSet_x[0])[0]);

        new Timer(1000, (e) -> {
            int i = testNum++ % Train.dataSet_y.length;
            DoubleMatrix2D[] netacts = network.activation(Train.dataSet_x[i])[0];
            primaryFrame.networkGraphicsPanel.setActivations(netacts);
            primaryFrame.networkGraphicsPanel.repaint();
        }).start();

        primaryFrame.networkGraphicsPanel.setMaxWeightValue(
            network.getMaxWeightValue()
        );

        SwingUtilities.invokeLater(() -> {
            primaryFrame.setVisible(true);
        });
    }
}
