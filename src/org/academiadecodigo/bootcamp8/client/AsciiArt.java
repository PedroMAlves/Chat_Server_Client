package org.academiadecodigo.bootcamp8.client;

/**
 * Created by prashanta on 18-06-2017.
 */
public enum AsciiArt {
    ALIEN("/Alien", "The face of an alien. Usage /ascii alien", "/ascii alien"),
    DRWHO("/Dr_Who", "Tribute to Dr Who. Usage /ascii drwho", "/ascii drwho"),
    SKULL("/Skull_Sword", "A sword and skull. Usage /ascii skull", "/ascii skull"),
    MONKEY("/Monkey_Bananas", "Monkey praying for Bananas. Usage /ascii monkey", "/ascii monkey"),
    LOVEYOU("/I_Love_You", "Show your love to someone special. Usage /ascii loveyou", "/ascii loveyou");


    private final String filePath;
    private final String description;
    private final String command;

    AsciiArt(String path, String description, String cmd) {
        filePath = path;
        this.description = description;
        command = cmd;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDescription() {
        return description;
    }

    public String getCommand() {
        return command;
    }
}
