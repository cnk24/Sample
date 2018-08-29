package com.cnk24.sample.app.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateItem extends AdapterItem {

    private String mMediaDate;

    public DateItem(String date) {
        super(null);
        mMediaDate = date;
    }

    @Override
    public int getType() {
        return TYPE_DATE;
    }

    public String getMediaDate() {
        return mMediaDate;
    }

    public void setDate(String date) {
        mMediaDate = date;
    }

}
