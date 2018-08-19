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
package com.cnk24.mediaalbum.api;

import android.content.Context;
import android.support.annotation.Nullable;

import com.cnk24.mediaalbum.Action;
import com.cnk24.mediaalbum.api.widget.Widget;

/**
 * 20180819 SJK: Created
 */
public abstract class BasicMediaWrapper<Returner extends BasicMediaWrapper, Result, Cancel, Checked>
{
    final Context mContext;
    Action<Result> mResult;
    Action<Cancel> mCancel;
    Widget mWidget;
    Checked mChecked;

    BasicMediaWrapper(Context context) {
        this.mContext = context;
        mWidget = Widget.getDefaultWidget(context);
    }

    /**
     * Set the action when result.
     *
     * @param result action when producing result.
     */
    public final Returner onResult(Action<Result> result) {
        this.mResult = result;
        return (Returner) this;
    }

    /**
     * Set the action when canceling.
     *
     * @param cancel action when canceled.
     */
    public final Returner onCancel(Action<Cancel> cancel) {
        this.mCancel = cancel;
        return (Returner) this;
    }

    /**
     * Set the widget property.
     *
     * @param widget the widget.
     */
    public final Returner widget(@Nullable Widget widget) {
        this.mWidget = widget;
        return (Returner) this;
    }

    /**
     * Start up.
     */
    public abstract void start();
}
