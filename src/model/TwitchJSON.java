package model;

public class TwitchJSON
{
    public class TwitchInfo
    {
        public int chatter_count;
        public Chatters chatters;
    }

    public class Chatters
    {
        public String[] broadcaster;
        public String[] vips;
        public String[] moderators;
        public String[] staff;
        public String[] admins;
        public String[] viewers;
    }

    public class BotInfo
    {
        public String[][] bots;

        public String[] getBotNames()
        {
            String[] botNames = new String[bots.length];
            for(int i = 0; i < bots.length; i++)
                botNames[i] = bots[i][0];
            return botNames;
        }
    }
}
