import cern.colt.function.tdouble.DoubleFunction;
import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import cern.jet.math.tdouble.DoubleFunctions;

public class Network {

    double
        learningRate = .01;


    int
        numLayers;


    int[]
        sizes;


    DoubleMatrix2D[]
        weights,
        biases;


    DoubleFunction
        _activation = MatOps.tanh,
        _dActivation = MatOps.dTanh;


    /**
     *
     * @param sizes
     */
    public Network(int[] sizes) {
        this.sizes = sizes;
        this.numLayers = sizes.length;
        this.weights = new DoubleMatrix2D[this.numLayers - 1];
        this.biases = new DoubleMatrix2D[this.numLayers - 1];

        // initialize weight and bias matrices with proper shapes
        //
        // todo - instead of unif()*2-1 use zero mean gaussian
        //
        double scale = 2, offset = -1;
        for (int l = 0; l < this.numLayers - 1; l++) {
            this.weights[l] = new DenseDoubleMatrix2D(sizes[l + 1], sizes[l])
                .assign(MatOps.detRand)
                .assign(DoubleFunctions.mult(scale))
                .assign(DoubleFunctions.plus(offset));
            this.biases[l] = new DenseDoubleMatrix2D(sizes[l + 1], 1)
                .assign(MatOps.detRand)
                .assign(DoubleFunctions.mult(scale))
                .assign(DoubleFunctions.plus(offset));
        }
    }


    /**
     *  z_l = W_l * a_l-1 + b_l
     *
     *
     *  z_l = tanh( a_l )
     *
     *
     * @param a
     * @return
     */
    public DoubleMatrix2D[][] activation(DoubleMatrix2D a) {
        DoubleMatrix2D[] A = new DoubleMatrix2D[this.numLayers];
        DoubleMatrix2D[] Z = new DoubleMatrix2D[this.numLayers];

        A[0] = a.copy();
        Z[0] = a.copy();

        for (int l = 1; l < this.numLayers; l++) {
            // [l - 1] because l = 0 (the input row)
            // stores no weights or biases
            DoubleMatrix2D b_l = DoubleFactory2D
                .dense
                .repeat(this.biases[l - 1], 1, a.columns());
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
     *
     * @param gradient
     * @param delta
     */
    public void backprop(DoubleMatrix2D[] gradient, DoubleMatrix2D[] delta) {

        for (int i_w = 0; i_w < gradient.length; i_w++) {
            this.weights[i_w].assign(
                gradient[i_w]
                    .copy()
                    .assign(DoubleFunctions.mult(this.learningRate)),
                MatOps.entryMinus
            );

            // i_w + 1 because delta includes first
            // layer while weight gradients do not
            this.biases[i_w]
                .assign(
                    delta[i_w + 1]
                        .copy()
                        .assign(DoubleFunctions.mult(this.learningRate)),
                    MatOps.entryMinus
                );
        }
    }


    /**
     * delta_L = nabla_C_a .* dTanh(z_L)
     *
     *
     * delta_l = ( ( W_l+1 )' * delta_l+1 ) .* dTanh( z_l )
     *
     *
     * @param y
     * @param AZ
     * @return
     */
    public DoubleMatrix2D[] delta(DoubleMatrix2D y, DoubleMatrix2D[][] AZ) {
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
     *  dC / dW_l_j_k = a_l-1_k * delta_l_j
     *
     *
     *  dC / dB_l_j = delta_l_j
     *
     *
     * @param A
     * @param delta
     * @return
     */
    public DoubleMatrix2D[] gradient(DoubleMatrix2D[] A, DoubleMatrix2D[] delta) {
        DoubleMatrix2D[] gradient = new DoubleMatrix2D[this.numLayers - 1];

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
     *  dC / daL = ( a_L .- y ) .* dTanh( Z_L )
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
        // value y from output activation value aL
        //
        // ( a_L .- y )
        //

        DoubleMatrix2D

            batch_dc_daL = aL
            .copy()
            .assign(y, MatOps.entryMinus)
            //
            // then do entry-wise multiplication of
            // nabla_C_a by the activation derivative
            //
            //  ( a_L .- y ) .* dTanh( Z_L )
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
     *
     * @return
     */
    public DoubleMatrix2D[] getWeights() {
        return this.weights;
    }

    /**
     *
     * @return
     */
    public DoubleMatrix2D[] getBiases() {
        return this.biases;
    }

    /**
     *
     * @return
     */
    public double getMaxWeightValue() {
        return Util.absMaxElement(this.getWeights());
    }
}