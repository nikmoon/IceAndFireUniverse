package innopolis.nikbird.org.iceandfireuniverse.models;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

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
import innopolis.nikbird.org.iceandfireuniverse.ManagerCharacters;
import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;

/**
 * Created by nikbird on 21.07.17.
 */

public class LoaderCharacterList extends AsyncTaskLoader<List<? extends ICharacter>> {

    public interface IProgressListener {
        void onProgress(int newCharactersCount);
    }

    private URL mBaseUrl;
    private List<? extends ICharacter> mCharacters;
    private Handler mHandler;
    private IProgressListener mListener;

    public LoaderCharacterList(Context context, IProgressListener listener, URL url) {
        super(context);
        mHandler = new Handler(context.getMainLooper());
        mListener = listener;
        mBaseUrl = url;
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
    public List<? extends ICharacter> loadInBackground() {
        mCharacters = null;

        HttpsURLConnection conn = null;
        int responseCode = 0;
        BufferedReader reader = null;
        String jsonString = null;

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
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(ActivityMain.LOG_TAG, e.getMessage());
        } catch (BadHttpResponseCodeException e) {
            e.printStackTrace();
            Log.i(ActivityMain.LOG_TAG, "Http sesponse code = " + responseCode);
        }
        return mCharacters;
    }
}
