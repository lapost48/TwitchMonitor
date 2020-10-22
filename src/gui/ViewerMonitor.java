package gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.text.View;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ViewerMonitor
{
    public static class Span
    {
        private String username;
        private LocalDateTime[] watchTimes;

        public Span(String username, LocalDateTime startTime, LocalDateTime endTime)
        {
            this.username = username;
            watchTimes = new LocalDateTime[]{startTime, endTime};
        }

        public String getUsername(){return username;}
        public LocalDateTime getStartTime(){return watchTimes[0];}
        public LocalDateTime getEndTime(){return watchTimes[1];}
    }

    public static class TwitchInfo
    {
        public int chatter_count;
        public Chatters chatters;
    }

    public static class Chatters
    {
        public String[] broadcaster;
        public String[] vips;
        public String[] moderators;
        public String[] staff;
        public String[] admins;
        public String[] viewers;
    }

    private String dbName;
    private boolean isStreaming;
    private String gameName;
    private String gameGenre;
    private int currentStreamId;
    private HashMap<String, LocalDateTime> currentViewers;
    private Connection conn;

    public ViewerMonitor(String dbName)
    {
        this.dbName = dbName;
        isStreaming = false;
        gameName = "None Selected";
        gameGenre = "Unknown";
        currentStreamId = 0;
        currentViewers = new HashMap<String, LocalDateTime>();
        conn = null;

        initDB();
    }

    private void initDB()
    {
        // Create a connection to the database
        try {
            renewConnection();
            // Create streams table
            String sql = "CREATE TABLE IF NOT EXISTS streams (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	start_time text NOT NULL,\n"
                    + "	end_time text,\n"
                    + "	game_id int NOT NULL\n"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);

            renewConnection();
            // Create games table
            sql = "CREATE TABLE IF NOT EXISTS games (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	name text NOT NULL,\n"
                    + "	genre text NOT NULL\n"
                    + ");";
            stmt = conn.createStatement();
            stmt.execute(sql);

            renewConnection();
            // Create spans table
            sql = "CREATE TABLE IF NOT EXISTS spans (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	username text NOT NULL,\n"
                    + "	start_time text NOT NULL,\n"
                    + "	end_time text NOT NULL,\n"
                    + "	stream_id int NOT NULL\n"
                    + ");";
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void newDbLocation(String dbName)
    {
        this.dbName = dbName;
        conn = null;

        initDB();
    }

    public void viewerUpdate()
    {
        int chatterCount = 0;
        LinkedList<String> viewers = new LinkedList<String>();

        URL url = null;
        try
        {
            url = new URL("http://tmi.twitch.tv/group/user/moonlightleaf/chatters");
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(url.openStream()));

            GsonBuilder builder = new GsonBuilder();

            Gson gson = builder.create();
            TwitchInfo twitchInfo = gson.fromJson(reader, TwitchInfo.class);

            chatterCount = twitchInfo.chatter_count;
            viewers.addAll(Arrays.asList(twitchInfo.chatters.broadcaster));
            viewers.addAll(Arrays.asList(twitchInfo.chatters.vips));
            viewers.addAll(Arrays.asList(twitchInfo.chatters.moderators));
            viewers.addAll(Arrays.asList(twitchInfo.chatters.staff));
            viewers.addAll(Arrays.asList(twitchInfo.chatters.admins));
            viewers.addAll(Arrays.asList(twitchInfo.chatters.viewers));

//            System.out.println("Chatter Count: " + chatterCount);
//            System.out.println("Chatters: ");
//            for (String s: viewers)
//                System.out.println("\t" + s);

        }
        catch (Exception e)
        {
            if(e instanceof MalformedURLException)
                e.printStackTrace();
            if(e instanceof IOException)
                e.printStackTrace();
        }

        updateNewViewers(viewers);
        updateMissingViewers(viewers);
    }

    public void updateNewViewers(LinkedList<String> viewers)
    {
        LinkedList<String> newViewers = new LinkedList<String>();
        for(String viewer : viewers)
            if(!currentViewers.containsKey(viewer))
                currentViewers.put(viewer, LocalDateTime.now());
    }

    public void updateMissingViewers(LinkedList<String> viewers)
    {
        if(viewers == null)
            viewers = new LinkedList<String>();

        LinkedList<Span> newSpans = new LinkedList<Span>();
        LinkedList<String> removeKeys = new LinkedList<String>();
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

        if(!newSpans.isEmpty()) {
            // Create a connection to the database
            try {
                renewConnection();
                for (Span s : newSpans) {
                    // Insert Spans
                    String sql = "INSERT INTO spans(username,start_time,end_time,stream_id) VALUES(?,?,?,?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, s.getUsername());
                    pstmt.setString(2, s.getStartTime().toString());
                    pstmt.setString(3, s.getEndTime().toString());
                    pstmt.setInt(4, currentStreamId);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean isStreaming()
    {
        return isStreaming;
    }

    public void notifyStreamState(boolean streaming) {
        if(!isStreaming && streaming)
        {
            try {
                renewConnection();
                // Check for existing game
                String sql = "SELECT id FROM games " +
                        "WHERE name = ?;";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, gameName);
                int gameID = pstmt.executeQuery().getInt("id");

                renewConnection();
                // Insert Stream
                sql = "INSERT INTO streams(start_time,game_id) VALUES(?,?);";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, LocalDateTime.now().toString());
                pstmt.setInt(2, gameID);
                pstmt.executeUpdate();

                renewConnection();
                // Get latest stream id
                sql = "SELECT MAX(id) FROM streams;";
                Statement stmt = conn.createStatement();
                currentStreamId = stmt.executeQuery(sql).getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if(conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            viewerUpdate();
        }
        if(!streaming && isStreaming){
            String endTime = LocalDateTime.now().toString();
            // Create a connection to the database
            try {
                renewConnection();

                // Update Stream
                String sql = "UPDATE streams SET end_time = ? "
                        + "WHERE id = ?;";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, endTime);
                pstmt.setInt(2, currentStreamId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if(conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            updateMissingViewers(null);
        }
        isStreaming = streaming;
    }

    public void notifyGameInfo(String gameName, String gameGenre) {
        if(this.gameName != null)
            this.gameName = gameName;
        if(this.gameGenre != null)
            this.gameGenre = gameGenre;

        // Create a connection to the database
        try {
            renewConnection();
            // Check for existing game
            String sql = "SELECT * FROM games " +
                    "WHERE name = ?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gameName);
            ResultSet result = pstmt.executeQuery();

            if(result.isClosed())
            {
                renewConnection();
                // Insert Game
                sql = "INSERT INTO games(name,genre) " +
                        "VALUES(?,?);";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, gameName);
                pstmt.setString(2, gameGenre);
                pstmt.executeUpdate();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void renewConnection() throws SQLException
    {
        if(conn != null)
            conn.close();
        conn = DriverManager.getConnection(dbName);
   }
}
