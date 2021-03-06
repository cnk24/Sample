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

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.cnk24.mediaalbum.AlbumFile;
import com.cnk24.mediaalbum.AlbumFolder;
import com.cnk24.mediaalbum.R;
import com.cnk24.mediaalbum.api.widget.Widget;
import com.cnk24.mediaalbum.app.Contract;
import com.cnk24.mediaalbum.impl.DoubleClickWrapper;
import com.cnk24.mediaalbum.impl.OnAlbumItemClickListener;
import com.cnk24.mediaalbum.impl.OnCheckedClickListener;
import com.cnk24.mediaalbum.impl.OnGroupCheckedClickListener;
import com.cnk24.mediaalbum.impl.OnItemClickListener;
import com.cnk24.mediaalbum.util.AlbumUtils;
import com.cnk24.mediaalbum.util.SystemBar;
import com.cnk24.mediaalbum.widget.ColorProgressBar;
import com.cnk24.mediaalbum.widget.divider.Api21ItemDivider;

/**
 * 20180819 SJK: Created
 */
class AlbumView extends Contract.AlbumView implements View.OnClickListener {

    private Activity mActivity;

    private Toolbar mToolbar;
    private MenuItem mCompleteMenu;

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private AlbumGroupAdapter mAdapter;

    private Button mBtnPreview;
    private Button mBtnSwitchFolder;

    private LinearLayout mLayoutLoading;
    private ColorProgressBar mProgressBar;

    public AlbumView(Activity activity, Contract.AlbumPresenter presenter) {
        super(activity, presenter);
        this.mActivity = activity;

        this.mToolbar = activity.findViewById(R.id.toolbar);
        this.mRecyclerView = activity.findViewById(R.id.recycler_view);

        this.mBtnSwitchFolder = activity.findViewById(R.id.btn_switch_dir);
        this.mBtnPreview = activity.findViewById(R.id.btn_preview);

        this.mLayoutLoading = activity.findViewById(R.id.layout_loading);
        this.mProgressBar = activity.findViewById(R.id.progress_bar);

        this.mToolbar.setOnClickListener(new DoubleClickWrapper(this));
        this.mBtnSwitchFolder.setOnClickListener(this);
        this.mBtnPreview.setOnClickListener(this);
    }

    @Override
    protected void onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_menu_album, menu);
        mCompleteMenu = menu.findItem(R.id.album_menu_finish);
    }

    @Override
    protected void onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.album_menu_finish) {
            getPresenter().complete();
        }
    }

    @Override
    public void setupViews(Widget widget, int column, boolean hasCamera, int choiceMode) {
        SystemBar.setNavigationBarColor(mActivity, widget.getNavigationBarColor());

        int statusBarColor = widget.getStatusBarColor();
        if (widget.getUiStyle() == Widget.STYLE_LIGHT) {
            if (SystemBar.setStatusBarDarkFont(mActivity, true)) {
                SystemBar.setStatusBarColor(mActivity, statusBarColor);
            } else {
                SystemBar.setStatusBarColor(mActivity, getColor(R.color.albumColorPrimaryBlack));
            }

            mProgressBar.setColorFilter(getColor(R.color.albumLoadingDark));

            Drawable navigationIcon = getDrawable(R.drawable.album_ic_back_white);
            AlbumUtils.setDrawableTint(navigationIcon, getColor(R.color.albumIconDark));
            setHomeAsUpIndicator(navigationIcon);

            Drawable completeIcon = mCompleteMenu.getIcon();
            AlbumUtils.setDrawableTint(completeIcon, getColor(R.color.albumIconDark));
            mCompleteMenu.setIcon(completeIcon);
        } else {
            mProgressBar.setColorFilter(widget.getToolBarColor());
            SystemBar.setStatusBarColor(mActivity, statusBarColor);
            setHomeAsUpIndicator(R.drawable.album_ic_back_white);
        }
        mToolbar.setBackgroundColor(widget.getToolBarColor());

        Configuration config = mActivity.getResources().getConfiguration();
        mLayoutManager = new GridLayoutManager(getContext(), 1, getOrientation(config), false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        int dividerSize = getResources().getDimensionPixelSize(R.dimen.album_dp_4);
        mRecyclerView.addItemDecoration(new Api21ItemDivider(Color.TRANSPARENT, dividerSize, dividerSize));

        mAdapter = new AlbumGroupAdapter(getContext(), hasCamera, choiceMode, widget.getMediaItemCheckSelector());
        mAdapter.setLayoutManager(column, getOrientation(config));
        mAdapter.setAddClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getPresenter().clickCamera(view);
            }
        });
        mAdapter.setCheckedClickListener(new OnCheckedClickListener() {
            @Override
            public void onCheckedClick(CompoundButton button, AlbumFile albumFile) {
                getPresenter().tryCheckItem(button, albumFile);
            }
        });
        mAdapter.setAlbumItemClickListener(new OnAlbumItemClickListener() {
            @Override
            public void onAlbumItemClick(View view, AlbumFile albumFile) {
                getPresenter().tryPreviewItem(albumFile);
            }
        });
        mAdapter.setGroupCheckedClickListener(new OnGroupCheckedClickListener() {
            @Override
            public void onCheckedClick(CompoundButton button, SectionDataModel sectionData) {
                for (AlbumFile albumFile:sectionData.getItemListInSection()) {
                    getPresenter().tryCheckItem(button, albumFile);
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setLoadingDisplay(boolean display) {
        mLayoutLoading.setVisibility(display ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int position = mLayoutManager.findFirstVisibleItemPosition();
        mLayoutManager.setOrientation(getOrientation(newConfig));
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager.scrollToPosition(position);
    }

    @RecyclerView.Orientation
    private int getOrientation(Configuration config) {
        switch (config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                return LinearLayoutManager.VERTICAL;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                return LinearLayoutManager.HORIZONTAL;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
    }

    @Override
    public void setCompleteDisplay(boolean display) {
        mCompleteMenu.setVisible(display);
    }

    @Override
    public void bindAlbumFolder(AlbumFolder albumFolder) {
        mBtnSwitchFolder.setText(albumFolder.getName());

        mAdapter.notifyDataSetChanged(albumFolder.getAlbumFiles());
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void notifyInsertItem(int position) {
        mAdapter.notifyItemInserted(position);
    }

    @Override
    public void notifyItem(int position) {
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void setCheckedCount(int count) {
        mBtnPreview.setText(" (" + count + ")");
    }

    @Override
    public void onClick(View v) {
        if (v == mToolbar) {
            mRecyclerView.smoothScrollToPosition(0);
        } else if (v == mBtnSwitchFolder) {
            getPresenter().clickFolderSwitch();
        } else if (v == mBtnPreview) {
            getPresenter().tryPreviewChecked();
        }
    }
}
