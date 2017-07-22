package innopolis.nikbird.org.iceandfireuniverse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;
import innopolis.nikbird.org.iceandfireuniverse.models.LooperThread;

public class ActivityMain
        extends AppCompatActivity
        implements LooperThread.IReaderThreadListener, CharacterListAdapter.IPageLoader {

    public final static String LOG_TAG = "MAIN_ACTIVITY_LOG_TAG";
    public final static String DEFAULT_URL_STRING_TEMPLATE = "https://www.anapioficeandfire.com/api/characters?page=%s&pageSize=%s";
    public final static int DEFAULT_PAGE_SIZE = 30;

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

        mUrlStringTemplate = DEFAULT_URL_STRING_TEMPLATE;
        mPageSize = DEFAULT_PAGE_SIZE;

        mCharacterListView = (RecyclerView) findViewById(R.id.rcvList);
        mCharacterListView.setAdapter(new CharacterListAdapter(this));
        new LooperThread(getApplicationContext(), this).start();
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

    private void postRequestNextPage() {
        mLooperHandler.postRequest(0, String.format(mUrlStringTemplate, mNextPage, mPageSize));
        mLoadingInProgress = true;
    }
}
