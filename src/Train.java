import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;

public class Train {


    public static void train(Network net) {

        int epochs = 1000;

        DoubleMatrix2D[] dataSet_x = new DoubleMatrix2D[] {
            new DenseDoubleMatrix2D(new double[][]{{0}, {0}}),
            new DenseDoubleMatrix2D(new double[][]{{0}, {1}}),
            new DenseDoubleMatrix2D(new double[][]{{1}, {0}}),
            new DenseDoubleMatrix2D(new double[][]{{1}, {1}}),
        };

        DoubleMatrix2D[] dataSet_y = new DoubleMatrix2D[] {
            new DenseDoubleMatrix2D(new double[][]{{0}}),
            new DenseDoubleMatrix2D(new double[][]{{1}}),
            new DenseDoubleMatrix2D(new double[][]{{1}}),
            new DenseDoubleMatrix2D(new double[][]{{0}}),
        };

        for (int epoch = 0; epoch < epochs; epoch++) {
            System.out.println("training epoch: " + epoch);

            for (int sample = 0; sample < dataSet_x.length; sample++) {
                DoubleMatrix2D
                    x = dataSet_x[sample],
                    y = dataSet_y[sample];

                DoubleMatrix2D[][]
                    AZ = net.activation(x);
                DoubleMatrix2D[]
                    delta = net.delta(y, AZ),
                    gradient = net.gradient(AZ[0], delta);

                net.backprop(gradient, delta);

            }
        }
    }
}
