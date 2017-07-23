package innopolis.nikbird.org.iceandfireuniverse;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;

/**
 * Created by nikbird on 21.07.17.
 */

public class CharacterListAdapter extends RecyclerView.Adapter {

    public interface IViewModel {
        void onSelectCharacter(int position);
        ICharacter getCharacter(int position);
        int characterCount();
    }

    private IViewModel mViewModel;

    private class CharacterItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameView;
        private LinearLayout mAliasesView;

        public CharacterItemHolder(View itemView) {
            super(itemView);

            mNameView = itemView.findViewById(R.id.tvCharName);
            mAliasesView = itemView.findViewById(R.id.relAliases);
            itemView.setOnClickListener(this);
        }

        public void bind(ICharacter character) {
            setName(character.getName());
            setAliases(character.getAliases());
        }

        private void setAliases(String[] aliases) {
            TextView textView;
            mAliasesView.removeAllViewsInLayout();
            for(String alias: aliases) {
                if (alias != null && !"".equals(alias)) {
                    textView = new TextView(mAliasesView.getContext());
                    textView.setId(View.generateViewId());
                    textView.setText(alias);
                    if (Build.VERSION.SDK_INT < 23)
                        textView.setTextAppearance(mAliasesView.getContext(), R.style.TextAppearance_AppCompat_Medium);
                    else
                        textView.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                    mAliasesView.addView(textView);
                }
            }
        }

        private void setName(String name) {
            name = (name == null || "".equals(name)) ? "<noname>" : name;
            mNameView.setText(name);
        }

        @Override
        public void onClick(View view) {
            mViewModel.onSelectCharacter(getAdapterPosition());
        }
    }

    public CharacterListAdapter(IViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflated = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_row_layout, parent, false);
        return new CharacterItemHolder(inflated);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ICharacter character = mViewModel.getCharacter(position);
        ((CharacterItemHolder) holder).bind(character);

    }

    @Override
    public int getItemCount() {
        return mViewModel.characterCount();
    }
}
