package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TwitchViewerModel
{
    private Config config;

    private TwitchViewerDatabase database;
    private HashMap<String, LocalDateTime> currentViewers;
    private LinkedList<String> newViewers;
    private boolean isStreaming;

    public TwitchViewerModel()
    {
        config = new Config("H:/StreamData/Testing/ViewerInfo.db", "moonlightleaf");
        database = new TwitchViewerDatabase(config.getDatabaseString());

        currentViewers = new HashMap<>();
        newViewers = new LinkedList<>();
        isStreaming = false;
    }

    public TwitchViewerModel(Config existingConfig)
    {
        config = existingConfig;
        database = new TwitchViewerDatabase(config.getDatabaseString());

        currentViewers = new HashMap<>();
        newViewers = new LinkedList<>();
        isStreaming = false;
    }

    public void setStreaming(boolean isStreaming)
    {
        this.isStreaming = isStreaming;
    }

    public boolean isStreaming()
    {
        return isStreaming;
    }

    public void updateChannelName(String channelName)
    {
        config.setChannelName(channelName);
        File configFile = new File("config.json");
        try(FileWriter writer = new FileWriter(configFile)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(config, Config.class, writer);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void updateDbPath(String dbPath)
    {
        config.setDatabaseLocation(dbPath);
        File configFile = new File("config.json");
        try(FileWriter writer = new FileWriter(configFile)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(config, Config.class, writer);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void startStream(String[] gameInfo)
    {
        String gameName = gameInfo[0];
        String gameGenre = gameInfo[1];
        int gameID = database.addGame(gameInfo);
        database.startStream(gameID);
        isStreaming = true;
    }

    public void endStream()
    {
        updateMissingViewers(new LinkedList<>());
        database.endStream();
        isStreaming = false;
    }

    public LinkedList<String> requestViewerInfo()
    {
        int chatterCount;
        LinkedList<String> viewers = new LinkedList<>();

        URL url;
        try
        {
            url = new URL(config.getViewerURL());
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(url.openStream()));

            GsonBuilder builder = new GsonBuilder();

            Gson gson = builder.create();
            TwitchJSON.TwitchInfo twitchInfo = gson.fromJson(reader, TwitchJSON.TwitchInfo.class);

            //noinspection UnusedAssignment
            chatterCount = twitchInfo.chatter_count;
            viewers.addAll(Arrays.asList(twitchInfo.chatters.broadcaster));
            viewers.addAll(Arrays.asList(twitchInfo.chatters.vips));
            viewers.addAll(Arrays.asList(twitchInfo.chatters.moderators));
            viewers.addAll(Arrays.asList(twitchInfo.chatters.staff));
            viewers.addAll(Arrays.asList(twitchInfo.chatters.admins));
            viewers.addAll(Arrays.asList(twitchInfo.chatters.viewers));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return viewers;
    }

    public void updateNewViewers(LinkedList<String> viewers)
    {
        newViewers = new LinkedList<>();
        for(String viewer : viewers)
            if (!currentViewers.containsKey(viewer))
            {
                newViewers.add(viewer);
                currentViewers.put(viewer, LocalDateTime.now());
            }
    }

    public void updateMissingViewers(LinkedList<String> viewers)
    {
        LinkedList<Span> newSpans = new LinkedList<>();
        LinkedList<String> removeKeys = new LinkedList<>();
        for(Map.Entry<String, LocalDateTime> entry : currentViewers.entrySet())
        {
            if(!viewers.contains(entry.getKey()))
            {
                newSpans.add(new Span(entry.getKey(), entry.getValue(), LocalDateTime.now()));
                removeKeys.add(entry.getKey());
            }
        }
        for(String user : removeKeys)
            currentViewers.remove(user);

        while(!newSpans.isEmpty())
        {
            database.addSpan(newSpans.remove(), database.getCurrentStreamId());
        }
    }

    public String[] getNewViewers()
    {
        return newViewers.toArray(new String[0]);
    }

    public String[] getCurrentViewers()
    {
        return currentViewers.keySet().toArray(new String[0]);
    }

    public HashMap<String, String> getAllGameInfo()
    {
        return database.getAllGameInfo();
    }

    public Config getConfig()
    {
        return config;
    }
}
