package gui;

import javax.swing.*;
import java.awt.*;

public class TwitchViewerGUI extends JFrame
{
    private JTextField[] gameFields;
    private JButton streamButton;
    private JTextArea viewerList;
    private JTextPane gameList;
    private HashMap<String, JMenuItem> menuItems;
    private JFileChooser dbChooser;

    public TwitchViewerGUI(String title)
    {
        super(title);
        menuItems = new HashMap<>();

        setupContentPane();
//        setIconImage();
        createMenu();

        streamButton = createStreamButton();
        createCenterPanel(streamButton);

        createViewerList();

        createGameList();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
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

    private void createMenu()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu configMenu = new JMenu("Config");
        JMenuItem channelName = new JMenuItem("Channel Name");
        configMenu.add(channelName);
        JMenuItem dbLocation = new JMenuItem(("Database Location"));
        dbChooser = new JFileChooser();
        configMenu.add(dbLocation);
        menuBar.add(configMenu);

        menuItems.put("Channel Name", channelName);
        menuItems.put("Database Location", dbLocation);

        JMenu scriptMenu = new JMenu("Scripts");

        menuBar.add(scriptMenu);

        this.setJMenuBar(menuBar);
    }

    public JFileChooser getFileChooser()
    {
        return dbChooser;
    }

    public HashMap<String, JMenuItem> getMenuItems()
    {
        return menuItems;
    }

    // TODO: Make this function look cleaner. Priority: LOW
    private void createGameInfo(JPanel parent)
    {
        gameFields = new JTextField[2];
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel nameLabel = new JLabel("Game Name");
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 5, 0);
        parent.add(nameLabel, constraints);

        JTextField gameNameTextField = new JTextField();
        gameFields[0] = gameNameTextField;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        parent.add(gameNameTextField, constraints);

        JLabel genreLabel = new JLabel("Game Genre");
        genreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        parent.add(genreLabel, constraints);

        JTextField gameGenreTextField = new JTextField();
        gameFields[1] = gameGenreTextField;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        parent.add(gameGenreTextField, constraints);
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

    private void createCenterPanel(JButton streamButton)
    {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setPreferredSize(new Dimension(200, centerPanel.getPreferredSize().height));

        createGameInfo(centerPanel);

        GridBagConstraints buttonCon = new GridBagConstraints();
        buttonCon.gridwidth = 3;
        buttonCon.gridheight = 1;
        buttonCon.gridx = 0;
        buttonCon.gridy = 2;

        centerPanel.add(streamButton, buttonCon);

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
        viewerList.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1),
                                                                BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        viewerList.setDisabledTextColor(Color.BLACK);
        viewerList.setEnabled(false);

        JScrollPane viewerListScrollPane = new JScrollPane(viewerList);
        viewerListScrollPane.setPreferredSize(new Dimension(150, 200));
        viewerListScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        viewerListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        viewerListPanel.add(viewerListScrollPane);

        this.getContentPane().add(viewerListPanel, BorderLayout.EAST);
    }

    public JTextArea getViewerList()
    {
        return viewerList;
    }

    public void createGameList()
    {
        JPanel gameListPanel = new JPanel();
        gameListPanel.setLayout(new BoxLayout(gameListPanel, BoxLayout.Y_AXIS));
        gameListPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JLabel gameListLabel = new JLabel("Game List");
        gameListPanel.add(gameListLabel);

        gameList = new JTextPane();
        gameList.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1),
                                                              BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        gameList.setDisabledTextColor(Color.BLACK);
        gameList.setEnabled(false);

        JScrollPane gameListScrollPane = new JScrollPane(gameList);
        gameListScrollPane.setPreferredSize(new Dimension(200, 200));
        gameListScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        gameListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        gameListPanel.add(gameListScrollPane);

        this.getContentPane().add(gameListPanel, BorderLayout.WEST);
    }

    public JTextPane getGameList()
    {
        return gameList;
    }
}
