package innopolis.nikbird.org.iceandfireuniverse.models;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;

/**
 * Created by nikbird on 21.07.17.
 */

public class Character implements ICharacter {

    //
    // STATIC
    //
    private static int sId = 0;
    private final static int getNextId() {
        return sId++;
    }

    private static String[] readStringArray(JsonReader reader) throws IOException {
        List<String> strings = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext())
            strings.add(reader.nextString());
        reader.endArray();
        return strings.toArray(new String[strings.size()]);
    }

    //
    // NON STATIC
    //
    private int mId = getNextId();
    private Map<String, Object> mProperties = new HashMap<>();

    //
    // CONSTUCTORS
    //
    public Character(JsonReader reader) throws IOException {
        String name;
        reader.beginObject();
        while (reader.hasNext()) {
            name = reader.nextName();
            switch (name) {
                case "uri":
                case "name":
                case "gender":
                case "culture":
                case "born":
                case "died":
                    mProperties.put(name, reader.nextString());
                    break;
                case "titles":
                case "aliases":
                case "allegiances":
                    mProperties.put(name, readStringArray(reader));
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
    }

    //
    // OVERRIDE
    //
    @Override public int hashCode() {
        return mId;
    }

    @Override public boolean equals(Object obj) {
        if (obj != null && obj instanceof Character) {
            Character c = (Character) obj;
            if (mId == c.mId)
                return true;
        }
        return false;
    }

    @Override public String getUrl() { return getStringProperty("url"); }
    @Override public String getName() { return getStringProperty("name"); }
    @Override public String getGender() { return getStringProperty("gender"); }
    @Override public String getCulture() { return getStringProperty("culture"); }
    @Override public String getBorn() { return getStringProperty("born"); }
    @Override public String getDied() { return getStringProperty("died"); }
    @Override public String[] getTitles() { return getStringArrayProperty("titles"); }
    @Override public String[] getAliases() { return getStringArrayProperty("aliases"); }
    @Override public String getFather() { return getStringProperty("father"); }
    @Override public String getMother() { return getStringProperty("mother"); }
    @Override public String getSpouse() { return getStringProperty("spouse"); }
    @Override public String[] getAllegiances() { return getStringArrayProperty("allegiances"); }
    @Override public String[] getBooks() { return getStringArrayProperty("books"); }
    @Override public String[] getPovBooks() { return getStringArrayProperty("povBooks"); }
    @Override public String[] getTvSeries() { return getStringArrayProperty("tvSeries"); }
    @Override public String[] getPlayedBy() { return getStringArrayProperty("playedBy"); }


    //
    // PRIVATE
    //
    private String getStringProperty(String s) {
        return (String) mProperties.get(s);
    }

    private String[] getStringArrayProperty(String s) {
        return (String[]) mProperties.get(s);
    }

    private void setProperty(String s, Object value) {
        mProperties.put(s, value);
    }
}
