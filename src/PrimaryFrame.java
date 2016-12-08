import javax.swing.*;
import java.awt.*;

public class PrimaryFrame extends JFrame {

    private Dimension defaultSize = new Dimension(700, 500);

    private HyperParameterPanel hyperParameterPanel = new HyperParameterPanel();

    public NetworkGraphicsPanel networkGraphicsPanel = new NetworkGraphicsPanel();

    public PrimaryFrame() {
        super("PrimaryFrame");
        this.init();
    }

    private void init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(this.defaultSize);
        this.setView_optionSelect();
    }

    public void setView_optionSelect() {
        //this.add(hyperParameterPanel, BorderLayout.WEST);
        this.add(networkGraphicsPanel, BorderLayout.CENTER);
    }
}
