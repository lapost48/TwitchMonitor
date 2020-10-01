package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StreamButton extends JButton implements ActionListener
{
    private MonitorWindow observer;
    private boolean isStreaming;

    public StreamButton(String label, MonitorWindow observer)
    {
        super(label);
        this.addActionListener(this);
        this.setPreferredSize(new Dimension(125, 30));
        this.observer = observer;
        isStreaming = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(this.getText().equals("Start Stream"))
        {
            this.setText("End Stream");
            isStreaming = true;
        }
        else if(this.getText().equals("End Stream"))
        {
            this.setText("Start Stream");
            isStreaming = false;
        }
        observer.notifyStreamState(isStreaming);
    }
}
