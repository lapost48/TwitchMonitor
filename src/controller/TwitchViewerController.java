package controller;

import gui.TwitchViewerGUI;
import model.TwitchViewerModel;

import java.util.HashMap;
import java.util.LinkedList;
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
        updateGameTable(model.getAllGameInfo());
        viewerUpdate();
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
            updateGameTable(model.getAllGameInfo());
        }
    }

    private void updateGameTable(HashMap<String, String> gameInfo)
    {
        
        StringBuilder builder = new StringBuilder();
        gameInfo.forEach((key, value) -> builder.append(key).append(",").append(value).append("\n"));
        view.updateGameList(builder.toString());
    }

    private void channelNameDialog()
    {
        String channelName = view.updateChannelName();
        model.updateChannelName(channelName);
        viewerUpdate();
    }

    private void dbBrowse()
    {
        String dbPath = this.view.browseDbFile();
        if(dbPath != null)
            model.updateDbPath(dbPath);
        updateGameTable(model.getAllGameInfo());
    }
}
