package model;

public class TwitchViewerModel
{
    private boolean isStreaming;

    public TwitchViewerModel()
    {

    }

    public void setStreaming(boolean isStreaming)
    {
        this.isStreaming = isStreaming;
    }

    public boolean isStreaming()
    {
        return isStreaming;
    }
}
