import cern.colt.function.tdouble.DoubleFunction;
import cern.jet.math.tdouble.DoubleFunctions;

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

    private int[]
        hiddenLayerSizes = {30, 30};


    /**
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
     *
     * @param value
     */
    public void setLearningRate(double value) {
        this.learningRate = value;
    }


    /**
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
            default:
                setActivation(0);
        }
    }


    /**
     *
     * @param code
     */
    public void setDataSet(int code) {
        switch (code) {
            case 0:
                this.dataSet = Train.DataSet.XOR;
                break;
            case 1:
                this.dataSet = Train.DataSet.BRAILLE;
                break;
            case 2:
                this.dataSet = Train.DataSet.XOR;
                break;
            case 3:
                this.dataSet = Train.DataSet.XOR;
                break;
            case 4:
                this.dataSet = Train.DataSet.OR;
                break;
            case 6:
                this.dataSet = Train.DataSet.AND;
                break;
            case 7:
                this.dataSet = Train.DataSet.XOR;
                break;
            default:
                this.setDataSet(0);
        }
    }


    /**
     *
     * @param hiddenLayerSizes
     */
    public void setHiddenLayerSizes(int[] hiddenLayerSizes) {
        this.hiddenLayerSizes = hiddenLayerSizes;
    }


    /**
     *
     * @return
     */
    public Train.DataSet getDataSet() {
        return this.dataSet;
    }


    /**
     *
     * @return
     */
    public double getLearningRate() {
        return this.learningRate;
    }


    /**
     *
     * @return
     */
    public DoubleFunction getActivation() {
        return this.activation;
    }


    /**
     *
     * @return
     */
    public DoubleFunction getdActivation() {
        return this.dActivation;
    }


    /**
     *
     * @return
     */
    public DoubleFunction getInitialWeights() {
        return this.initialWeights;
    }

    public int[] getHiddenLayerSizes() {
        return this.hiddenLayerSizes;
    }
}