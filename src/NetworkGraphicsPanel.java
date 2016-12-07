import cern.colt.matrix.tdouble.DoubleMatrix2D;

import java.awt.*;
import javax.swing.*;

public class NetworkGraphicsPanel extends JPanel {

    int
        fontSize = 9;

    private DoubleMatrix2D[]
        networkWeights,
        networkBiases,
        networkActivations;

    private double
        maxWeightValue;


    public NetworkGraphicsPanel() {
        super();
    }


    /**
     *
     * @param g
     * @param network
     */
    @Override
    public void paintComponent(Graphics g) {
        // prepare 2d graphics with antialiasing
        //
        Dimension size = this.getSize();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        // draw the network
        //
        this.paintBackground(g2, size);
        this.paintWeights(g2, size, this.networkWeights);
        this.paintUnits(g2, size, this.networkActivations, this.networkBiases);
    }


    /**
     *
     * @param g2
     * @param size
     */
    private void paintWeights(Graphics2D g2, Dimension size, DoubleMatrix2D[] weights) {
        // xPad and yPad define the distance
        // between each unit drawn
        //
        int numLayers = weights.length + 1,
            xPad = this.unitXPad(size.width, numLayers),
            yPad = this.unitYPad(size.height, Util.maxRowCount(weights));

        for (int layer = 0; layer < numLayers - 1; layer++) {
            // calculate the x positions of the current
            // layer and the following layer
            //
            int unitXPosA = this.unitXPos(xPad, layer),
                unitXPosB = this.unitXPos(xPad, layer + 1),
                // the number of columns in this layers weight matrix  corresponds
                // to the number of units in the current layer. the number of rows
                // corresponds to the number  of weights in the next layer
                //
                numUnitsA = weights[layer].columns(),
                numUnitsB = weights[layer].rows();
            for (int unitA = 0; unitA < numUnitsA; unitA++) {
                int unitYPosA = this.unitYPos(size.height,yPad, numUnitsA, unitA);
                for (int unitB = 0; unitB < numUnitsB; unitB++) {
                    // the position of the unit in the next layer
                    //
                    int unitYPosB = this.unitYPos(size.height, yPad, numUnitsB, unitB);
                    // get the value for the weight between unitA to unitB
                    //
                    double weight = weights[layer].get(unitB, unitA);
                    // set the graphics properties to draw weight as a line
                    //
                    this.setDrawProperties_unitWeight(g2, weight);
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
    private void paintUnits(Graphics2D g2, Dimension size, DoubleMatrix2D[] activations, DoubleMatrix2D[] biases) {
        int numLayers = activations.length,
            xPad = this.unitXPad(size.width, numLayers),
            yPad = this.unitYPad(size.height, Util.maxRowCount(activations)),
            unitRadius = 13;
        // paint with full opacity
        //
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        for (int layer = 0; layer < numLayers; layer++) {
            int xPos = this.unitXPos(xPad, layer) - unitRadius / 2,
                numUnits = activations[layer].rows();
            for (int unit = 0; unit < numUnits; unit++) {
                int yPos = this.unitYPos(size.height, yPad, numUnits, unit) - unitRadius / 2;
                // get the activation value
                //
                double activation = activations[layer].get(unit, 0);
                // draw unit as a filled circle with a gray 1px border
                //
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawOval(xPos - 1, yPos - 1, unitRadius + 1, unitRadius + 1);

                this.setDrawProperties_unitActivation(g2, activation);
                g2.fillOval(xPos, yPos, unitRadius, unitRadius);
                // draw labels representing the activation and bias
                //
                g2.setColor(Color.LIGHT_GRAY);
                g2.setFont(new Font("Monospaced", Font.BOLD, this.fontSize));
                g2.drawString(this.roundFormat(activation), xPos, yPos + 2 * unitRadius);
                if (layer > 0) {
                    g2.drawString(this.roundFormat(biases[layer - 1].get(unit, 0)), xPos, this.fontSize + yPos + 2 * unitRadius);
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
    private void setDrawProperties_unitActivation(Graphics2D g2, double activation) {
        int brightness = (int)Math.abs(activation * 255);
        Color color;

        if (activation > 0) {
            color = new Color(brightness, 0, 0);
        } else {
            color = new Color(0, 0, brightness);
        }

        g2.setColor(color);
    }


    /**
     *
     * @param g2
     * @param weight
     */
    private void setDrawProperties_unitWeight(Graphics2D g2, double weight) {
        Color color;
        AlphaComposite alpha;
        if (this.maxWeightValue != 0) {
            // make sure that the color is in
            // the range [127, 255]
            //
            double normWeight = weight / this.maxWeightValue;
            int brightness = (int)Math.abs(normWeight * 127);
            if (weight > 0) {
                color = new Color(127 + brightness, 0, 0);
            } else {
                color = new Color(0, 0, 127 + brightness);
            }
            alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)Math.abs(normWeight));
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
        this.networkWeights = networkWeights;
    }


    /**
     *
     * @param networkBiases
     */
    public void setBiases(DoubleMatrix2D[] networkBiases) {
        this.networkBiases = networkBiases;
    }


    /**
     *
     * @param networkActivations
     */
    public void setActivations(DoubleMatrix2D[] networkActivations) {
        this.networkActivations = networkActivations;
    }


    /**
     *
     * @param maxWeightValue
     */
    public void setMaxWeightValue(double maxWeightValue) {
        this.maxWeightValue = maxWeightValue;
    }

    /**
     *
     * @param num
     * @return
     */
    private String roundFormat(double num) {
        return String.format("%.3f", num);
    }
}
