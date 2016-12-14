import javax.swing.*;
import java.awt.*;

public class NetworkSettingsPanel extends JPanel {

    private Dimension
        preferredSize = new Dimension(250, -1);

    private String[]

        opt_dataset = new String[]{
            "Census Salary",
            "Braille",
            "Red Wine",
            "White Wine",
            "OR",
            "AND",
            "XOR"
        },

        opt_activation = new String[]{
            "Tanh",
            "ReLU",
            "Leaky ReLU",
            "Sigmoid",
            "Linear"
        },

        //opt_learningRate = new String[]{
        //    "0.5",
        //    "0.1",
        //    "0.05",
        //    "0.01",
        //    "0.01",
        //    "0.005",
        //    "0.001",
        //    "0.0005",
        //    "0.0001"
        //},

        opt_initialWeights = new String[]{
            "Gaussian mean=0 stddev=1",
            "Gaussian mean=1 stddev=1",
            "Gaussian mean=0 stddev=5",
            "Gaussian mean=5 stddev=5",
            "Gaussian mean=0 stddev=0.1",
            "Gaussian mean=.1 stddev=0.1",
            "Uniform [0, 0.1]",
            "Uniform [-0.1, 0.1]"
        };

    private JLabel
        label_dataset = new JLabel("Dataset"),
        label_activation = new JLabel("Activation"),
        label_learningRate = new JLabel("Learning Rate"),
        label_initialWeights = new JLabel("Initial Weights");

    private JButton
        button_startPause = new JButton("Start"),
        button_reset = new JButton("Reset");

    private JComboBox
        combo_dataset = new JComboBox(this.opt_dataset),
        combo_activation = new JComboBox(this.opt_activation),
        //combo_learningrate = new JComboBox(this.opt_learningRate),
        combo_initialWeights = new JComboBox(this.opt_initialWeights);

    private JSlider
        slider_learningrate = new JSlider();

    private double
        learningRateValue = 0.01;


    private PrimaryFrame
        primaryFrame;


    /**
     *
     * @param primaryFrame
     */
    public NetworkSettingsPanel(PrimaryFrame primaryFrame) {
        super();
        this.primaryFrame = primaryFrame;
        this.init();
    }

    /**
     *
     */
    private void init() {
        // set up layout deatils
        //
        this.setLayout(new GridLayout(11, 1));
        this.setBorder(BorderFactory.createTitledBorder("Parameters"));
        this.setPreferredSize(this.preferredSize);

        // style components
        //
        this.slider_learningrate.setMinorTickSpacing(10);
        this.slider_learningrate.setMajorTickSpacing(10);
        this.slider_learningrate.setPaintTicks(true);
        //this.slider_learningrate.setPaintLabels(true);

        // add components
        //
        this.add(this.label_dataset);
        this.add(this.combo_dataset);

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
     *
     * @return
     */
    public NetworkSettings getNetworkSettings() {
        NetworkSettings settings = new NetworkSettings();

        settings.setActivation(this.combo_activation.getSelectedIndex());
        settings.setInitialWeights(this.combo_initialWeights.getSelectedIndex());
        settings.setDataSet(this.combo_dataset.getSelectedIndex());
        settings.setLearningRate(this.learningRateValue);

        return settings;
    }


    /**
     * binds various events of this panels components
     *
     */
    private void bindEventHandlers() {
        this.slider_learningrate.addChangeListener(e -> {
            this.learningRateValue = .001 + 5 * this.slider_learningrate.getValue() / 1000d;
            this.label_learningRate.setText("Learning Rate: " + this.learningRateValue);
        });

        this.button_reset.addActionListener(e -> {
            this.primaryFrame.setNetworkSettings(this.getNetworkSettings());
        });

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
}
