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
package com.cnk24.mediaalbum.widget.photoview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 20180817 SJK: Created
 */
public class AttacherImageView extends android.support.v7.widget.AppCompatImageView
{
    private PhotoViewAttacher mAttacher;

    public AttacherImageView(Context context) {
        super(context);
    }

    public AttacherImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AttacherImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAttacher(PhotoViewAttacher attacher) {
        this.mAttacher = attacher;
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        if (mAttacher != null) {
            mAttacher.update();
        }
    }
}
