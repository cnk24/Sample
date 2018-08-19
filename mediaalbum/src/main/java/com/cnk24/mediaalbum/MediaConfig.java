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
package com.cnk24.mediaalbum;

import android.content.Context;

import java.util.Locale;

/**
 * 20180814 SJK: Created
 */
public class MediaConfig
{
    /**
     * Create a new builder.
     */
    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    private MediaLoader mLoader;
    private Locale mLocale;

    private MediaConfig(Builder builder) {
        this.mLoader = builder.mLoader == null ? MediaLoader.DEFAULT : builder.mLoader;
        this.mLocale = builder.mLocale == null ? Locale.getDefault() : builder.mLocale;
    }

    /**
     * Get {@link MediaLoader}.
     *
     * @return {@link MediaLoader}.
     */
    public MediaLoader getMediaLoader() {
        return mLoader;
    }

    /**
     * Get {@link Locale}.
     *
     * @return {@link Locale}.
     */
    public Locale getLocale() {
        return mLocale;
    }

    public static final class Builder {

        private MediaLoader mLoader;
        private Locale mLocale;

        private Builder(Context context) {
        }

        /**
         * Set album loader.
         *
         * @param loader {@link MediaLoader}.
         * @return {@link Builder}.
         */
        public Builder setMediaLoader(MediaLoader loader) {
            this.mLoader = loader;
            return this;
        }

        /**
         * Set locale for language.
         *
         * @param locale {@link Locale}.
         * @return {@link Builder}.
         */
        public Builder setLocale(Locale locale) {
            this.mLocale = locale;
            return this;
        }

        /**
         * Create AlbumConfig.
         *
         * @return {@link MediaConfig}.
         */
        public MediaConfig build() {
            return new MediaConfig(this);
        }
    }
}
