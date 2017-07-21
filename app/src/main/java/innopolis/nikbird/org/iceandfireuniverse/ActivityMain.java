package innopolis.nikbird.org.iceandfireuniverse;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;
import innopolis.nikbird.org.iceandfireuniverse.models.LoaderCharacterList;

public class ActivityMain extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<? extends ICharacter>>
        , LoaderCharacterList.IProgressListener {

    private static String sBaseUrlString = "https://www.anapioficeandfire.com/api/characters";
    public static String LOG_TAG = "MAINACTIVITY_LOG_TAG";


    private URL mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mUrl = new URL(sBaseUrlString);
            getSupportLoaderManager().initLoader(0, null, this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(LOG_TAG, e.getMessage());
        }
    }

    @Override
    public Loader<List<? extends ICharacter>> onCreateLoader(int id, Bundle args) {
        LoaderCharacterList loader = new LoaderCharacterList(this, this, mUrl);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<? extends ICharacter>> loader, List<? extends ICharacter> data) {
        
    }

    @Override
    public void onLoaderReset(Loader<List<? extends ICharacter>> loader) {

    }

    @Override
    public void onProgress(int newCharactersCount) {

    }
}
