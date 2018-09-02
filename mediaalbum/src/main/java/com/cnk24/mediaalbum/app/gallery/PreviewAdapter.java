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
package com.cnk24.mediaalbum.app.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cnk24.mediaalbum.AlbumFile;
import com.cnk24.mediaalbum.R;
import com.cnk24.mediaalbum.impl.OnAlbumItemClickListener;
import com.cnk24.mediaalbum.impl.OnItemClickListener;
import com.cnk24.mediaalbum.widget.photoview.AttacherImageView;
import com.cnk24.mediaalbum.widget.photoview.PhotoViewAttacher;

import java.util.List;

/**
 * 20180822 SJK Created.
 */
public abstract class PreviewAdapter<T> extends PagerAdapter {

    private Context mContext;
    private List<T> mPreviewList;

    private OnItemClickListener mItemClickListener;
    private OnItemClickListener mItemLongClickListener;
    private OnAlbumItemClickListener mAlbumItemClickListener;

    public PreviewAdapter(Context context, List<T> previewList) {
        this.mContext = context;
        this.mPreviewList = previewList;
    }

    /**
     * Set item click listener.
     *
     * @param onClickListener listener.
     */
    public void setItemClickListener(OnItemClickListener onClickListener) {
        this.mItemClickListener = onClickListener;
    }

    /**
     * Set item long click listener.
     *
     * @param longClickListener listener.
     */
    public void setItemLongClickListener(OnItemClickListener longClickListener) {
        this.mItemLongClickListener = longClickListener;
    }

    /**
     * Set item click listener.
     *
     * @param onClickListener listener.
     */
    public void setAlbumItemClickListener(OnAlbumItemClickListener onClickListener) {
        this.mAlbumItemClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return mPreviewList == null ? 0 : mPreviewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Object itemView = null;
        T t = mPreviewList.get(position);
        final AlbumFile albumFile = (AlbumFile)t;

        switch (albumFile.getMediaType())
        {
            case AlbumFile.TYPE_IMAGE: {
                AttacherImageView imageView = new AttacherImageView(mContext);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
                imageView.setAttacher(attacher);
                loadPreview(imageView, t, position);

                imageView.setId(position);
                if (mItemClickListener != null) {
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemClickListener.onItemClick(v, v.getId());
                        }
                    });
                }
                if (mItemLongClickListener != null) {
                    imageView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            mItemLongClickListener.onItemClick(v, v.getId());
                            return true;
                        }
                    });
                }

                container.addView(imageView);
                itemView = imageView;
                break;
            }
            case AlbumFile.TYPE_VIDEO: {
                AttacherImageView imageView = new AttacherImageView(mContext);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                loadPreview(imageView, t, position);

                AttacherImageView playBtnView = new AttacherImageView(mContext);
                playBtnView.setBackgroundResource(R.drawable.album_ic_video_camera_white);
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                param.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                playBtnView.setLayoutParams(param);

                playBtnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAlbumItemClickListener.onAlbumItemClick(v, albumFile);
                    }
                });

                RelativeLayout relativeLayout = new RelativeLayout(mContext);
                relativeLayout.addView(imageView);
                relativeLayout.addView(playBtnView);

                container.addView(relativeLayout);
                itemView = relativeLayout;
                break;
            }
        }

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(((View) object));
    }

    protected abstract void loadPreview(ImageView imageView, T item, int position);
}