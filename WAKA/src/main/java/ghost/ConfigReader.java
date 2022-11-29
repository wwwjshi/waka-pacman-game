package ghost;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConfigReader {
    /**
     * File name of map file specified in configuration file
     */
    private String mapName;

    /**
     * Contents inside map file as list of strings
     */
    private ArrayList<String> mapStrArr = new ArrayList<String>();

    /**
     * Value of lives specified in configuration file
     */
    private int lives;

    /**
     * Value of speed specified in configuration file
     */
    private int speed;

    /**
     * Values of mode lengths specified in configuration file.
     */
    private ArrayList<Integer> modeLengths = new ArrayList<Integer>();

    /**
     * Value of frightened length specified in configuration file
     */
    private int frightenedLength;
    
    /**
     * Constructor of ConfigReader
     * @param configFile, the file name of configuration file
     * read and store data in configuartion file
     */
    public ConfigReader(String configFile) {
        
        JSONParser parser = new JSONParser();

        try {     
            Object obj = parser.parse(new FileReader(configFile));
            JSONObject jsonObj = (JSONObject) obj;

            String mapName = (String) jsonObj.get("map");
            this.mapName = mapName;

            Long lives = (Long) jsonObj.get("lives");
            this.lives = lives.intValue();

            Long speed = (Long) jsonObj.get("speed");
            this.speed = speed.intValue();

            Long frightenedLength = (Long) jsonObj.get("frightenedLength");
            this.frightenedLength = frightenedLength.intValue();
            
            JSONArray modeLengths = (JSONArray) jsonObj.get("modeLengths");
            for(Object num: modeLengths){
                Long val = (Long) num;
                this.modeLengths.add(val.intValue());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return lines in map file as list of strings
     */
    public ArrayList<String> getMap() {
        File f = new File(this.mapName);
        ArrayList<String> temp = new ArrayList<String>();
        try{
            Scanner sc = new Scanner(f);
            while(sc.hasNextLine()){
                String s = sc.nextLine();
                temp.add(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.mapStrArr = temp;
        return this.mapStrArr;
    }

    /**
     * @return value of lives
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * @return value of speed
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * @return value of frigtened length
     */
    public int getFrightenedLength() {
        return this.frightenedLength;
    }

    /**
     * @return values of mode length in list
     */
    public ArrayList<Integer> getModeLengths(){
        return this.modeLengths;
    }
}