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
import com.cnk24.mediaalbum.AlbumFile;

import com.cnk24.mediaalbum.AlbumLoader;
import com.cnk24.sample.R;

//import com.bumptech.glide.Priority;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MediaLoader implements AlbumLoader
{
    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {

        // Glide 4.8.0
        //RequestOptions options = new RequestOptions()
        //        .centerCrop()
        //        .placeholder(R.drawable.placeholder)
        //        .error(R.drawable.placeholder)
        //        .diskCacheStrategy(DiskCacheStrategy.ALL)
        //        .priority(Priority.HIGH);
        //Glide.with(imageView.getContext())
        //        .load(url)
        //        .apply(options)
        //        .transition(withCrossFade())
        //        .into(imageView);

        // Glide 3.8.0
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .crossFade()
                .into(imageView);
    }
}
