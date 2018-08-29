package com.cnk24.sample.app.data;

import com.cnk24.mediaalbum.AlbumFile;

public class AlbumItem extends AdapterItem {

    public AlbumItem(AlbumFile albumFile) {
        super(albumFile);
    }

    @Override
    public int getType() {
        return TYPE_MEDIA;
    }

}
