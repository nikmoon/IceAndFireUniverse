package innopolis.nikbird.org.iceandfireuniverse.interfaces;

import java.util.List;

import innopolis.nikbird.org.iceandfireuniverse.models.DataModel;


/**
 * Created by nikbird on 23.07.17.
 */

public interface IDataModel {

    interface IListener {
        void onModelReady();
        void onDataFail(int requestId, String errorMsg);
        void onDataReady(int requestId, List<ICharacter> data);
    }

    class PageRequest {
        public int mPageNum;
        public int mPageSize;

        public PageRequest(int pageNum, int pageSize) {
            mPageNum = pageNum;
            mPageSize = pageSize;
        }
    }

    class RangeRequest {
        public int mFirst;
        public int mLast;

        public RangeRequest(int firstRec, int lastRec) {
            mFirst = firstRec;
            mLast = lastRec;
        }
    }

    void setListener(IListener listener);
    void requestData(int requestId, int index);
    void requestData(int requestId, PageRequest requestParams);
    void requestData(int requestId, RangeRequest requestParams);
}
