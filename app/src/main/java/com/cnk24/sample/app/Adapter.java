/*
 * Copyright 2018 Seo Jeonggyu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cnk24.sample.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnk24.mediaalbum.Album;
import com.cnk24.mediaalbum.AlbumFile;
import com.cnk24.mediaalbum.impl.OnItemClickListener;
import com.cnk24.mediaalbum.util.AlbumUtils;
import com.cnk24.sample.R;
import com.cnk24.sample.app.data.AdapterItem;
import com.cnk24.sample.app.data.DateItem;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;

    private ArrayList<AdapterItem> mItemList;

    public Adapter(Context context, OnItemClickListener itemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mItemClickListener = itemClickListener;
    }

    public void notifyDataSetChanged(ArrayList<AdapterItem> itemList) {
        //this.mItemList = itemList;

        this.mItemList = initItemList(itemList);
        super.notifyDataSetChanged();
    }

    private ArrayList<AdapterItem> initItemList(ArrayList<AdapterItem> dataset) {
        ArrayList<AdapterItem> result = new ArrayList<>();

        int year = 0, month = 0, day = 0;
        for(AdapterItem data:dataset) {
            if(year != data.getAlbumYear() || month != data.getAlbumMonth() || day != data.getAlbumDay()) {
                result.add(new DateItem(data.getAlbumDate()));
                year = data.getAlbumYear();
                month = data.getAlbumMonth();
                day = data.getAlbumDay();
            }

            result.add(data);
        }

        return result;
    }

    @Override
    public int getItemViewType(int position) {
        AdapterItem item = mItemList.get(position);
        if (item.getType() == AdapterItem.TYPE_MEDIA) {
            if (item.getAlbumFile().getMediaType() == AlbumFile.TYPE_IMAGE) {
                return AdapterItem.TYPE_IMAGE;
            } else {
                return AdapterItem.TYPE_VIDEO;
            }
        } else {
            return item.getType();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case AdapterItem.TYPE_DATE: {
                return new DateViewHolder(mInflater.inflate(R.layout.item_content_date, parent, false));
            }
            case AdapterItem.TYPE_IMAGE: {
                return new ImageViewHolder(mInflater.inflate(R.layout.item_content_image, parent, false), mItemClickListener);
            }
            case AdapterItem.TYPE_VIDEO: {
                return new VideoViewHolder(mInflater.inflate(R.layout.item_content_video, parent, false), mItemClickListener);
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case AdapterItem.TYPE_DATE: {
                ((DateViewHolder) holder).setData( ((DateItem)mItemList.get(position)).getMediaDate() );
                break;
            }
            case AdapterItem.TYPE_IMAGE: {
                ((ImageViewHolder) holder).setData( mItemList.get(position).getAlbumFile() );
                break;
            }
            case AdapterItem.TYPE_VIDEO: {
                ((VideoViewHolder) holder).setData( mItemList.get(position).getAlbumFile() );
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
        //return mAlbumFiles == null ? 0 : mAlbumFiles.size();
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemClickListener mItemClickListener;
        private ImageView mIvImage;

        ImageViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            this.mItemClickListener = itemClickListener;
            this.mIvImage = itemView.findViewById(R.id.iv_media_content_image);
            itemView.setOnClickListener(this);
        }

        public void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().
                    getAlbumLoader().
                    load(mIvImage, albumFile);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    private static class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemClickListener mItemClickListener;

        private ImageView mIvImage;
        private TextView mTvDuration;

        VideoViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            this.mItemClickListener = itemClickListener;
            this.mIvImage = itemView.findViewById(com.cnk24.mediaalbum.R.id.iv_album_content_image);
            this.mTvDuration = itemView.findViewById(com.cnk24.mediaalbum.R.id.tv_duration);
            itemView.setOnClickListener(this);
        }

        void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().
                    getAlbumLoader().
                    load(mIvImage, albumFile);
            mTvDuration.setText(AlbumUtils.convertDuration(albumFile.getDuration()));
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvDate;

        public DateViewHolder(View itemView) {
            super(itemView);
            mTvDate = itemView.findViewById(R.id.tv_date);
        }

        void setData(String date) {
            mTvDate.setText(date);
        }
    }
}
