
import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import java.awt.*;
import javax.swing.*;

public class NetworkGraphicsPanel extends JPanel {

    int
        fontSize = 11;


    private DoubleMatrix2D[]
        netWeights,
        netBiases,
        netActivations;


    private double
        maxWeightValue = 1,
        maxActivationValue = 1;


    public NetworkGraphicsPanel() {
        super();
    }

    public NetworkGraphicsPanel(Network network) {
        super();
        DoubleMatrix2D[] weights = network.getWeights();
        DoubleMatrix2D[][] AZ = network.activation(DoubleFactory2D.dense.random(weights[0].columns(), 1));
        this.setWeights(weights);
        this.setBiases(network.getBiases());
        this.setActivations(AZ[0]);
        this.setMaxActivationValue(1);
        this.setMaxWeightValue(1);
    }

    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        // prepare 2d graphics
        //
        Dimension size = this.getSize();
        Graphics2D g2 = (Graphics2D) g;

        // todo - delete this when activation function becomes an option
        //
        //this.maxActivationValue = Util.absMaxElement(this.netActivations);
        //this.maxActivationValue = 1;
        this.maxWeightValue = Util.absMaxElement(this.netWeights);

        // draw the network
        //
        this.paintBackground(g2, size);
        this.paintWeights(g2, size);
        this.paintUnits(g2, size);
    }


    /**
     *
     * @param g2
     * @param size
     */
    private void paintWeights(Graphics2D g2, Dimension size) {
        // xPad and yPad define the distance between each unit drawn
        //
        int numLayers = this.netActivations.length,
            xPad = this.unitXPad(size.width, numLayers),
            yPadA = this.unitYPad(size.height, Util.maxRowCount(this.netWeights));

        // antialiasing becomes extremely slow when used on every
        // weight, so turn it off for this step
        //
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_OFF
        );

        for (int layer = 0; layer < numLayers - 1; layer++) {
            yPadA = this.unitYPad(size.height, this.netActivations[layer].rows());
            // calculate the x positions of the currentlayer and the following layer
            //
            int unitXPosA = this.unitXPos(xPad, layer),
                unitXPosB = this.unitXPos(xPad, layer + 1),
                // the number of columns in this layers weight matrix  corresponds
                // to the number of units in the current layer. the number of rows
                // corresponds to the number  of weights in the next layer
                //
                numUnitsA = this.netWeights[layer].columns(),
                numUnitsB = this.netWeights[layer].rows();
            for (int unitA = 0; unitA < numUnitsA; unitA++) {
                int unitYPosA = this.unitYPos(size.height, yPadA, numUnitsA, unitA),
                    yPadB = this.unitYPad(size.height, this.netActivations[layer + 1].rows());
                for (int unitB = 0; unitB < numUnitsB; unitB++) {
                    // the position of the unit in the next layer
                    //
                    int unitYPosB = this.unitYPos(size.height, yPadB, numUnitsB, unitB);
                    // get the value for the weight between unitA to unitB
                    //
                    double weight = this.netWeights[layer].get(unitB, unitA),
                        activation = this.netActivations[layer].get(unitA, 0);
                    // set the graphics properties to draw weight as a line
                    //
                    this.setDrawProperties_weight(g2, weight, activation);
                    g2.drawLine(unitXPosA, unitYPosA, unitXPosB, unitYPosB);
                }
            }
        }
    }


    /**
     *
     * @param g2
     * @param size
     */
    private void paintUnits(Graphics2D g2, Dimension size) {
        int numLayers = this.netActivations.length,
            xPad = this.unitXPad(size.width, numLayers),
            yPad = this.unitYPad(size.height, Util.maxRowCount(this.netActivations)),
            unitRadius = 13;
        // paint with full opacity
        //

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        for (int layer = 0; layer < numLayers; layer++) {
            int unitXPos = this.unitXPos(xPad, layer) - unitRadius / 2,
                numUnits = this.netActivations[layer].rows();
            yPad = this.unitYPad(size.height, this.netActivations[layer].rows());
            for (int unit = 0; unit < numUnits; unit++) {
                int unitYPos = this.unitYPos(size.height, yPad, numUnits, unit) - unitRadius / 2,
                    labelYPos =  unitYPos + unitRadius / 2,
                    labelXPos =  unitXPos + 2 * unitRadius;;
                // get the activation value
                //
                double activation = this.netActivations[layer].get(unit, 0);
                // draw unit as a filled circle with a gray 1px border
                //
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawOval(unitXPos - 1, unitYPos - 1, unitRadius + 1, unitRadius + 1);
                this.setDrawProperties_unit(g2, activation);
                g2.fillOval(unitXPos, unitYPos, unitRadius, unitRadius);
                // draw labels representing the activation and bias
                //
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Monospaced", Font.BOLD, this.fontSize));
                g2.drawString(this.roundFormat(activation), labelXPos, labelYPos);
                if (layer > 0) {
                    g2.drawString(this.roundFormat(this.netBiases[layer - 1].get(unit, 0)), labelXPos, labelYPos + this.fontSize);
                }
            }
        }
    }


    /**
     *
     * @param g2
     * @param size
     */
    private void paintBackground(Graphics2D g2, Dimension size) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, size.width, size.height);
    }


    /**
     *
     * @param g2
     * @param activation
     */
    private void setDrawProperties_unit(Graphics2D g2, double activation) {
        int br = Math.abs((int)Math.abs(255 * activation / this.maxActivationValue));
        Color color;
        if (activation > 0) {
            color = new Color(br, 0, 0);
        } else {
            color = new Color(0, 0, br);
        }
        g2.setColor(color);
    }


    /**
     *
     * @param g2
     * @param weight
     */
    private void setDrawProperties_weight(Graphics2D g2, double weight, double activation) {
        Color color;
        AlphaComposite alpha;
        if (this.maxWeightValue != 0) {
            // make sure that the color is in [127, 255] and
            // the opacity is in [0.2, 1]
            //
            double normWeight = (activation / Math.abs(this.maxActivationValue)) * (weight / Math.abs(this.maxWeightValue));
            int brightness = (int)Math.abs(normWeight * 127);
            if (normWeight > 0) {
                color = new Color(127 + brightness, 0, 0);
            } else {
                color = new Color(0, 0, 127 + brightness);
            }
            alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f + .8f * (float)Math.abs(normWeight));
        } else {
            // the unlikely case that the max weight is zero
            //
            alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f);
            color = Color.BLACK;
        }
        g2.setComposite(alpha);
        g2.setColor(color);
    }

    /**
     *
     * @param network
     * @param train
     * @param learningRate
     */
    public void trainAnimated(Network network, Train train, double learningRate) {
        DoubleMatrix2D[] dummyActivations = Util.onesWithShapes(
            network.activation(train.trainX.get(0))[0]
        );

        this.setMaxActivationValue(1);
        this.setActivations(dummyActivations);

        new Timer(16, (e) -> {
            for (int i = 0; i < 100; i++) {
                DoubleMatrix2D
                    x = train.trainX.get(i%4),
                    y = train.trainY.get(i%4);

                DoubleMatrix2D[][] AZ = network.activation(x);

                DoubleMatrix2D[]
                    delta = network.delta(AZ, y),
                    gradient = network.gradient(AZ, delta);

                network.backprop(gradient, delta, learningRate);
                this.setMaxActivationValue(Util.absMaxElement(AZ[0]));
                this.setActivations(AZ[0]);
            }

            DoubleMatrix2D[]
                weights = network.getWeights(),
                biases = network.getBiases();

            int index = (int)((e.getWhen()%30000)/1000);

            DoubleMatrix2D[][] AZ = network.activation(train.trainX.get(index));
            this.setMaxActivationValue(Util.absMaxElement(AZ[0]));
            this.setActivations(AZ[0]);

            this.setMaxWeightValue(1);
            this.setWeights(weights);
            this.setBiases(biases);

            this.repaint();
        }).start();
    }



    /**
     *
     * @param width
     * @param numLayers
     * @return
     */
    private int unitXPad(int width, int numLayers) {
        return width / numLayers;
    }


    /**
     *
     * @param height
     * @param maxNumUnits
     * @return
     */
    private int unitYPad(int height, int maxNumUnits) {
        return height / maxNumUnits;
    }


    /**
     *
     * @param xPad
     * @param layer
     * @return
     */
    private int unitXPos(int xPad, int layer) {
        return (int)(xPad * (layer + .5));
    }


    /**
     *
     * @param height
     * @param yPad
     * @param numUnits
     * @param unit
     * @return
     */
    private int unitYPos(int height,  int yPad, int numUnits, int unit) {
        int centerYOffset = (height - numUnits * yPad) / 2,
            unitOffset = yPad * unit,
            topMargin = yPad / 2;
        return centerYOffset + unitOffset + topMargin;
    }


    /**
     *
     * @param networkWeights
     */
    public void setWeights(DoubleMatrix2D[] networkWeights) {
        this.netWeights = networkWeights;
    }


    /**
     *
     * @param networkBiases
     */
    public void setBiases(DoubleMatrix2D[] networkBiases) {
        this.netBiases = networkBiases;
    }


    /**
     *
     * @param networkActivations
     */
    public void setActivations(DoubleMatrix2D[] networkActivations) {
        this.netActivations = networkActivations;
    }


    /**
     *
     * @param maxWeightValue
     */
    public void setMaxWeightValue(double maxWeightValue) {
        this.maxWeightValue = Math.abs(maxWeightValue);
    }

    /**
     *
     * @param maxActivationValue
     */
    public void setMaxActivationValue(double maxActivationValue) {
        this.maxActivationValue = Math.abs(maxActivationValue);
    }

    /**
     *
     * @param num
     * @return
     */
    private String roundFormat(double num) {
        return String.format("%s%.3f",  num < 0 ? "" : " ", num);
    }
}
