package innopolis.nikbird.org.iceandfireuniverse;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import innopolis.nikbird.org.iceandfireuniverse.models.Character;

public class ActivityDetail extends AppCompatActivity {

    private ViewGroup mContainer;
    private TextView mUrlView;
    private TextView mNameView;
    private TextView mGenderView;
    private TextView mCultureView;
    private TextView mBornView;
    private TextView mDiedView;
    private LinearLayout mTitlesView;
    private LinearLayout mAliasesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Character character = getIntent().getParcelableExtra(ActivityMain.EXTRA_CHARACTER_INFO);
        mContainer = (ViewGroup) findViewById(R.id.layoutCharInfo);

        mUrlView = mContainer.findViewById(R.id.tvUrl);
        mUrlView.setText(checkEmpty(character.getUrl()));

        mNameView = mContainer.findViewById(R.id.tvName);
        mNameView.setText(checkEmpty(character.getName()));

        mGenderView = mContainer.findViewById(R.id.tvGender);
        mGenderView.setText(checkEmpty(character.getGender()));

        mCultureView = mContainer.findViewById(R.id.tvCulture);
        mCultureView.setText(checkEmpty(character.getCulture()));

        mBornView = mContainer.findViewById(R.id.tvBorn);
        mBornView.setText(checkEmpty(character.getBorn()));

        mDiedView = mContainer.findViewById(R.id.tvDied);
        mDiedView.setText(checkEmpty(character.getDied()));

        mTitlesView = mContainer.findViewById(R.id.layoutTitles);
        setTextValues(mTitlesView, character.getTitles());

        mAliasesView = mContainer.findViewById(R.id.layoutAliases);
        setTextValues(mAliasesView, character.getAliases());
    }

    private String checkEmpty(String s) {
        return s == null || "".equals(s) ? "<empty>" : s;
    }

    private void setTextValues(LinearLayout layout, String[] values) {
        TextView textView;
        if (values != null)
            for(String s: values) {
                s = checkEmpty(s);
                textView = new TextView(this);
                textView.setId(View.generateViewId());
                textView.setText(s);
                if (Build.VERSION.SDK_INT < 23)
                    textView.setTextAppearance(this, R.style.TextAppearance_AppCompat_Medium);
                else
                    textView.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                layout.addView(textView);
            }
    }
}
