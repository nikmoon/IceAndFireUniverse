package innopolis.nikbird.org.iceandfireuniverse;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;
import innopolis.nikbird.org.iceandfireuniverse.models.LoaderCharacterList;

public class ActivityMain extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<ICharacter>>
        , LoaderCharacterList.IProgressListener {

    private static String sBaseUrlString = "https://www.anapioficeandfire.com/api/characters";
    public static String LOG_TAG = "MAINACTIVITY_LOG_TAG";


    private URL mUrl;
    private RecyclerView mCharactersView;
    private CharacterListAdapter mAdapter;
    private List<ICharacter> mCharacters;
    private LoaderCharacterList mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCharactersView = (RecyclerView) findViewById(R.id.rcvList);
        try {
            mUrl = new URL(sBaseUrlString);
            getSupportLoaderManager().initLoader(0, null, this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(LOG_TAG, e.getMessage());
        }
    }

    @Override
    public Loader<List<ICharacter>> onCreateLoader(int id, Bundle args) {
        mLoader = new LoaderCharacterList(this, this, mUrl);
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<ICharacter>> loader, List<ICharacter> data) {
        Toast.makeText(this, "Загрузка завершена", Toast.LENGTH_SHORT).show();
        if (mCharacters == null) {
            mCharacters = data;
            mAdapter = new CharacterListAdapter(mCharacters);
            mCharactersView.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<ICharacter>> loader) {

    }

    @Override
    public void onProgress(List<ICharacter> newCharacters) {
        if (mCharacters == null) {
            mCharacters = mLoader.getCharacterList();
            mAdapter = new CharacterListAdapter(mCharacters);
            mCharactersView.setAdapter(mAdapter);
        }
        mAdapter.notifyItemInserted(mCharacters.size() - 1);
    }
}
