
import javax.swing.*;
import java.awt.*;

public class PrimaryFrame extends JFrame {

    Network network;

    Train train;

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
     *
     * @param network
     * @param train
     */
    public PrimaryFrame(Network network, Train train) {
        super("PrimaryFrame");
        this.init(network, train, new NetworkSettings());
    }


    /**
     *
     */
    public PrimaryFrame() {
        NetworkSettings networkSettings = new NetworkSettings();
        Train train = new Train(networkSettings);
        Network network = new Network(networkSettings, train);
        this.init(network, train, networkSettings);
    }


    /**
     *
     * @param network
     */
    private void init(Network network, Train train, NetworkSettings networkSettings) {
        this.train = train;
        this.network = network;

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(this.defaultSize);

        this.networkGraphicsPanel = new NetworkGraphicsPanel(network);
        this.trainAnimation = this.networkGraphicsPanel.timerAnimation_trainCyclingInput(this.network, this.train, networkSettings);
        this.networkSettingsPanel = new NetworkSettingsPanel(this);

        this.setView_optionSelect();
    }


    /**
     *
     */
    public void setView_optionSelect() {
        this.add(this.networkSettingsPanel, BorderLayout.WEST);
        this.add(this.networkGraphicsPanel, BorderLayout.CENTER);

    }


    /**
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
    }



    /**
     *
     * @return
     */
    public Timer getTrainAnimation() {
        return this.trainAnimation;
    }
}
