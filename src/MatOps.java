
import cern.colt.function.tdouble.DoubleDoubleFunction;
import cern.colt.function.tdouble.DoubleFunction;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.jet.math.tdouble.DoubleFunctions;

import java.util.Random;

public class MatOps {

    public static Random random = new Random();


    /**
     * tanh is a continuous non-linear activation function in range (-1, 1).
     *
     */
    public static DoubleFunction tanh = (a) -> Math.tanh(a);

    /**
     * dTanh is the derivative of the tanh function in range (0, 1)
     *
     */
    public static DoubleFunction dTanh = new DoubleFunction() {
        @Override
        public double apply(double a) {
            //return a > 0 ? 1 : 0;
            return 1 - Math.pow(Math.tanh(a), 2);
        }
    };

    /**
     * relu is a non-linear activation function in range [0, inf)
     *
     */
    public static DoubleFunction relu = new DoubleFunction() {
        @Override
        public double apply(double a) {
            return a > 0 ? a : 0;
        }
    };

    /**
     * dRelu is the derivative of the relu function in {0, 1}
     *
     */
    public static DoubleFunction dRelu = new DoubleFunction() {
        @Override
        public double apply(double a) { return a > 0 ? 1 : 0; }
    };

    /**
     * entryPlus is the entrywise addition of two n x m matrices.
     *
     */
    public static DoubleDoubleFunction entryPlus = new DoubleDoubleFunction() {
        @Override
        public double apply(double a, double b) {
            return a + b;
        }
    };

    /**
     * entryMinus is the entrywise subtraction of two n x m matrices.
     *
     */
    public static DoubleDoubleFunction entryMinus = new DoubleDoubleFunction() {
        @Override
        public double apply(double a, double b) {
            return a - b;
        }
    };

    /**
     * entryMultiply is the entrywise product of two n x m matrices.
     * also known as the Hadamard product.
     *
     */
    public static DoubleDoubleFunction entryMultiply = new DoubleDoubleFunction() {
        @Override
        public double apply(double a, double b) {
            return a * b;
        }
    };

    /**
     * seeded random numbers for predictable tests
     *
     */
    public static DoubleFunction detRand = new DoubleFunction() {
        @Override
        public double apply(double v) {
            return random.nextDouble();
        }
    };

    /**
     *
     * @param a
     */
    public static void matshow(DoubleMatrix2D a) {
        DoubleMatrix2D display = a;
        String colorMap =  " .:-=+*#%@!";
        double max = a.getMaxLocation()[0] + 1;
        double min = a.getMinLocation()[0];
        // ensure positive values
        System.out.println(a.rows() + " x " + a.columns());
        if (min < 0) {
            display = a.assign(DoubleFunctions.minus(min));
        }
        for (int r = 0; r < display.rows(); r++) {
            for (int c = 0; c < display.columns(); c++) {
                double e = display.get(r, c);
                char ch = colorMap.charAt((int)(colorMap.length() * e / max));
                System.out.print(ch);
                System.out.print(" ");
            }
            System.out.print('\n');
        }
    }

    /**
     *
     * @param name
     * @param a
     */
    public static void matshow(String name, DoubleMatrix2D a) {
        System.out.print("\n"+name + ", ");
        matshow(a);
    }

    /**
     *
     * @param A
     */
    public static void matshow(DoubleMatrix2D[] A) {
        for (DoubleMatrix2D a: A) {
            System.out.println(a+"\n");
            // matshow("-----", a);
        }
    }
}