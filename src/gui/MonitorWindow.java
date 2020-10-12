package gui;

import javax.swing.*;
import java.awt.*;

public class MonitorWindow extends JFrame
{
    private ViewerMonitor observer;
    private JTextField[] gameFields;

    public MonitorWindow(String title, ViewerMonitor observer)
    {
        super(title);
        this.observer = observer;
        gameFields = new JTextField[2];

        setupContentPane();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 200);

        createGameInfoPanel();

        createStreamButton();

        setIconImage();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 200);
        this.setVisible(true);
    }

    private void setupContentPane()
    {
        this.setContentPane(new JPanel());
    }

    private void createGameInfoPanel()
    {
        JPanel gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new BoxLayout(gameInfoPanel, BoxLayout.Y_AXIS));

        JPanel gameNamePanel = new JPanel();
        gameNamePanel.add(new JLabel("Game Name"));
        JTextField gameNameTextField = new JTextField();
        gameNameTextField.setPreferredSize(new Dimension(100, 20));
        gameNamePanel.add(gameNameTextField);
        gameFields[0] = gameNameTextField;

        JPanel gameGenrePanel = new JPanel();
        gameGenrePanel.add(new JLabel("Game Genre"));
        JTextField gameGenreTextField = new JTextField();
        gameGenreTextField.setPreferredSize(new Dimension(100, 20));
        gameGenrePanel.add(gameGenreTextField);
        gameFields[1] = gameGenreTextField;

        gameInfoPanel.add(gameNamePanel);
        gameInfoPanel.add(gameGenrePanel);

        this.getContentPane().add(gameInfoPanel);
    }

    private void createStreamButton()
    {
        JButton streamButton = new StreamButton("Start Stream", this);
        this.getContentPane().add(streamButton);
    }

    private void setIconImage()
    {
//        ImageIcon img = new ImageIcon(getClass().getResource("C:\\Users\\Nick LaPosta\\IdeaProjects\\TwitchMonitor\\Images\\MoonIcon.png"));
//        this.setIconImage(img.getImage());
    }

    public void notifyStreamState(boolean isStreaming) {
        gameFields[0].setEnabled(!isStreaming);
        gameFields[1].setEnabled(!isStreaming);
        if(isStreaming)
            observer.notifyGameInfo(gameFields[0].getText(), gameFields[1].getText());
        observer.notifyStreamState(isStreaming);
    }
}
