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

    public Character() {
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

    public void setUrlString(String mUrlString) {
        this.mUrlString = mUrlString;
    }

    public void setBorn(String mBorn) {
        this.mBorn = mBorn;
    }

    public void setDied(String mDied) {
        this.mDied = mDied;
    }

    public void setTitles(String[] mTitles) {
        this.mTitles = mTitles;
    }

    public void setAliases(String[] mAliases) {
        this.mAliases = mAliases;
    }

    public void setFather(String mFather) {
        this.mFather = mFather;
    }

    public void setMother(String mMother) {
        this.mMother = mMother;
    }

    public void setAllegiances(String[] mAllegiances) {
        this.mAllegiances = mAllegiances;
    }

    public void setBooks(String[] mBooks) {
        this.mBooks = mBooks;
    }

    public void setTvSeries(String[] mTvSeries) {
        this.mTvSeries = mTvSeries;
    }

    public void setPlayedBy(String[] mPlayedBy) {
        this.mPlayedBy = mPlayedBy;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setGender(String mGender) {
        this.mGender = mGender;
    }

    public void setCulture(String mCulture) {
        this.mCulture = mCulture;
    }
}
