package innopolis.nikbird.org.iceandfireuniverse.models;

import android.os.Parcel;
import android.os.Parcelable;
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
            Class propertyType = getPropertyType(name);
            if (propertyType == String.class)
                mProperties.put(name, reader.nextString());
            else if (propertyType == String[].class)
                mProperties.put(name, readStringArray(reader));
            else
                reader.skipValue();
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

    // ICharacter
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

    // Parcelable
    public static final Parcelable.Creator<Character> CREATOR
            = new Parcelable.Creator<Character>() {

        @Override public Character createFromParcel(Parcel parcel) { return new Character(parcel); }
        @Override public Character[] newArray(int i) { return new Character[i]; }
    };

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        for(Map.Entry<String, Object> entry: mProperties.entrySet()) {
            parcel.writeString(entry.getKey());
            Object value = entry.getValue();
            if (value instanceof String)
                parcel.writeString((String) value);
            else if (value instanceof String[]) {
                parcel.writeInt(((String[]) value).length);
                parcel.writeStringArray((String[]) value);
            }
        }
        parcel.writeString("CHAR_END");
    }

    private Character(Parcel parcel) {
        String key;
        String[] values;
        Class valueType;
        int len;
        mId = parcel.readInt();
        while (true) {
            key = parcel.readString();
            if ("CHAR_END".equals(key))
                break;
            valueType = getPropertyType(key);
            if (valueType == String.class)
                mProperties.put(key, parcel.readString());
            else if (valueType == String[].class) {
                len = parcel.readInt();
                parcel.readStringArray((values = new String[len]));
                mProperties.put(key, values);
            }
        }
    }

    //
    // PRIVATE
    //
    private String getStringProperty(String s) {
        return (String) mProperties.get(s);
    }

    private String[] getStringArrayProperty(String s) {
        return (String[]) mProperties.get(s);
    }

    private Class getPropertyType(String property) {
        Class propertyType;
        switch (property) {
            case "url":
            case "name":
            case "gender":
            case "culture":
            case "born":
            case "died":
                propertyType = String.class;
                break;
            case "titles":
            case "aliases":
            case "allegiances":
                propertyType = String[].class;
                break;
            default:
                propertyType = null;
        }
        return propertyType;
    }

    private void setProperty(String s, Object value) {
        mProperties.put(s, value);
    }
}
