import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;

import javax.swing.*;

public class Main {

    static int testCase = 0;

    static double x_1 = 0, x_2 = 0;

    public static void main(String args[]) {
        PrimaryFrame primaryFrame = new PrimaryFrame();

        Network network = new Network(new int[]{2, 3, 5, 10, 10, 1});

        network.learningRate = .01;

        Train.train(network);

        primaryFrame.networkGraphicsPanel.setWeights(network.getWeights());
        primaryFrame.networkGraphicsPanel.setBiases(network.getBiases());
        primaryFrame.networkGraphicsPanel.setActivations(network.activation(Train.dataSet_x[0])[0]);

        new Timer(1000, (e) -> {

            double inc = 1;

            x_1 += inc;
            if (x_1 > 1) {
                x_1 = 0;
                x_2 += inc;
            }
            if (x_2 > 1) {
                x_2 = 0;
            }

            DoubleMatrix2D[] activations = network.activation(new DenseDoubleMatrix2D(new double[][]{
                {x_1},
                {x_2}
            }))[0];
            primaryFrame.networkGraphicsPanel.setActivations(activations);
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
