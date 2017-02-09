package com.tompee.utilities.photoexplorer.model;

import java.util.ArrayList;
import java.util.List;

public class PhotoGroup {
    private final int mPageCount;
    private final int mPage;
    private final int mTotal;
    private final List<String> mIdList;

    public PhotoGroup(int pageCount, int page, int total) {
        mPageCount = pageCount;
        mPage = page;
        mTotal = total;
        mIdList = new ArrayList<>();
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

    public void addId(String id) {
        mIdList.add(id);
    }

    public List<String> getIdList() {
        return mIdList;
    }
}
