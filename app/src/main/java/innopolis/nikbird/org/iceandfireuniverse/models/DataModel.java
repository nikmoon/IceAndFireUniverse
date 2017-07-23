package innopolis.nikbird.org.iceandfireuniverse.models;

import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;
import innopolis.nikbird.org.iceandfireuniverse.interfaces.IDataModel;

import static innopolis.nikbird.org.iceandfireuniverse.ActivityMain.LOG_TAG;

/**
 * Created by nikbird on 23.07.17.
 */

public class DataModel extends Thread implements IDataModel {

    public static final String BASE_URL = "https://www.anapioficeandfire.com/api/characters";
    public static final String PAGE_QUERY_TEMPLATE = BASE_URL + "?page=%s&pageSize=%s";
    public static final String CHARACTER_QUERY_TEMPLATE = BASE_URL + "/%s";

    public static class BadResponseException extends Exception {}
    public static class NotJsonResponseException extends Exception {}

    private Handler mHandler;
    private Handler mMainHandler;

    /**
     *  Использование дополнительной обертки на ссылкой должно решить проблему
     *  потери уведомления в ситуации, когда уведомление добавлено в очередь перед
     *  тем, как активити пересоздалась. Если сразу после пересоздания обновить это поле,
     *  уведомление будет доставлено новому слушателю, как и нужно.
     */
    private IListener[] mListener;

    public DataModel(IListener listener) {
        mListener = new IListener[1];
        setListener(listener);
        mMainHandler = new Handler(Looper.getMainLooper());
        this.start();
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper());
        notifyModelReady();
        Looper.loop();
    }

    @Override
    public void setListener(IListener listener) {
        if (listener == null)
            throw new NullPointerException();
        mListener[0] = listener;
    }

    @Override
    public void requestData(int requestId, int index) {
        requestData(requestId, String.format(CHARACTER_QUERY_TEMPLATE, index));
    }

    @Override
    public void requestData(int requestId, PageRequest pageRequest) {
        requestData(requestId, String.format(PAGE_QUERY_TEMPLATE, pageRequest.mPageNum, pageRequest.mPageSize));
    }

    @Override
    public void requestData(int requestId, RangeRequest requestParams) {
        throw new UnsupportedOperationException();
    }

    private void requestData(int requestId, String requestUrl) {
        mHandler.post(new Request(requestId, requestUrl));
    }

    private class Request implements Runnable {

        private int mRequestId;
        private String mUrlString;

        public Request(int requestId, String requestUrl) {
            mRequestId = requestId;
            mUrlString = requestUrl;
        }

        @Override
        public void run() {
            URL url;
            HttpURLConnection conn = null;
            int responseCode = 0;

            Log.i(LOG_TAG, "New loading begin: " + mUrlString);
            try {
                url = new URL(mUrlString);
                conn = (HttpURLConnection) url.openConnection();
                if ((responseCode = conn.getResponseCode()) != HttpURLConnection.HTTP_OK)
                    throw new BadResponseException();
                if (!conn.getContentType().toLowerCase().contains("application/json"))
                    throw new NotJsonResponseException();
                try (
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                ) {
                    String line;
                    StringBuilder responseBody = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        responseBody.append(line).append('\n');
                    }
                    deliverData(mRequestId, responseBody.toString());
                }
            } catch (MalformedURLException e) {
                notifyDataFail(mRequestId, "Bad url " + mUrlString);
            } catch (IOException e) {
                notifyDataFail(mRequestId, "IOException: " + e.getMessage());
            } catch (BadResponseException e) {
                notifyDataFail(mRequestId, "Response code: " + responseCode);
            } catch (NotJsonResponseException e) {
                notifyDataFail(mRequestId, "Response type: " + conn.getContentType());
            } catch (Character.MissedURLException e) {
                notifyDataFail(mRequestId, "Missed \"url\" in response");
            } catch (Character.MissedIdException e) {
                notifyDataFail(mRequestId, "Missed id in url");
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
        }
    }

    private void deliverData(final int requestId, String response) throws IOException, Character.MissedURLException, Character.MissedIdException {
        final List<ICharacter> data = new ArrayList<>();
        response = response.toString().trim();
        JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(response.getBytes())));
        if (response.startsWith("[")) {
            reader.beginArray();
            while (reader.hasNext()) {
                data.add(new Character(reader));
            }
            reader.endArray();
        } else if (response.startsWith("{")) {
            data.add(new Character(reader));
        }

        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener[0].onDataReady(requestId, data);
            }
        });
    }

    private void notifyModelReady() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener[0].onModelReady();
            }
        });
    }

    private void notifyDataFail(final int requestId, final String msg) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener[0].onDataFail(requestId, msg);
            }
        });
    }
}
