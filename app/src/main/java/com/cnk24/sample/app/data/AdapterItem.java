package com.cnk24.sample.app.data;

import com.cnk24.mediaalbum.AlbumFile;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AdapterItem {

    public static final int TYPE_MEDIA = 1;
    public static final int TYPE_CATEGORY = 2;
    public static final int TYPE_DATE = 3;
    public static final int TYPE_IMAGE = 4;
    public static final int TYPE_VIDEO = 5;

    private AlbumFile mAlbumFile;

    public AdapterItem(AlbumFile albumFile) {
        mAlbumFile = albumFile;
    }

    public AlbumFile getAlbumFile() {
        return mAlbumFile;
    }

    public void setAlbumFile(AlbumFile albumFile) {
        mAlbumFile = albumFile;
    }

    // 1393217807 단위로 가져오는데 ms는 1000단위 더 높음
    public long convertDate() {
        long date = mAlbumFile.getAddDate();
        return date * 1000;
    }

    public String getAlbumDate() {
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");//날짜 단위 변경
        return day.format(new Date(convertDate()));
    }

    public int getAlbumYear() {
        SimpleDateFormat day = new SimpleDateFormat("yyyy");//날짜 단위 변경
        String str = day.format(new Date(convertDate()));
        return Integer.parseInt(str);
    }

    public int getAlbumMonth() {
        SimpleDateFormat day = new SimpleDateFormat("MM");//날짜 단위 변경
        String str = day.format(new Date(convertDate()));
        return Integer.parseInt(str);
    }

    public int getAlbumDay() {
        SimpleDateFormat day = new SimpleDateFormat("dd");//날짜 단위 변경
        String str = day.format(new Date(convertDate()));
        return Integer.parseInt(str);
    }

    public abstract int getType();

}
