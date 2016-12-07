import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;

import javax.swing.*;

public class Main {
    static int testNum = 0;
    public static void main(String args[]) {
        PrimaryFrame primaryFrame = new PrimaryFrame();

        Network network = new Network(new int[]{2, 12, 12, 3, 4, 8, 1});

        network.learningRate = 0.1;

        Train.train(network);

        primaryFrame.networkGraphicsPanel.setWeights(network.getWeights());
        primaryFrame.networkGraphicsPanel.setBiases(network.getBiases());

        DoubleMatrix2D[] tests = new DoubleMatrix2D[] {
            new DenseDoubleMatrix2D(new double[][]{{0}, {0}}),
            new DenseDoubleMatrix2D(new double[][]{{0}, {1}}),
            new DenseDoubleMatrix2D(new double[][]{{1}, {0}}),
            new DenseDoubleMatrix2D(new double[][]{{1}, {1}})
        };

        primaryFrame.networkGraphicsPanel.setActivations(network.activation(tests[0])[0]);

        new Timer(1000, (e) -> {
            DoubleMatrix2D[] netacts = network.activation(tests[testNum++ % 4])[0];
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
