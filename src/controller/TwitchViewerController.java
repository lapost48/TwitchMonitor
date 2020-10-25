package controller;

import gui.TwitchViewerGUI;
import model.TwitchViewerModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TwitchViewerController
{
    private TwitchViewerModel model;
    private TwitchViewerGUI view;
    public TwitchViewerController(TwitchViewerModel model, TwitchViewerGUI view)
    {
        this.model = model;
        this.view = view;
    }

    public void init()
    {
        this.view.getStreamButton().addActionListener(e -> streamButtonPress());
        this.view.getMenuItems().get("Database Location").addActionListener(e -> dbBrowse());
        this.view.getMenuItems().get("Channel Name").addActionListener(e -> channelNameDialog());
        setGameText(model.getAllGameInfo());
    }

    public void run()
    {
        //noinspection InfiniteLoopStatement
        while(true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (model.isStreaming())
                viewerUpdate();
        }
    }

    private void viewerUpdate()
    {
        LinkedList<String> viewerInfo = model.requestViewerInfo();

        model.updateNewViewers(viewerInfo);
        model.updateMissingViewers(viewerInfo);

        String[] newViewers = model.getNewViewers();
        String[] currentViewers = model.getCurrentViewers();

        StringBuilder users = new StringBuilder();
        for(String user : currentViewers)
            users.append(user).append("\n");
        view.getViewerList().setText(users.toString().trim());
    }

    public void exit()
    {
        if(model.isStreaming())
            model.endStream();
    }

    private void streamButtonPress()
    {
        boolean isStreaming = model.isStreaming();
        String[] gameInfo = new String[2];

        for(int i = 0; i < view.getGameFields().length; i++)
        {
            view.getGameFields()[i].setEnabled(isStreaming);
            gameInfo[i] = view.getGameFields()[i].getText();
        }


        if(isStreaming)
        {
            view.getStreamButton().setText("Start Stream");
            model.endStream();
        }
        else
        {
            view.getStreamButton().setText("End Stream");
            model.startStream(gameInfo);
            setGameText(model.getAllGameInfo());
        }
    }

    private void setGameText(HashMap<String, String> gameInfo)
    {
        StringBuilder builder = new StringBuilder();
        gameInfo.forEach((key, value) -> builder.append(key).append(" | ").append(value).append("\n"));
        view.getGameList().setText(builder.toString());
    }

    private void mouseClicked(MouseEvent me)
    {

    }

    private void channelNameDialog()
    {
        String channelName = JOptionPane.showInputDialog(
                this.view,
                "Enter the channel name.",
                "Channel Name",
                JOptionPane.PLAIN_MESSAGE);
        model.updateChannelName(channelName);
    }

    private void dbBrowse()
    {
        int returnVal = this.view.getFileChooser().showOpenDialog(this.view);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Path dbPath = Paths.get(this.view.getFileChooser().getSelectedFile().getAbsolutePath());
            model.updateDbPath(dbPath);
        }
    }
}
