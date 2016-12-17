import cern.colt.function.tdouble.DoubleFunction;
import cern.jet.math.tdouble.DoubleFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

class NetworkSettings {

    public boolean isOneBounded = false;


    private double
        learningRate = 0.001;


    private Train.DataSet
        dataSet = Train.DataSet.XOR;


    private DoubleFunction
        activation = MatOps.tanh,
        dActivation = MatOps.dTanh,
        initialWeights = MatOps.rand;


    private ArrayList<Integer>
        hiddenLayerSizes = new ArrayList<>(Arrays.asList(new Integer[] {3, 3}));


    /**
     * convert the selected index of the initial weights
     * combo box into a DoubleFunction representing the
     * desired random distribution
     *
     * @param code
     */
    public void setInitialWeights(int code) {
        switch (code) {
            case 0:
                this.initialWeights = MatOps.normal_mean_0_stdev_1;
                break;
            case 1:
                this.initialWeights = MatOps.normal_mean_1_stddev_1;
                break;
            case 2:
                this.initialWeights = MatOps.normal_mean_0_stddev_5;
                break;
            case 3:
                this.initialWeights = MatOps.normal_mean_5_stddev_5;
                break;
            case 4:
                this.initialWeights = MatOps.normal_mean_0_stddev_0_1;
                break;
            case 5:
                this.initialWeights = MatOps.normal_mean_0_1_stddev_0_1;
                break;
            default:
                this.initialWeights = MatOps.rand;
                break;
        }
    }


    /**
     * convert the selected index of the learning rate
     * slider into a number
     *
     * @param value
     */
    public void setLearningRate(double value) {
        this.learningRate = value;
    }


    /**
     * convert the selected index of the activation function
     * combo box into a DoubleFunction representing the
     * desired activation
     *
     * @param code
     */
    public void setActivation(int code) {
        switch (code) {
            case 0:
                this.isOneBounded = true;
                this.activation = MatOps.tanh;
                this.dActivation = MatOps.dTanh;
                break;
            case 1:
                this.activation = MatOps.relu;
                this.dActivation = MatOps.dRelu;
                break;
            case 2:
                this.activation = MatOps.lrelu;
                this.dActivation = MatOps.dLrelu;
                break;
            case 3:
                this.isOneBounded = true;
                this.activation = MatOps.sigmoid;
                this.dActivation = MatOps.dSigmoid;
                break;
            case 4:
                this.activation = DoubleFunctions.identity;
                this.dActivation = DoubleFunctions.constant(1);
                break;
            default:
                setActivation(0);
        }
    }


    /**
     * convert the selected index of the data set into a
     * member of the Train.DataSet enum
     *
     * @param code
     */
    public void setDataSet(int code) {
        switch (code) {
            case 0:
                this.dataSet = Train.DataSet.XOR;
                break;
            case 1:
                this.dataSet = Train.DataSet.OR;
                break;
            case 2:
                this.dataSet = Train.DataSet.AND;
                break;
            case 3:
                this.dataSet = Train.DataSet.BRAILLE;
                break;
            default:
                this.setDataSet(0);
        }
    }


    /**
     * set the current hidden layer sizes
     *
     * @param hiddenLayerSizes
     */
    public void setHiddenLayerSizes(ArrayList<Integer> hiddenLayerSizes) {
        this.hiddenLayerSizes = hiddenLayerSizes;
    }


    /**
     * get the current activation
     *
     * @return
     */
    public DoubleFunction getActivation() {
        return this.activation;
    }


    /**
     * get the current data set enum member
     *
     * @return
     */
    public Train.DataSet getDataSet() {
        return this.dataSet;
    }


    /**
     * get the current learning rate
     *N
     * @return
     */
    public double getLearningRate() {
        return this.learningRate;
    }


    /**
     * get the current activation derivative
     *
     * @return
     */
    public DoubleFunction getdActivation() {
        return this.dActivation;
    }


    /**
     * get the current initial weight random distribution
     * function
     *
     * @return
     */
    public DoubleFunction getInitialWeights() {
        return this.initialWeights;
    }

    /**
     * get the current hidden layer size array list
     *
     * @return
     */
    public ArrayList<Integer> getHiddenLayerSizes() {
        return this.hiddenLayerSizes;
    }
}