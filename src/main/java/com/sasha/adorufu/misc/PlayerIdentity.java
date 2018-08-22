package com.sasha.adorufu.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sasha.adorufu.AdorufuMod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerIdentity implements Serializable {
    private String displayName;
    private String stringUuid;

    public PlayerIdentity(String stringUuid) {
        String formattedUuid = stringUuid.replace("-", "");
        this.stringUuid = stringUuid;
        this.displayName = "Loading...";
        new Thread(() -> {
            LinkedHashMap<String, Long> map = getNameHistory(formattedUuid);
            AtomicReference<String> latest = new AtomicReference<>();
            latest.set(stringUuid);
            map.forEach((str, lon) -> latest.set(str));
            this.displayName = latest.get();
            AdorufuMod.DATA_MANAGER.identityCacheMap.put(this.getStringUuid(), this);
            try {
                AdorufuMod.DATA_MANAGER.savePlayerIdentity(this, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public String getDisplayName() {
        return this.displayName;
    }
    public String getStringUuid() {
        return this.stringUuid;
    }

    /**
     * run this on a seperate thread pls so u dont kill the game
     */
    public void updateDisplayName() {
        new Thread(() -> {
            PlayerIdentity identity = new PlayerIdentity(this.stringUuid);
            this.displayName = identity.getDisplayName();
            identity = null;
        }).start();
    }











    private static LinkedHashMap<String, Long> getNameHistory(String UUID) {
        LinkedHashMap<String, Long> nameHistories = new LinkedHashMap<>();
        try {
            URL e = new URL("https://api.mojang.com/user/profiles/" + UUID.replace("-", "") + "/names");
            URLConnection connection = e.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonb.append(line + "\n");
            }
            String formattedjson = jsonb.toString();
            reader.close();

            JsonArray array = new JsonParser().parse(formattedjson).getAsJsonArray();
            for(int i = 0; i < array.size(); i++) {
                JsonObject obj = array.get(i).getAsJsonObject();
                String nameform = obj.get("name").getAsString();
                try {
                    obj.get("changedToAt");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(obj.get("changedToAt").getAsLong());
                    long changedAt = obj.get("changedToAt").getAsLong();
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    //nameform = nameform + " @ " + getDateFormat(mMonth + 1, mDay, mYear);
                    nameHistories.put(nameform, changedAt);
                }catch (Exception ee) {
                    /*
                    StringWriter w = new StringWriter();
                    PrintWriter writer = new PrintWriter(w);
                    ee.printStackTrace(writer);
                    pushNotify(w.toString());
                    */
                    nameHistories.put(nameform + " @ Before existence", null);
                }
            }

            /*JSONObject jsonObject = new JSONObject(formattedjson);
            JSONArray ja = jsonObject.getJSONArray("name");
            for ()*/
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.print("fuck");
        }
        return nameHistories;
    }
    private static String getDateFormat(int mm, int dd, int yyyy) {
        return mm + "/" + dd + "/" + yyyy;
    }
}
