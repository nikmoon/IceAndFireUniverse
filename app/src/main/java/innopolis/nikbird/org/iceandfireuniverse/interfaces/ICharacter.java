package innopolis.nikbird.org.iceandfireuniverse.interfaces;

import android.os.Parcelable;

import java.util.Objects;

/**
 * Created by nikbird on 21.07.17.
 */

public interface ICharacter extends Parcelable {

    String getUrl();
    String getName();
    String getGender();
    String getCulture();
    String getBorn();
    String getDied();
    String[] getTitles();
    String[] getAliases();
    String getFather();
    String getMother();
    String getSpouse();
    String[] getAllegiances();
    String[] getBooks();
    String[] getPovBooks();
    String[] getTvSeries();
    String[] getPlayedBy();
}
