package innopolis.nikbird.org.iceandfireuniverse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;
import innopolis.nikbird.org.iceandfireuniverse.interfaces.IDataModel;
import innopolis.nikbird.org.iceandfireuniverse.models.DataModel;

public class ActivityMain
        extends AppCompatActivity
        implements CharacterListAdapter.IViewModel, IDataModel.IListener {

    public static final String EXTRA_CHARACTER_INFO = "innopolis.nikbird.org.char_info";
    public final static String LOG_TAG = "MAIN_ACTIVITY_LOG_TAG";

    /**
     *  Оказалось, что 50 - максимальное значение записей на странице, возвращаемое ресурсом
     */
    public final static int DEFAULT_PAGE_SIZE = 50;

    private int mNextPage = 1;
    private int mPageSize;
    private List<ICharacter> mCharacters;
    private RecyclerView mCharacterListView;
    private IDataModel mDataModel;
    private boolean isPageLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCharacterListView = (RecyclerView) findViewById(R.id.rcvList);
        mCharacterListView.setAdapter(new CharacterListAdapter(this));

        ActivityMain prevActivity = (ActivityMain) getLastCustomNonConfigurationInstance();
        if (prevActivity != null) {
            mDataModel = prevActivity.mDataModel;
            mDataModel.setListener(this);
            mPageSize = prevActivity.mPageSize;
            mNextPage = prevActivity.mNextPage;
            mCharacters = prevActivity.mCharacters;
            isPageLoading = prevActivity.isPageLoading;
        } else {
            mPageSize = DEFAULT_PAGE_SIZE;
            mCharacters = new ArrayList<>(mPageSize);
            mDataModel = new DataModel(this);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return this;
    }

    @Override
    public void onModelReady() {
        requestNextPage();
    }

    @Override
    public void onDataFail(int requestId, String errorMsg) {
        isPageLoading = false;
    }

    @Override
    public void onDataReady(int requestId, List<ICharacter> data) {
        int rangeStart = mCharacters.size();
        mCharacters.addAll(data);
        mCharacterListView.getAdapter().notifyItemRangeChanged(rangeStart, data.size());
        mNextPage++;
        isPageLoading = false;
    }

    @Override
    public void onSelectCharacter(int position) {
        Intent intent = new Intent(this, ActivityDetail.class);
        intent.putExtra(EXTRA_CHARACTER_INFO, mCharacters.get(position));
        startActivity(intent);
    }

    @Override
    public ICharacter getCharacter(int position) {
        if ((mCharacters.size() - position) < 20)
            requestNextPage();
        return mCharacters.get(position);
    }

    @Override
    public int characterCount() {
        return mCharacters.size();
    }

    private void requestNextPage() {
        if (!isPageLoading) {
            mDataModel.requestData(0, new IDataModel.PageRequest(mNextPage, mPageSize));
            isPageLoading = true;
        }
    }
}
