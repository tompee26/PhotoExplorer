package com.tompee.utilities.photoexplorer.model;

import java.util.List;

public class Photo {
    private int mPageCount;
    private int mPage;
    private int mTotal;
    private List<String> mIdList;

    public Photo(int pageCount, int page, int total, List<String> idList) {
        mPageCount = pageCount;
        mPage = page;
        mTotal = total;
        mIdList = idList;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public int getPage() {
        return mPage;
    }

    public int getTotal() {
        return mTotal;
    }

    public List<String> getIdList() {
        return mIdList;
    }
}
