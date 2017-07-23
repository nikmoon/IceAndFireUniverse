package innopolis.nikbird.org.iceandfireuniverse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;
import innopolis.nikbird.org.iceandfireuniverse.models.LooperThread;

public class ActivityMain
        extends AppCompatActivity
        implements LooperThread.IReaderThreadListener, CharacterListAdapter.IViewModel {

    public static final String EXTRA_CHARACTER_INFO = "innopolis.nikbird.org.char_info";
    public final static String LOG_TAG = "MAIN_ACTIVITY_LOG_TAG";
    public final static String DEFAULT_URL_STRING_TEMPLATE = "https://www.anapioficeandfire.com/api/characters?page=%s&pageSize=%s";
    public final static int DEFAULT_PAGE_SIZE = 50;

    private String mUrlStringTemplate;
    private int mNextPage = 1;
    private int mPageSize;
    private RecyclerView mCharacterListView;
    private LooperThread.LooperHandler mLooperHandler;
    private boolean mLoadingInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView.Adapter adapter;
        ActivityMain prevActivity = (ActivityMain) getLastCustomNonConfigurationInstance();
        if (prevActivity != null) {
            mUrlStringTemplate = prevActivity.mUrlStringTemplate;
            mPageSize = prevActivity.mPageSize;
            mNextPage = prevActivity.mNextPage;
            mLoadingInProgress = prevActivity.mLoadingInProgress;
            mLooperHandler = prevActivity.mLooperHandler;
            mLooperHandler.getThread().setListener(this);
            CharacterListAdapter characterListAdapter =
                    (CharacterListAdapter) prevActivity.mCharacterListView.getAdapter();
            adapter = new CharacterListAdapter(characterListAdapter.getCharacters(), this);
        } else {
            mUrlStringTemplate = DEFAULT_URL_STRING_TEMPLATE;
            mPageSize = DEFAULT_PAGE_SIZE;
            new LooperThread(getApplicationContext(), this).start();
            adapter = new CharacterListAdapter(this);
        }
        mCharacterListView = (RecyclerView) findViewById(R.id.rcvList);
        mCharacterListView.setAdapter(adapter);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return this;
    }

    @Override
    public void onHandlerReady(LooperThread.LooperHandler handler) {
        mLooperHandler = handler;
        postRequestNextPage();
    }

    @Override
    public void onDataReady(int requestId, List<ICharacter> data) {
        mNextPage++;
        CharacterListAdapter adapter = (CharacterListAdapter) mCharacterListView.getAdapter();
        adapter.addCharacters(data);
        mLoadingInProgress = false;
    }

    @Override
    public void onRequestFail(int requestId, String desc) {
        Log.i(LOG_TAG, desc);
        mLoadingInProgress = false;
    }

    @Override
    public void onNewPageNeeded() {
        if (!mLoadingInProgress)
            postRequestNextPage();
    }

    @Override
    public void onDetailStart(ICharacter character) {
        Intent intent = new Intent(this, ActivityDetail.class);
        intent.putExtra(EXTRA_CHARACTER_INFO, character);
        startActivity(intent);
    }

    private void postRequestNextPage() {
        mLooperHandler.postRequest(0, String.format(mUrlStringTemplate, mNextPage, mPageSize));
        mLoadingInProgress = true;
    }
}
