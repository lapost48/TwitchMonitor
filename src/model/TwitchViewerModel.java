package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TwitchViewerModel
{
    private TwitchViewerDatabase database;

    private HashMap<String, LocalDateTime> currentViewers;
    private LinkedList<String> newViewers;
    private boolean isStreaming;

    public TwitchViewerModel()
    {
        database = new TwitchViewerDatabase("jdbc:sqlite:H:/StreamData/Testing/ViewerInfo.db");

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

    public LinkedList<String> requestViewerInfo()
    {
        int chatterCount;
        LinkedList<String> viewers = new LinkedList<>();

        URL url;
        try
        {
            url = new URL("http://tmi.twitch.tv/group/user/moonlightleaf/chatters");
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(url.openStream()));

            GsonBuilder builder = new GsonBuilder();

            Gson gson = builder.create();
            TwitchJSON.TwitchInfo twitchInfo = gson.fromJson(reader, TwitchJSON.TwitchInfo.class);

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
        LinkedList<String> newViewers = new LinkedList<>();
        for(String viewer : viewers)
            if(!currentViewers.containsKey(viewer))
                currentViewers.put(viewer, LocalDateTime.now());
    }

    public void updateMissingViewers(LinkedList<String> viewers)
    {
        if(viewers == null)
            viewers = new LinkedList<>();

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
        return newViewers.toArray(new String[0]);
    }
}
