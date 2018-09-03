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
package com.cnk24.mediaalbum.impl;

import android.widget.CompoundButton;

import com.cnk24.mediaalbum.AlbumFile;
import com.cnk24.mediaalbum.app.album.SectionDataModel;

/**
 * 20180903 SJK: Created
 */
public interface OnGroupCheckedClickListener {

    /**
     * Compound button is clicked.
     *
     * @param button   view.
     * @param sectionData
     */
    void onCheckedClick(CompoundButton button, SectionDataModel sectionData);
}
