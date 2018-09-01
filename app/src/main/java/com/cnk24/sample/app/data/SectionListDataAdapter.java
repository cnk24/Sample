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
package com.cnk24.sample.app.data;

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
import com.cnk24.mediaalbum.impl.OnAlbumItemClickListener;
import com.cnk24.mediaalbum.impl.OnItemClickListener;
import com.cnk24.mediaalbum.util.AlbumUtils;
import com.cnk24.sample.R;

import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private OnAlbumItemClickListener mAlbumItemClickListener;

    private ArrayList<AlbumFile> mItemList;

    public SectionListDataAdapter(Context context, OnAlbumItemClickListener itemClickListener, ArrayList<AlbumFile> itemList) {
        this.mInflater = LayoutInflater.from(context);
        this.mAlbumItemClickListener = itemClickListener;
        this.mItemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position).getMediaType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case AlbumFile.TYPE_IMAGE: {
                return new SectionListDataAdapter.ImageViewHolder(mInflater.inflate(R.layout.item_content_image, viewGroup, false), mAlbumItemClickListener);
            }
            case AlbumFile.TYPE_VIDEO: {
                return new SectionListDataAdapter.VideoViewHolder(mInflater.inflate(R.layout.item_content_video, viewGroup, false), mAlbumItemClickListener);
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case AlbumFile.TYPE_IMAGE: {
                ((SectionListDataAdapter.ImageViewHolder) holder).setData(mItemList.get(position));
                break;
            }
            case AlbumFile.TYPE_VIDEO: {
                ((SectionListDataAdapter.VideoViewHolder) holder).setData(mItemList.get(position));
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return (null != mItemList ? mItemList.size() : 0);
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnAlbumItemClickListener mAlbumItemClickListener;
        private ImageView mIvImage;
        private AlbumFile mAlbumFile;

        ImageViewHolder(View itemView, OnAlbumItemClickListener itemClickListener) {
            super(itemView);
            this.mAlbumItemClickListener = itemClickListener;
            this.mIvImage = itemView.findViewById(R.id.iv_media_content_image);
            itemView.setOnClickListener(this);
        }

        public void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().
                    getAlbumLoader().
                    load(mIvImage, albumFile);

            mAlbumFile = albumFile;
        }

        @Override
        public void onClick(View v) {
            if (mAlbumItemClickListener != null) {
                mAlbumItemClickListener.onAlbumItemClick(v, mAlbumFile);
            }
        }
    }

    private static class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnAlbumItemClickListener mAlbumItemClickListener;
        private AlbumFile mAlbumFile;

        private ImageView mIvImage;
        private TextView mTvDuration;

        VideoViewHolder(View itemView, OnAlbumItemClickListener itemClickListener) {
            super(itemView);
            this.mAlbumItemClickListener = itemClickListener;
            this.mIvImage = itemView.findViewById(R.id.iv_media_content_image);
            this.mTvDuration = itemView.findViewById(R.id.tv_duration);
            itemView.setOnClickListener(this);
        }

        void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().
                    getAlbumLoader().
                    load(mIvImage, albumFile);

            mAlbumFile = albumFile;
            mTvDuration.setText(AlbumUtils.convertDuration(albumFile.getDuration()));
        }

        @Override
        public void onClick(View v) {
            if (mAlbumItemClickListener != null) {
                mAlbumItemClickListener.onAlbumItemClick(v, mAlbumFile);
            }
        }
    }

}

