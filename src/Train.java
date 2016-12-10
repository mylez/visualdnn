import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Train {

    ArrayList<DoubleMatrix2D>
        trainX,
        trainY,

        testX,
        testY;


    /**
     * data to convert a braile character represented as a
     * vector of R^6 into a one hot vector of R^30
     *
     */
    public void loadData_braille() {
        this.testX = new ArrayList<DoubleMatrix2D>();
        this.testY = new ArrayList<DoubleMatrix2D>();
        this.trainX = new ArrayList<DoubleMatrix2D>();
        this.trainY = new ArrayList<DoubleMatrix2D>();
        this._readFile_braille_30(this.trainX, this.trainY, "data/braille/30/data_set30.txt");
    }


    /**
     * data to train a network as an XOR logic gate
     *
     */
    public void loadData_xor() {
        this.testX = new ArrayList<DoubleMatrix2D>();
        this.testY = new ArrayList<DoubleMatrix2D>();

        this.trainX = new ArrayList<DoubleMatrix2D>();
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{0}, {0}}));
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{0}, {1}}));
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{1}, {0}}));
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{1}, {1}}));

        this.trainY = new ArrayList<DoubleMatrix2D>();
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{0}}));
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{1}}));
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{1}}));
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{0}}));
    }


    /**
     * data to train a network as an OR logic gate
     *
     */
    public void loadData_or() {
        this.testX = new ArrayList<DoubleMatrix2D>();
        this.testY = new ArrayList<DoubleMatrix2D>();

        this.trainX = new ArrayList<DoubleMatrix2D>();
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{0}, {0}}));
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{0}, {1}}));
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{1}, {0}}));
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{1}, {1}}));

        this.trainY = new ArrayList<DoubleMatrix2D>();
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{0}}));
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{1}}));
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{1}}));
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{1}}));
    }


    /**
     * data to train a network as an AND logic gate
     *
     */
    public void loadData_and() {
        this.testX = new ArrayList<DoubleMatrix2D>();
        this.testY = new ArrayList<DoubleMatrix2D>();

        this.trainX = new ArrayList<DoubleMatrix2D>();
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{0}, {0}}));
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{0}, {1}}));
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{1}, {0}}));
        this.trainX.add(new DenseDoubleMatrix2D(new double[][]{{1}, {1}}));

        this.trainY = new ArrayList<DoubleMatrix2D>();
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{0}}));
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{0}}));
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{0}}));
        this.trainY.add(new DenseDoubleMatrix2D(new double[][]{{1}}));
    }


    /**
     *
     * @param setX
     * @param setY
     * @param filePath
     */
    private void _readFile_braille_30(ArrayList<DoubleMatrix2D> setX, ArrayList<DoubleMatrix2D> setY, String filePath) {
        Scanner scanner;

        try {
            scanner = new Scanner(new File(filePath));
        }
        catch (FileNotFoundException e) {
            System.err.println("unable to load braille data set: " + e);
            return;
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            DoubleMatrix2D
                x = new DenseDoubleMatrix2D(6, 1),
                y = new DenseDoubleMatrix2D(30, 1);

            for (int i = 0; i < 36; i++) {
                double val = 0;
                try {
                    String ch = line.substring(2*i, 2*i + 1);
                    val = Double.parseDouble(ch);
                } catch (NumberFormatException e) {
                    System.out.println("error reading braille data set: " + e);
                    val = -1;
                }

                if (i < 6) {
                    x.set(i, 0, val);
                } else {
                    y.set(i - 6, 0, val);
                }
            }

            setX.add(x);
            setY.add(y);
        }
    }
}
