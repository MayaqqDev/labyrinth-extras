package dev.mayaqq.labyrinthextras.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class LabyrinthExtrasConfig {

    private static final File configFolder = new File(FabricLoader.getInstance().getConfigDir() + "/labyrinthextras");
    private static final File configFile = new File(configFolder, "config.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Config CONFIG = new Config();

    public static void load() {
        //we do bunch of checking here mainly if the file exists
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                CONFIG = gson.fromJson(new FileReader(configFile), Config.class);
                save();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter(configFile);
            gson.toJson(CONFIG, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Config {
        public boolean disableEndPortal = true;
        public String botToken = "";
        public String botChannel = "";
        public HashMap<UUID, Boolean> players = new HashMap<>();
        public Config() {}
    }
}
