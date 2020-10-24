package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TwitchViewerGUI extends JFrame
{
    private JTextField[] gameFields;
    private JButton streamButton;
    private JTextArea viewerList;

    public TwitchViewerGUI(String title)
    {
        super(title);

        setupContentPane();
//        setIconImage();

        JPanel gameInfoPanel = createGameInfoPanel();
        streamButton = createStreamButton();
        createCenterPanel(gameInfoPanel, streamButton);

        createViewerList();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 200);
        this.setVisible(true);
        this.pack();
    }

    private void setupContentPane()
    {
        this.setContentPane(new JPanel(new BorderLayout()));
    }

    private void setIconImage()
    {
//        ImageIcon img = new ImageIcon(getClass().getResource("C:\\Users\\Nick LaPosta\\IdeaProjects\\TwitchMonitor\\Images\\MoonIcon.png"));
//        this.setIconImage(img.getImage());
    }

    // TODO: Make this function look cleaner. Priority: LOW
    private JPanel createGameInfoPanel()
    {
        gameFields = new JTextField[2];

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

        return gameInfoPanel;
    }

    public JTextField[] getGameFields()
    {
        return gameFields;
    }

    private JButton createStreamButton()
    {
        return  new JButton("Start Stream");
    }

    public JButton getStreamButton()
    {
        return streamButton;
    }

    private void createCenterPanel(JPanel gameInfoPanel, JButton streamButton)
    {
        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(200, 100));
        centerPanel.add(gameInfoPanel);
        centerPanel.add(streamButton);

        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

    private void createViewerList()
    {
        JPanel viewerListPanel = new JPanel();
        viewerListPanel.setLayout(new BoxLayout(viewerListPanel, BoxLayout.Y_AXIS));
        viewerListPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JLabel viewerListLabel = new JLabel("Viewer List");
        viewerListPanel.add(viewerListLabel);

        viewerList = new JTextArea();
        viewerList.setBorder(BorderFactory.createBevelBorder(1));
        viewerList.setDisabledTextColor(Color.BLACK);
        viewerList.setEnabled(false);

        JScrollPane viewerListScrollPane = new JScrollPane(viewerList);
        viewerListScrollPane.setPreferredSize(new Dimension(150, 200));
        viewerListScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        viewerListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        viewerListPanel.add(viewerListScrollPane);

        this.getContentPane().add(viewerListPanel, BorderLayout.WEST);
    }

    public JTextArea getViewerList()
    {
        return viewerList;
    }
}
