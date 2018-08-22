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
package com.cnk24.sample;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cnk24.mediaalbum.MediaFile;

import com.cnk24.sample.R;

public class MediaLoader implements com.cnk24.mediaalbum.MediaLoader
{
    @Override
    public void load(ImageView imageView, MediaFile mediaFile) {
        load(imageView, mediaFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder);

        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
