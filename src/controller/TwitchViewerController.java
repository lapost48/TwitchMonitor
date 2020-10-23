package controller;

import gui.StreamButton;
import gui.TwitchViewerGUI;
import model.TwitchViewerModel;

import javax.swing.*;
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
    }

    public void run()
    {
        while(true) {
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (model.isStreaming())
                viewerUpdate();
        }
    }

    private void streamButtonPress()
    {
        boolean isStreaming = model.isStreaming();
        JButton button = view.getStreamButton();
        JTextField[] gameFields = view.getGameFields();

        for(JTextField field : gameFields)
            field.setEnabled(isStreaming);

        if(isStreaming)
        {
            button.setText("Start Stream");
            model.setStreaming(false);
        }
        else
        {
            button.setText("End Stream");
            model.setStreaming(true);
        }
    }

    // TODO: Implement viewer update function
    private void viewerUpdate()
    {

    }
}
