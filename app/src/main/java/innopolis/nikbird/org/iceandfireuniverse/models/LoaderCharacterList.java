package innopolis.nikbird.org.iceandfireuniverse.models;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import innopolis.nikbird.org.iceandfireuniverse.ActivityMain;
import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;

/**
 * Created by nikbird on 21.07.17.
 */

public class LoaderCharacterList extends AsyncTaskLoader<List<ICharacter>> {

    public interface IProgressListener {
        void onProgress(List<ICharacter> newCharacters);
    }

    private URL mBaseUrl;
    private List<ICharacter> mCharacters;
    private Handler mHandler;
    private IProgressListener mListener;

    public LoaderCharacterList(Context context, IProgressListener listener, URL url) {
        super(context);
        mHandler = new Handler(context.getMainLooper());
        mListener = listener;
        mBaseUrl = url;
    }

    public List<ICharacter> getCharacterList() {
        return mCharacters;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mCharacters == null) {
            forceLoad();
        }
        else {
            deliverResult(mCharacters);
        }
    }

    public class BadHttpResponseCodeException extends Exception {}

    @Override
    public List<ICharacter> loadInBackground() {
        mCharacters = readCharacters();
        return mCharacters;
    }

    private List<ICharacter> readCharacters() {
        ICharacter character;
        List<ICharacter> characters = new ArrayList<>(50);

        HttpsURLConnection conn;
        int responseCode = 0;
        BufferedReader reader;
        String jsonString;
        JSONArray jsonArray;

        try {
            conn = (HttpsURLConnection) mBaseUrl.openConnection();
            responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK)
                throw new BadHttpResponseCodeException();
            StringBuilder stringBuilder = new StringBuilder(500);
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while( (line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            jsonString = stringBuilder.toString();
            jsonArray = new JSONArray(jsonString);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonCharacter = jsonArray.getJSONObject(i);
                character = parseCharacter(jsonCharacter);
                characters.add(character);
            }
            if (mListener != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onProgress(null);
                    }
                });
            }
            for(int i = jsonArray.length(); true; i++) {

            }
        } catch (BadHttpResponseCodeException e) {
            e.printStackTrace();
            Log.i(ActivityMain.LOG_TAG, "Http sesponse code = " + responseCode);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(ActivityMain.LOG_TAG, "Bad URL: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(ActivityMain.LOG_TAG, "IOException: " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(ActivityMain.LOG_TAG, "Json exception: " + e.getMessage());
        }
        return characters;
    }

    private ICharacter parseCharacter(JSONObject jsonObject) throws JSONException {
        Character character = new Character();
        character.setUrlString(jsonObject.getString("url"));
        character.setName(jsonObject.getString("name"));
        character.setGender(jsonObject.getString("gender"));
        character.setCulture(jsonObject.getString("culture"));
        character.setBorn(jsonObject.getString("born"));
        character.setDied(jsonObject.getString("died"));
        character.setTitles(getArray(jsonObject.getJSONArray("titles")));
        character.setAliases(getArray(jsonObject.getJSONArray("aliases")));
        character.setFather(jsonObject.getString("father"));
        character.setMother(jsonObject.getString("mother"));
        character.setAllegiances(getArray(jsonObject.getJSONArray("allegiances")));
        character.setBooks(getArray(jsonObject.getJSONArray("books")));
        character.setTvSeries(getArray(jsonObject.getJSONArray("tvSeries")));
        character.setPlayedBy(getArray(jsonObject.getJSONArray("playedBy")));
        return character;
    }

    private ICharacter readCharacter() {
        Character character = new Character();
        return character;
    }

    private String[] getArray(JSONArray jsonArray) throws JSONException {
        String[] array = null;
        if (jsonArray != null && jsonArray.length() != 0) {
            array = new String[jsonArray.length()];
            for(int i = 0; i < array.length; i++) {
                array[i] = jsonArray.getString(i);
            }
        }
        return array;
    }
}
