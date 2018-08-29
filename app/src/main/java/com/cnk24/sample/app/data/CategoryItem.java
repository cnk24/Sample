package com.cnk24.sample.app.data;

import com.cnk24.mediaalbum.AlbumFile;

public class CategoryItem extends AdapterItem {

    private String mCategoryName;

    public CategoryItem(String name, AlbumFile albumFile) {
        super(albumFile);
        mCategoryName = name;
    }

    @Override
    public int getType() {
        return TYPE_CATEGORY;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String name) {
        mCategoryName = name;
    }

}
