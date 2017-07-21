package innopolis.nikbird.org.iceandfireuniverse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;

/**
 * Created by nikbird on 21.07.17.
 */

public class CharacterListAdapter extends RecyclerView.Adapter {

    private List<ICharacter> mCharacters;

    private class CharacterItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameView;
        private ListView mAliasesView;

        public CharacterItemHolder(View itemView) {
            super(itemView);

            mNameView = itemView.findViewById(R.id.tvCharName);
            mAliasesView = itemView.findViewById(R.id.lvAliases);
        }

        public void bind(int position) {
            String name = mCharacters.get(position).getName();
            if (name == null || "".equals(name))
                name = "noname";
            mNameView.setText(name);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public CharacterListAdapter(List<ICharacter> characters) {
        mCharacters = characters;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflated = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_row_layout, parent, false);

        return new CharacterItemHolder(inflated);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CharacterItemHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return mCharacters.size();
    }
}
