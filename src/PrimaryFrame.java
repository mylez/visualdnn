
import javax.swing.*;
import java.awt.*;

public class PrimaryFrame extends JFrame {

    private Dimension defaultSize = new Dimension(700, 500);

    public NetworkSettingsPanel networkSettingsPanel;
    public NetworkGraphicsPanel networkGraphicsPanel;

    public PrimaryFrame(Network network) {
        super("PrimaryFrame");
        this.init(network);
    }

    private void init(Network network) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(this.defaultSize);

        this.networkSettingsPanel = new NetworkSettingsPanel();
        this.networkGraphicsPanel = new NetworkGraphicsPanel(network);

        this.setView_optionSelect();
    }

    public void setView_optionSelect() {
        this.add(this.networkSettingsPanel, BorderLayout.WEST);
        this.add(this.networkGraphicsPanel, BorderLayout.CENTER);
    }
}
