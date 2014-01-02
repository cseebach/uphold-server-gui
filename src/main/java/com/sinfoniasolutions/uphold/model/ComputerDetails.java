package com.sinfoniasolutions.uphold.model;

import com.almworks.sqlite4java.SQLiteConnection;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cameron Seebach on 12/26/13.
 */
public class ComputerDetails {

    private final Jedis jedis;
    private final String computer;
    private final SQLiteConnection db;

    public ComputerDetails(Jedis jedis, SQLiteConnection db, String computer){
        this.jedis = jedis;
        this.db = db;
        this.computer = computer;
    }

    public List<String> getTasksWaiting(){
        ArrayList<String> waiting = new ArrayList<String>(jedis.lrange("tasks:" + computer, 0, -1));
        return waiting;
    }

    public List<String> getRecentMessages(){
        storeRecentMessages();
        return loadRecentMessages();
    }

    private List<String> loadRecentMessages() {
        return new ArrayList<String>();
    }

    private void storeRecentMessages() {
    }

}
