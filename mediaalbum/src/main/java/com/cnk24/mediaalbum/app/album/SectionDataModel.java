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
package com.cnk24.mediaalbum.app.album;

import com.cnk24.mediaalbum.AlbumFile;

import java.util.ArrayList;

public class SectionDataModel {

    private String mHeaderTitle;
    private ArrayList<AlbumFile> mItemListInSection;

    public SectionDataModel(String headerTitle, ArrayList<AlbumFile> itemListInSection) {
        this.mHeaderTitle = headerTitle;
        this.mItemListInSection = itemListInSection;
    }

    public String getHeaderTitle() {
        return mHeaderTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.mHeaderTitle = headerTitle;
    }

    public ArrayList<AlbumFile> getItemListInSection() {
        return mItemListInSection;
    }

    public void setItemListInSection(ArrayList<AlbumFile> itemListInSection) {
        this.mItemListInSection = itemListInSection;
    }

}
