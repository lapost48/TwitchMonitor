package model;

import java.sql.*;
import java.time.LocalDateTime;

public class TwitchViewerDatabase
{
    private Connection conn;
    private String dbName;

    private int currentStreamId;

    public TwitchViewerDatabase(String dbName)
    {
        this.dbName = dbName;
        try {
            conn = DriverManager.getConnection(dbName);
            initDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initDB() throws SQLException
    {
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
        renewConnection();
    }

    public void setDbLocation(String dbName) throws SQLException
    {
        this.dbName = dbName;
        conn.close();
        conn = null;
        initDB();
    }

    private void renewConnection() throws SQLException
    {
        if(conn != null)
            conn.close();
        conn = DriverManager.getConnection(dbName);
   }

   public void startStream(String gameName)
   {
       try {
            // Check for existing game
            String sql = "SELECT id FROM games " +
                    "WHERE name = ?;";
            PreparedStatement pstmt;
               pstmt = conn.prepareStatement(sql);

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
            renewConnection();
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

   public void addSpan(Span span, int currentStreamId)
   {
       try {
           // Insert Spans
           String sql = "INSERT INTO spans(username,start_time,end_time,stream_id) VALUES(?,?,?,?)";
           PreparedStatement stmt = conn.prepareStatement(sql);
           stmt.setString(1, span.getUsername());
           stmt.setString(2, span.getStartTime().toString());
           stmt.setString(3, span.getEndTime().toString());
           stmt.setInt(4, currentStreamId);
           stmt.executeUpdate();
           renewConnection();
       } catch(SQLException e) {
           e.printStackTrace();
       }
   }

    public int getCurrentStreamId()
    {
        return currentStreamId;
    }
}
