import cern.colt.function.tdouble.DoubleFunction;
import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import cern.jet.math.tdouble.DoubleFunctions;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * glossary of symbols
 *
 *      l       layer number. eg a_l = _activation vector at layer l
 *
 *      L       highest layer number. a_L is the output of the network.
 *
 *      W_l     weight matrix at layer l, representing connections from
 *                          layer l to layer l+1
 *
 *      b_l     additive bias vector at layer l. it has the same number
 *                          of rows as a_l
 *
 *      z_l     value of each layer pre-_activation. it is the input of the
 *                          _activation function (eg tanh). it is also used
 *                          to calculate the derivatives of the weights. w_l
 *                          has the shape (a, b) and a_l-1 has the shape
 *                          (b, c), where a is the number if units in layer l,
 *                          b is the number of units in layer l-1, and c is
 *                          the batch size
 *
 *                                    z_l = w_l * a_l-1 + b_l
 *
 *      a_l     _activation at layer l, a column vector. the _activation
 *                          function that is used by default in this class
 *                          is the hyperbolic tangent (tanh)
 *
 *                             a_l = tanh( z_l ) = tanh( w_l * a_l-1 + b_l )
 *
 *      A       an array of every a_l
 *
 *      Z       an array of every z_l
 *
 *      AZ      a 2d array of column vecs, {A, Z}. must be returned as one
 *                           object since calculating z_l is dependent on
 *                           a_l-1, so they appear in the same function.
 *
 *
 */
public class Network {

    private int
        numLayers;


    private double
        randScale = .2,
        randOffset = 0;


    private DoubleMatrix2D[]
        weights,
        biases;


    private DoubleFunction
        _activation =  MatOps.tanh,
        _dActivation = MatOps.dTanh,
        random = MatOps.rand;


    /**
     * create a new Network object based on a list of sizes
     *
     * @param sizes
     */
    public Network(ArrayList<Integer> sizes) {
        this.init(sizes);
    }


    /**
     * create a new network object from a NetworkSettings object
     * and a Train object
     *
     * @param train
     * @param hiddenLayerSizes
     */
    public Network(NetworkSettings networkSettings, Train train) {
        ArrayList<Integer> hiddenLayerSizes = networkSettings.getHiddenLayerSizes(),
            sizes = new ArrayList<>(Collections.nCopies(hiddenLayerSizes.size() + 2, 0));

        sizes.set(0, train.trainX.get(0).rows());
        sizes.set(sizes.size() - 1, train.trainY.get(0).rows());

        for (int i = 1; i < sizes.size() - 1; i++) {
            sizes.set(i, hiddenLayerSizes.get(i - 1));
        }

        this._activation = networkSettings.getActivation();
        this._dActivation = networkSettings.getdActivation();
        this.random = networkSettings.getInitialWeights();

        this.init(sizes);
    }


    /**
     * given an array of sizes, initialize the network
     *
     * @param sizes
     */
    private void init(ArrayList<Integer> sizes) {
        this.numLayers = sizes.size();
        this.weights = new DoubleMatrix2D[this.numLayers - 1];
        this.biases = new DoubleMatrix2D[this.numLayers - 1];

        for (int l = 0; l < this.numLayers - 1; l++) {
            this.weights[l] = new DenseDoubleMatrix2D(sizes.get(l + 1), sizes.get(l))
                .assign(this.random)
                .assign(DoubleFunctions.mult(this.randScale))
                .assign(DoubleFunctions.plus(this.randOffset));

            this.biases[l] = new DenseDoubleMatrix2D(sizes.get(l + 1), 1)
                .assign(this.random)
                .assign(DoubleFunctions.mult(this.randScale))
                .assign(DoubleFunctions.plus(this.randOffset));
        }
    }


    /**
     * this computes the pre-activation and activation
     * values for every layer
     *
     *  z_l = W_l * a_l-1 + b_l
     *
     *
     *  a_l = tanh( z_l )
     *
     *
     * @param a_0
     * @return
     */
    public DoubleMatrix2D[][] activation(DoubleMatrix2D a_0) {
        DoubleMatrix2D[] A = new DoubleMatrix2D[this.numLayers];
        DoubleMatrix2D[] Z = new DoubleMatrix2D[this.numLayers];

        A[0] = a_0.copy();
        Z[0] = a_0.copy();

        for (int l = 1; l < this.numLayers; l++) {
            // [l - 1] because l = 0 (the input row)
            // stores no weights or biases
            DoubleMatrix2D b_l = DoubleFactory2D
                .dense
                .repeat(this.biases[l - 1], 1, a_0.columns());
            //
            // a_l = W_l * a_l-1 + b_l
            //
            Z[l] = this.weights[l - 1]
                .zMult(A[l - 1], null)
                .assign(b_l, DoubleFunctions.plus);
            //
            // a_l = tanh( z_l )
            //
            A[l] = Z[l]
                .copy()
                .assign(this._activation);
        }

        return new DoubleMatrix2D[][] {A, Z};
    }


