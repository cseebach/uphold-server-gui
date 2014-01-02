package com.sinfoniasolutions.uphold.model;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import org.yaml.snakeyaml.Yaml;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Cameron Seebach on 12/26/13.
 */
public class UpholdModel {

    private final String redisURL;
    private final Jedis jedis;
    private final SQLiteConnection db;

    public UpholdModel() throws FileNotFoundException, URISyntaxException, SQLiteException {
        Yaml yaml = new Yaml();
        Map map = (Map) yaml.load(new FileInputStream("uphold.txt"));
        redisURL = (String) map.get("redis");
        jedis = new Jedis(new URI(redisURL));
        db = new SQLiteConnection(new File("logs.db"));
        db.open(true);
        db.exec("CREATE TABLE IF NOT EXISTS log (" +
                "computer text," +
                "time text," +
                "data text" +
                ");" +
                "CREATE INDEX IF NOT EXISTS log_computer ON log ( computer );" +
                "CREATE INDEX IF NOT EXISTS log_time ON log ( time );");
    }

    public List<String> getComputers() {
        return new ArrayList<String>(jedis.smembers("subscriptions"));
    }

    public ComputerDetails getDetails(String computer) {
        return new ComputerDetails(jedis, db, computer);
    }

}
