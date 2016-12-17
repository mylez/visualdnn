
import javax.swing.*;
import java.awt.*;

public class PrimaryFrame extends JFrame {

    Network
        network;

    Train
        train;

    private Dimension
        defaultSize = new Dimension(700, 500);

    //private NetworkSettings
    //    networkSettings = new NetworkSettings();

    public NetworkSettingsPanel
        networkSettingsPanel;

    public NetworkGraphicsPanel
        networkGraphicsPanel;

    private Timer
        trainAnimation;


    /**
     * create a new primary frame, which will in turn
     * instantiate new NetworkSettings, Train, and Network
     * objects with default settings
     *
     */
    public PrimaryFrame() {
        NetworkSettings networkSettings = new NetworkSettings();
        Train train = new Train(networkSettings);
        Network network = new Network(networkSettings, train);
        this.init(network, train, networkSettings);

        this.setNetworkSettings(this.networkSettingsPanel.getNetworkSettings());
    }


    /**
     * initialize the PrimaryFrame object
     *
     * @param network
     */
    private void init(Network network, Train train, NetworkSettings networkSettings) {
        this.setLayout(new BorderLayout());
        this.train = train;
        this.network = network;

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(this.defaultSize);

        this.networkGraphicsPanel = new NetworkGraphicsPanel(network, train, networkSettings);
        this.trainAnimation = this.networkGraphicsPanel.timerAnimation_trainCyclingInput(this.network, this.train, networkSettings);
        this.networkSettingsPanel = new NetworkSettingsPanel(this);

        this.setView_optionSelect();
    }


    /**
     * set the view to the optionSelect view. this ended
     * up beign the only view required for this project
     *
     */
    public void setView_optionSelect() {
        this.add(this.networkSettingsPanel, BorderLayout.WEST);
        this.add(this.networkGraphicsPanel, BorderLayout.CENTER);

    }


    /**
     * take a NetworkSettings object, update the current
     * network settings, and repaint the network graphics
     * panel to reflect the new settings
     *
     * @param networkSettings
     */
    public void setNetworkSettings(NetworkSettings networkSettings) {
        boolean wasRunning = this.trainAnimation.isRunning();

        this.train = new Train(networkSettings);
        this.network = new Network(networkSettings, this.train);
        this.trainAnimation.stop();
        this.trainAnimation = this.networkGraphicsPanel.timerAnimation_trainCyclingInput(this.network, this.train, networkSettings);

        if (wasRunning) {
            this.trainAnimation.start();
        }

        this.networkGraphicsPanel.setInitialDrawState(this.network, this.train, networkSettings);
        this.networkGraphicsPanel.repaint();
    }



    /**
     * return the current train animation object
     *
     * @return
     */
    public Timer getTrainAnimation() {
        return this.trainAnimation;
    }
}