    /**
     * applies the gradient vectors to each layer.
     *
     * w_l_j_k' = w_l_j_k .- n * dW_l_j_k
     *
     *
     * b_l_j' = b_l_j .- n * dB_l_j
     *
     *
     * @param gradient
     * @param delta
     */
    public void backprop(DoubleMatrix2D[] gradient, DoubleMatrix2D[] delta, double learningRate) {

        for (int i_w = 0; i_w < gradient.length; i_w++) {
            this.weights[i_w].assign(
                gradient[i_w]
                    .copy()
                    .assign(DoubleFunctions.mult(learningRate)),
                MatOps.entryMinus
            );

            // i_w + 1 because delta includes first layer while
            // weight gradients do not
            //
            this.biases[i_w]
                .assign(
                    delta[i_w + 1]
                        .copy()
                        .assign(DoubleFunctions.mult(learningRate)),
                    MatOps.entryMinus
                );
        }
    }


    /**
     * computes the delta value for every layer in the network
     *
     * delta_L = nabla_C_a .* dTanh(z_L)
     *
     *
     * delta_l = ( ( W_l+1 )' * delta_l+1 ) .* dTanh( z_l )
     *
     *
     * @param AZ
     * @param y
     * @return
     */
    public DoubleMatrix2D[] delta(DoubleMatrix2D[][] AZ, DoubleMatrix2D y) {
        int maxL = this.numLayers - 1;

        DoubleMatrix2D[]
            A = AZ[0],
            Z = AZ[1];

        DoubleMatrix2D[] delta = new DoubleMatrix2D[this.numLayers];
        delta[this.numLayers - 1] = this.costDerivative(y, A[maxL], Z[maxL]);
        for (int l = maxL - 1; l >= 0; l--) {
            delta[l] = this.weights[l]
                .viewDice()
                .zMult(delta[l + 1], null)
                .assign(
                    Z[l]
                        .copy()
                        .assign(this._dActivation),
                    MatOps.entryMultiply
                );
        }

        return delta;
    }


    /**
     * this computes the derivative of the cost function
     * with respect to every activation in the network
     *
     *
     *  dC / dW_l_j_k = a_l-1_k * delta_l_j
     *
     *
     *  dC / dB_l_j = delta_l_j
     *
     *
     * @param AZ
     * @param delta
     * @return
     */
    public DoubleMatrix2D[] gradient(DoubleMatrix2D[][] AZ, DoubleMatrix2D[] delta) {

        DoubleMatrix2D[]
            A = AZ[0],
            gradient = new DoubleMatrix2D[this.numLayers - 1];

        for (int l = 1; l < this.numLayers; l++) {
            DoubleMatrix2D
                // extend delta_l columns by height of a_l-1
                delta_l = DoubleFactory2D
                    .dense
                    .repeat(delta[l], 1, A[l - 1].rows()),
                // extend transposed a_l-1 rows by height of delta_l
                a_lminus1 = DoubleFactory2D
                    .dense
                    .repeat(A[l - 1].viewDice(), delta_l.rows(), 1);
            //
            // dC / dW_l_j_k = a_l-1_k * delta_l_j
            //
            // gradients and weights are addressed by [l - 1] because
            // there are only L-1 weight matrices: {w_l2, w_l3, ..., w_L}
            //
            gradient[l - 1] = delta_l.copy().assign(a_lminus1, DoubleFunctions.mult);
        }

        return gradient;
    }


    /**
     * this computes the derivative of the cost function
     * with respect to the last layer activation
     *
     *  dC / daL = ( a_L .- y ) .* dTanh( z_L )
     *
     *
     * @param y
     * @param aL
     * @param zL
     * @return
     */
    public DoubleMatrix2D costDerivative(DoubleMatrix2D y, DoubleMatrix2D aL, DoubleMatrix2D zL) {
        //
        // find dC / daL by subtracting the target
        // value y from output _activation value aL
        //
        // ( a_L .- y )
        //
        DoubleMatrix2D

            batch_dc_daL = aL
            .copy()
            .assign(y, MatOps.entryMinus)
            //
            // then do entry-wise multiplication of
            // nabla_C_a by the _activation derivative
            //
            //  ( a_L .- y ) .* dTanh( z_L )
            //
            .assign(
                zL
                    .copy()
                    .assign(this._dActivation),
                MatOps.entryMultiply
            );

        return batch_dc_daL;
    }


    /**
     * get the weights
     *
     * @return
     */
    public DoubleMatrix2D[] getWeights() {
        return this.weights;
    }


    /**
     * get the biases
     *
     * @return
     */
    public DoubleMatrix2D[] getBiases() {
        return this.biases;
    }


    /**
     * get the maximum weight value
     *
     * @return
     */
    public double getMaxWeightValue() {
        return Util.absMaxElement(this.getWeights());
    }
}
