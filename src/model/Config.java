package model;


public class Config
{
    private String databaseLocation;
    private String channelName;
    private String botListURL;

    public Config(String databaseLocation, String channelName)
    {
        this.databaseLocation = databaseLocation;
        this.channelName = channelName;
        this.botListURL = "https://api.twitchinsights.net/v1/bots/online";
    }

    public void setDatabaseLocation(String databaseLocation) {
        this.databaseLocation = databaseLocation;
    }

    public String getDatabaseString()
    {
        return "jdbc:sqlite:" + databaseLocation;
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

    public String getBotListURL()
    {
        return botListURL;
    }
}
