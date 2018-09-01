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
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnk24.mediaalbum.AlbumFile;
import com.cnk24.mediaalbum.impl.OnAlbumItemClickListener;
import com.cnk24.mediaalbum.impl.OnItemClickListener;
import com.cnk24.mediaalbum.widget.divider.Api21ItemDivider;
import com.cnk24.mediaalbum.widget.divider.Divider;
import com.cnk24.sample.R;

import java.util.ArrayList;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {

    private Context mContext;
    private OnAlbumItemClickListener mAlbumItemClickListener;

    private ArrayList<SectionDataModel> mItemList;

    public RecyclerViewDataAdapter(Context context, OnAlbumItemClickListener itemClickListener) {
        this.mContext = context;
        this.mAlbumItemClickListener = itemClickListener;
    }

    public void notifyDataSetChanged(ArrayList<AlbumFile> itemList) {
        this.mItemList = initItemList(itemList);
        super.notifyDataSetChanged();
    }

    private ArrayList<SectionDataModel> initItemList(ArrayList<AlbumFile> dataset) {
        ArrayList<SectionDataModel> result = new ArrayList<>();

        int position = 0;
        int year = 0, month = 0, day = 0;
        for(AlbumFile data:dataset) {
            if(year != data.getAlbumYear() || month != data.getAlbumMonth() || day != data.getAlbumDay()) {
                result.add(new SectionDataModel(data.getAlbumDate(), new ArrayList<AlbumFile>()));

                year = data.getAlbumYear();
                month = data.getAlbumMonth();
                day = data.getAlbumDay();
                position++;
            }

            result.get(position - 1).getItemListInSection().add(data);
        }

        return result;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_content_list, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int position) {
        final String sectionName = mItemList.get(position).getHeaderTitle();
        ArrayList singleSectionItems = mItemList.get(position).getItemListInSection();
        itemRowHolder.itemTitle.setText(sectionName);

        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, mAlbumItemClickListener, singleSectionItems);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new GridLayoutManager(this.mContext, 4));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 5, 5);
        itemRowHolder.recycler_view_list.addItemDecoration(divider);
    }

    @Override
    public int getItemCount() {
        return (null != mItemList ? mItemList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView itemTitle;
        protected RecyclerView recycler_view_list;

        public ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
        }
    }

}
