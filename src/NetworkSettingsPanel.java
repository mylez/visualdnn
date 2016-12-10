import javax.swing.*;
import java.awt.*;

public class NetworkSettingsPanel extends JPanel {

    Dimension
        preferredSize = new Dimension(250, -1);

    String[]
        opt_dataset = new String[]{"Census Salary", "Braille", "Red Wine", "White Wine", "OR", "AND", "XOR"},
        opt_activation = new String[]{"Tanh", "ReLU", "Leaky ReLU", "Sigmoid", "Linear"},
        opt_learningRate = new String[]{"0.5", "0.1", "0.05", "0.01", "0.01", "0.005", "0.001", "0.0005", "0.0001"},
        opt_initialWeights = new String[]{"Normal", "Half-Normal", "Uniform [0, 1]", "Uniform [-1, 1]", "Uniform [0, 0.1]", "Uniform [-0.1, 0.1]"};

    JLabel
        label_dataset = new JLabel("Dataset"),
        label_activation = new JLabel("Activation"),
        label_learningRate = new JLabel("Learning Rate"),
        label_initialWeights = new JLabel("Initial Weights");

    JComboBox
        combo_dataset = new JComboBox(this.opt_dataset),
        combo_activation = new JComboBox(this.opt_activation),
        combo_learningrate = new JComboBox(this.opt_learningRate),
        combo_initialWeights = new JComboBox(this.opt_initialWeights);


    public NetworkSettingsPanel() {
        super();
        this.init();
    }


    private void init() {
        // set up layout deatils
        //
        this.setLayout(new GridLayout(10, 1));
        this.setBorder(BorderFactory.createTitledBorder("Parameters"));
        this.setPreferredSize(this.preferredSize);

        // add components
        //
        this.add(label_dataset);
        this.add(combo_dataset);

        this.add(label_activation);
        this.add(combo_activation);

        this.add(label_learningRate);
        this.add(combo_learningrate);

        this.add(label_initialWeights);
        this.add(combo_initialWeights);

        this.bindEventHandlers();
    }


    /**
     * binds various event types of this panels components
     *
     */
    private void bindEventHandlers() {
        this.combo_dataset.addActionListener((e) ->{
            if (e.getActionCommand() ==  "comboBoxChanged") {
                this.combo_dataset.getSelectedIndex();
            }
        });
    }

    public void updateNetworkSettings() {

    }
}
