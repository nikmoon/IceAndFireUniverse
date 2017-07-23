package innopolis.nikbird.org.iceandfireuniverse.models;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;

import static innopolis.nikbird.org.iceandfireuniverse.ActivityMain.LOG_TAG;

/**
 * Created by nikbird on 21.07.17.
 */

public class LooperThread extends Thread {

    public interface IReaderThreadListener {
        void onHandlerReady(LooperHandler handler);
        void onDataReady(int requestId, List<ICharacter> data);
        void onRequestFail(int requestId, String desc);
    }

    private LooperHandler mHandler;
    private Handler mMainHandler;
    private Context mContext;
    private IReaderThreadListener mListener;


    public LooperThread(Context context, IReaderThreadListener listener) {
        mContext = context;
        mListener = listener;
        mMainHandler = new Handler();
    }

    public void setListener(IReaderThreadListener listener) {
        mListener = listener;
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new LooperHandler();
        notifyHandlerReady();
        Looper.loop();
    }

    private void notifyHandlerReady() {
        if (mListener != null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onHandlerReady(mHandler);
                }
            });
        }
    }

    private void notifyDataReady(final int requestId, final List<ICharacter> data) {
        if (mListener != null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() { mListener.onDataReady(requestId, data); }
            });
        }
    }

    private void notifyRequestFail(final int requestId, final String desc) {
        if (mListener != null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {mListener.onRequestFail(requestId, desc); }
            });
        }
    }

    public class LooperHandler extends Handler {
        public void postRequest(int requestId, String urlString) {
            post(new Request(requestId, urlString));
        }

        public LooperThread getThread() {
            return (LooperThread) getLooper().getThread();
        }
    }


    private class BadResponseException extends Exception {}

    private class Request implements Runnable {

        private int mId;
        private String mUrlString;
        private List<ICharacter> mCharacters;

        public Request(int requestId, String requestUrl) {
            mId = requestId;
            mUrlString = requestUrl;
        }

        @Override
        public void run() {
            URL url;
            HttpsURLConnection conn;
            int responseCode = 0;
            JsonReader reader;

            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.i(LOG_TAG, "New loading begin: " + mUrlString);
            try {
                url = new URL(mUrlString);
                conn = (HttpsURLConnection) url.openConnection();
                if ( (responseCode = conn.getResponseCode()) != HttpsURLConnection.HTTP_OK)
                    throw new BadResponseException();
                reader = new JsonReader(new InputStreamReader(conn.getInputStream()));
                mCharacters = new ArrayList<>();
                reader.beginArray();
                while (reader.hasNext()) {
                    mCharacters.add(new Character(reader));
                }
                reader.endArray();
                notifyDataReady(mId, mCharacters);
            } catch (MalformedURLException e) {
                notifyRequestFail(mId, "Bad url " + mUrlString);
            } catch (IOException e) {
                notifyRequestFail(mId, "IOException: " + e.getMessage());
            } catch (ClassCastException e) {
                notifyRequestFail(mId, "Casting URLConnection to HttpsURLConnection failed");
            } catch (BadResponseException e) {
                notifyRequestFail(mId, "Response code: " + responseCode);
            } catch (Character.MissedURLException e) {
                notifyRequestFail(mId, "Missed \"url\" key");
            } catch (Character.MissedIdException e) {
                notifyRequestFail(mId, "Missed id in url");
            }
        }
    }
}
