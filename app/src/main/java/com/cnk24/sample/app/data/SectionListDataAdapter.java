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

import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private Context mContext;
    private ArrayList<AlbumFile> mItemList;

    public SectionListDataAdapter(Context context, ArrayList<AlbumFile> itemList) {
        this.mItemList = itemList;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position).getMediaType();
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_content_image, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);


        //switch (viewType) {
        //    case AdapterItem.TYPE_DATE: {
        //        return new Adapter.DateViewHolder(mInflater.inflate(R.layout.item_content_date, parent, false));
        //    }
        //    case AdapterItem.TYPE_IMAGE: {
        //        return new Adapter.ImageViewHolder(mInflater.inflate(R.layout.item_content_image, parent, false), mItemClickListener);
        //    }
        //    case AdapterItem.TYPE_VIDEO: {
        //        return new Adapter.VideoViewHolder(mInflater.inflate(R.layout.item_content_video, parent, false), mItemClickListener);
        //    }
        //    default: {
        //        throw new AssertionError("This should not be the case.");
        //    }
        //}

        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {
        //SingleItemModel singleItem = itemsList.get(i);

        //holder.tvTitle.setText(singleItem.getName());
        //String url = singleItem.getUrl().toString();

        //holder.webView.setWebViewClient(new WebViewClient()); // 이걸 안해주면 새창이 뜸
        //holder.webView.loadUrl(url);


        //int viewType = getItemViewType(position);
        //switch (viewType) {
        //    case AdapterItem.TYPE_DATE: {
        //        ((Adapter.DateViewHolder) holder).setData( ((DateItem)mItemList.get(position)).getMediaDate() );
        //        break;
        //    }
        //    case AdapterItem.TYPE_IMAGE: {
        //        ((Adapter.ImageViewHolder) holder).setData( mItemList.get(position).getAlbumFile() );
        //        break;
        //    }
        //    case AdapterItem.TYPE_VIDEO: {
        //        ((Adapter.VideoViewHolder) holder).setData( mItemList.get(position).getAlbumFile() );
        //        break;
        //    }
        //}

    }

    @Override
    public int getItemCount() {
        return (null != mItemList ? mItemList.size() : 0);
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


    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        //protected TextView tvTitle;
        //protected WebView webView;

        public SingleItemRowHolder(View view) {
            super(view);

            //this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            //this.webView = (WebView)view.findViewById(R.id.webview);

            //view.setOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
            //    }
            //});
        }
    }

}

