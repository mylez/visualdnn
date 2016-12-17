import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class NetworkSettingsPanel extends JPanel {


    private Dimension
        preferredSize = new Dimension(250, -1);


    private String[]
        opt_dataset = new String[]{
            "XOR",
            "OR",
            "AND",
            "Braille"
        },
        opt_activation = new String[]{
            "Tanh",
            "ReLU",
            "Leaky ReLU",
            "Sigmoid",
            "Linear"
        },
        opt_initialWeights = new String[]{
            "Gaussian mean=0 stddev=1",
            "Gaussian mean=1 stddev=1",
            "Gaussian mean=0 stddev=5",
            "Gaussian mean=5 stddev=5",
            "Gaussian mean=0 stddev=0.1",
            "Gaussian mean=0.1 stddev=0.1",
            "Uniform [0, 1]"
        };


    private JLabel
        label_dataset = new JLabel("Dataset"),
        label_layerSizes = new JLabel("Hidden Layer Sizes"),
        label_activation = new JLabel("Activation"),
        label_learningRate = new JLabel("Learning Rate"),
        label_initialWeights = new JLabel("Random Weights");


    private JButton
        button_startPause = new JButton("Start"),
        button_reset = new JButton("Reset");


    private JComboBox
        combo_dataset = new JComboBox(this.opt_dataset),
        combo_activation = new JComboBox(this.opt_activation),
        combo_initialWeights = new JComboBox(this.opt_initialWeights);


    private JTextField
        textField_layerSizes = new JTextField("4, 8, 4");


    private JSlider
        slider_learningrate = new JSlider();


    private double
        learningRateValue = 0.01;


    private PrimaryFrame
        primaryFrame;


    /**
     * create a new NetworkSettingsPanel object with a reference
     * to the containing primaryFrame
     *
     * @param primaryFrame
     */
    public NetworkSettingsPanel(PrimaryFrame primaryFrame) {
        super();
        this.primaryFrame = primaryFrame;
        this.init();
    }


    /**
     * initialize the NetworkSettingsPanel object
     *
     */
    private void init() {
        // set up layout deatils
        //
        this.setLayout(new GridLayout(13, 1));
        this.setBorder(BorderFactory.createTitledBorder("Parameters"));
        this.setPreferredSize(this.preferredSize);
        // style components
        //
        this.slider_learningrate.setMinorTickSpacing(10);
        this.slider_learningrate.setMajorTickSpacing(10);
        this.slider_learningrate.setPaintTicks(true);
        // add components
        //
        this.add(this.label_dataset);
        this.add(this.combo_dataset);

        this.add(this.label_layerSizes);
        this.add(this.textField_layerSizes);

        this.add(this.label_activation);
        this.add(this.combo_activation);

        this.add(this.label_learningRate);
        this.add(this.slider_learningrate);

        this.add(this.label_initialWeights);
        this.add(this.combo_initialWeights);

        this.add(this.button_reset);
        this.add(this.button_startPause);
        // start listening for user actions
        //
        this.bindEventHandlers();
    }


    /**
     * convert the state of all input components to a NetworkSettings
     * object that can be understood by other classes
     *
     * @return
     */
    public NetworkSettings getNetworkSettings() {
        NetworkSettings settings = new NetworkSettings();

        settings.setActivation(this.combo_activation.getSelectedIndex());
        settings.setInitialWeights(this.combo_initialWeights.getSelectedIndex());
        settings.setDataSet(this.combo_dataset.getSelectedIndex());
        settings.setLearningRate(this.learningRateValue);
        settings.setHiddenLayerSizes(this.parseHiddenLayerSizes());

        return settings;
    }


    /**
     * binds various events of this panels components
     *
     */
    private void bindEventHandlers() {
        // user has changed learning rate slider
        //
        this.slider_learningrate.addChangeListener(e -> {
            int sliderIntVal = this.slider_learningrate.getValue();

            if (sliderIntVal < 10) {
                this.learningRateValue = 0.00001;
            } else if (sliderIntVal < 20) {
                this.learningRateValue = 0.00005;
            } else if (sliderIntVal < 30) {
                this.learningRateValue = 0.0001;
            } else if (sliderIntVal < 40) {
                this.learningRateValue = 0.0005;
            } else if (sliderIntVal < 50) {
                this.learningRateValue = 0.001;
            } else if (sliderIntVal < 60) {
                this.learningRateValue = 0.005;
            } else if (sliderIntVal < 70) {
                this.learningRateValue = 0.01;
            } else if (sliderIntVal < 80) {
                this.learningRateValue = 0.1;
            } else if (sliderIntVal < 90) {
                this.learningRateValue = 0.5;
            } else {
                this.learningRateValue = 1;
            }

            this.label_learningRate.setText("Learning Rate: " + String.format("%.5f", learningRateValue));
        });
        // user has pressed reset button
        //
        this.button_reset.addActionListener(e -> {
            this.primaryFrame.setNetworkSettings(this.getNetworkSettings());
        });
        // user has pressed start / pause button
        //
        this.button_startPause.addActionListener(e -> {
            Timer trainAnim = primaryFrame.getTrainAnimation();
            if (!trainAnim.isRunning()) {
                trainAnim.start();
                this.button_startPause.setText("Pause");
            } else {
                trainAnim.stop();
                this.button_startPause.setText("Start");
            }
        });
    }


    /**
     * read the text of the hidden layer sizes text field into
     * an ArrayList of Integers
     *
     * @return
     */
    private ArrayList<Integer> parseHiddenLayerSizes() {
        ArrayList layerSizes = new ArrayList<Integer>();
        Scanner intScanner = new Scanner(
            this.textField_layerSizes
                .getText()
                .replaceAll("[^ 0-9]", "")
        );

        try {
            while (intScanner.hasNextInt()) {
                int layerSize = intScanner.nextInt();
                if (layerSize > 0) {
                    layerSizes.add(layerSize);
                }
            }
        } catch (InputMismatchException e) {
            JOptionPane.showMessageDialog(this, "I don't think it's possible for you to be seeing this message.");
        }

        return layerSizes;
    }
}
