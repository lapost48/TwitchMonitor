package controller;

import java.time.LocalDateTime;

public class Span
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
