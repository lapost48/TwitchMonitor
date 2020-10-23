package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StreamButton extends JButton
{
    private boolean isStreaming;

    public StreamButton(String label, TwitchViewerGUI observer)
    {
        super(label);
        this.setPreferredSize(new Dimension(125, 30));
        isStreaming = false;
    }
}
