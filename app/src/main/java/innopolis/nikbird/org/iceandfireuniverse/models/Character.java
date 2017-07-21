package innopolis.nikbird.org.iceandfireuniverse.models;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;

/**
 * Created by nikbird on 21.07.17.
 */

public class Character implements ICharacter {

    private static int sId = 0;
    private static int getNextId() {
        return sId++;
    }

    private String mUrlString;
    private String mBorn;
    private String mDied;
    private String[] mTitles;
    private String[] mAliases;
    private String mFather;
    private String mMother;
    private String[] mAllegiances;
    private String[] mBooks;
    private String[] mTvSeries;
    private String[] mPlayedBy;

    private int mId;
    private String mName;
    private String mGender;
    private String mCulture;

    private Character() {
        mId = getNextId();
    }

    @Override
    public int hashCode() {
        return mId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Character) {
            Character c = (Character) obj;
            if (mId == c.mId)
                return true;
        }
        return false;
    }

    @Override
    public String getUrl() {
        return mUrlString;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getGender() {
        return mGender;
    }

    @Override
    public String getCulture() {
        return mCulture;
    }

    @Override
    public String getBorn() {
        return mBorn;
    }

    @Override
    public String getDied() {
        return mDied;
    }

    @Override
    public String[] getTitles() {
        return mTitles;
    }

    @Override
    public String[] getAliases() {
        return mAliases;
    }

    @Override
    public String getFather() {
        return mFather;
    }

    @Override
    public String getMother() {
        return mMother;
    }

    @Override
    public String[] getAllegiances() {
        return mAllegiances;
    }

    @Override
    public String[] getBooks() {
        return mBooks;
    }

    @Override
    public String[] getTvSeries() {
        return mTvSeries;
    }

    @Override
    public String[] getPlayedBy() {
        return mPlayedBy;
    }
}
