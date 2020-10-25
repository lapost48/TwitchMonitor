package model;


import java.nio.file.Path;

public class Config
{
    private Path databaseLocation;
    private String channelName;

    public Config(Path databaseLocation, String channelName)
    {
        this.databaseLocation = databaseLocation;
        this.channelName = channelName;
    }

    public void setDatabaseLocation(Path databaseLocation) {
        this.databaseLocation = databaseLocation;
    }

    public String getDatabaseString()
    {
        return "jdbc:sqlite:" + databaseLocation.toString();
    }

    public void setChannelName(String channelName) {
        if(channelName != null)
            this.channelName = channelName;
    }

    public String getChannelName()
    {
        return channelName;
    }

    public String getViewerURL()
    {
        return "http://tmi.twitch.tv/group/user/" + channelName + "/chatters";
    }
}
