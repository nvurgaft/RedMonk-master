package com.nvurgaft.redmonk.Entities;

/**
 * Created by Koby on 02-Jul-15.
 */
public class Reminder {

    private long reminderId;
    private int hour;
    private int minute;
    private String todo;
    private String resolved;

    public Reminder() {
    }

    public long getReminderId() {
        return reminderId;
    }

    public void setReminderId(long reminderId) {
        this.reminderId = reminderId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getResolved() {
        return resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "reminderId=" + reminderId +
                ", hour=" + hour +
                ", minute=" + minute +
                ", todo='" + todo + '\'' +
                ", resolved='" + resolved + '\'' +
                '}';
    }
}
