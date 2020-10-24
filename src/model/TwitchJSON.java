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
}
