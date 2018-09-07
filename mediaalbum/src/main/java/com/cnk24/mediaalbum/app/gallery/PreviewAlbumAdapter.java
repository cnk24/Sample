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
import android.view.View;
import android.widget.ImageView;

import com.cnk24.mediaalbum.Album;
import com.cnk24.mediaalbum.AlbumFile;

import java.util.ArrayList;

/**
 * 20180822 SJK Created.
 */
public class PreviewAlbumAdapter extends PreviewAdapter<AlbumFile> {

    public PreviewAlbumAdapter(Context context, ArrayList<AlbumFile> previewList) {
        super(context, previewList);
    }

    @Override
    protected void loadPreview(ImageView imageView, AlbumFile item, int position) {
        Album.getAlbumConfig().getAlbumLoader().load(imageView, item);
    }
}