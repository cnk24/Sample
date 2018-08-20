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
package com.cnk24.mediaalbum.app.media;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.cnk24.mediaalbum.Action;
import com.cnk24.mediaalbum.Filter;
import com.cnk24.mediaalbum.Media;
import com.cnk24.mediaalbum.MediaFile;
import com.cnk24.mediaalbum.MediaFolder;
import com.cnk24.mediaalbum.R;
import com.cnk24.mediaalbum.api.widget.Widget;
import com.cnk24.mediaalbum.app.Contract;
import com.cnk24.mediaalbum.app.media.data.MediaReadTask;
import com.cnk24.mediaalbum.app.media.data.MediaReader;
import com.cnk24.mediaalbum.app.media.data.PathConversion;
import com.cnk24.mediaalbum.app.media.data.PathConvertTask;
import com.cnk24.mediaalbum.app.media.data.ThumbnailBuildTask;
import com.cnk24.mediaalbum.impl.OnItemClickListener;
import com.cnk24.mediaalbum.mvp.BaseActivity;
import com.cnk24.mediaalbum.util.MediaUtils;
import com.cnk24.mediaalbum.util.mediascanner.MediaScanner;
import com.cnk24.mediaalbum.widget.LoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 20180819 SJK: Created
 */
public class MediaActivity extends BaseActivity implements
                                        Contract.MediaPresenter,
                                        MediaReadTask.Callback,
                                        GalleryActivity.Callback,
                                        PathConvertTask.Callback,
                                        ThumbnailBuildTask.Callback
{
    private static final int CODE_ACTIVITY_NULL = 1;
    private static final int CODE_PERMISSION_STORAGE = 1;

    public static Filter<Long> sSizeFilter;
    public static Filter<String> sMimeFilter;
    public static Filter<Long> sDurationFilter;

    public static Action<ArrayList<MediaFile>> sResult;
    public static Action<String> sCancel;

    private List<MediaFolder> mMediaFolders;
    private int mCurrentFolder;

    private Widget mWidget;
    private int mFunction;
    private int mChoiceMode;
    private int mColumnCount;
    private boolean mHasCamera;
    private int mLimitCount;

    private int mQuality;
    private long mLimitDuration;
    private long mLimitBytes;

    private ArrayList<MediaFile> mCheckedList;
    private MediaScanner mMediaScanner;

    private Contract.MediaView mView;
    private FolderDialog mFolderDialog;
    private PopupMenu mCameraPopupMenu;
    private LoadingDialog mLoadingDialog;

    private MediaReadTask mMediaReadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeArgument();
        setContentView(createView());
        mView = new MediaView(this, this);
        mView.setupViews(mWidget, mColumnCount, mHasCamera, mChoiceMode);
        mView.setTitle(mWidget.getTitle());
        mView.setCompleteDisplay(false);
        mView.setLoadingDisplay(true);

        requestPermission(PERMISSION_STORAGE, CODE_PERMISSION_STORAGE);
    }

    private void initializeArgument() {
        Bundle argument = getIntent().getExtras();
        assert argument != null;
        mWidget = argument.getParcelable(Media.KEY_INPUT_WIDGET);
        mFunction = argument.getInt(Media.KEY_INPUT_FUNCTION);
        mChoiceMode = argument.getInt(Media.KEY_INPUT_CHOICE_MODE);
        mColumnCount = argument.getInt(Media.KEY_INPUT_COLUMN_COUNT);
        mHasCamera = argument.getBoolean(Media.KEY_INPUT_ALLOW_CAMERA);
        mLimitCount = argument.getInt(Media.KEY_INPUT_LIMIT_COUNT);
        mQuality = argument.getInt(Media.KEY_INPUT_CAMERA_QUALITY);
        mLimitDuration = argument.getLong(Media.KEY_INPUT_CAMERA_DURATION);
        mLimitBytes = argument.getLong(Media.KEY_INPUT_CAMERA_BYTES);
    }

    /**
     * Use different layouts depending on the style.
     *
     * @return layout id.
     */
    private int createView() {
        switch (mWidget.getUiStyle()) {
            case Widget.STYLE_DARK: {
                return R.layout.media_activity_album_dark;
            }
            case Widget.STYLE_LIGHT: {
                return R.layout.media_activity_album_light;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mView.onConfigurationChanged(newConfig);
        if (mFolderDialog != null && !mFolderDialog.isShowing()) mFolderDialog = null;
    }

    @Override
    protected void onPermissionGranted(int code) {
        ArrayList<MediaFile> checkedList = getIntent().getParcelableArrayListExtra(Media.KEY_INPUT_CHECKED_LIST);
        MediaReader mediaReader = new MediaReader(this, sSizeFilter, sMimeFilter, sDurationFilter);
        mMediaReadTask = new MediaReadTask(mFunction, checkedList, mediaReader, this);
        mMediaReadTask.execute();
    }

    @Override
    protected void onPermissionDenied(int code) {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.media_title_permission_failed)
                .setMessage(R.string.media_permission_storage_failed_hint)
                .setPositiveButton(R.string.media_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callbackCancel();
                    }
                })
                .show();
    }

    @Override
    public void onScanCallback(ArrayList<MediaFolder> mediaFolders, ArrayList<MediaFile> checkedFiles) {
        mMediaReadTask = null;
        switch (mChoiceMode) {
            case Media.MODE_MULTIPLE: {
                mView.setCompleteDisplay(true);
                break;
            }
            case Media.MODE_SINGLE: {
                mView.setCompleteDisplay(false);
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }

        mView.setLoadingDisplay(false);
        mMediaFolders = mediaFolders;
        mCheckedList = checkedFiles;

        if (mMediaFolders.get(0).getMediaFiles().isEmpty()) {
            Intent intent = new Intent(this, NullActivity.class);
            intent.putExtras(getIntent());
            startActivityForResult(intent, CODE_ACTIVITY_NULL);
        } else {
            showFolderAlbumFiles(0);
            int count = mCheckedList.size();
            mView.setCheckedCount(count);
            mView.setSubTitle(count + "/" + mLimitCount);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_ACTIVITY_NULL: {
                if (resultCode == RESULT_OK) {
                    String imagePath = NullActivity.parsePath(data);
                    String mimeType = MediaUtils.getMimeType(imagePath);
                    if (!TextUtils.isEmpty(mimeType)) mCameraAction.onAction(imagePath);
                } else {
                    callbackCancel();
                }
                break;
            }
        }
    }

    @Override
    public void clickFolderSwitch() {
        if (mFolderDialog == null) {
            mFolderDialog = new FolderDialog(this, mWidget, mMediaFolders, new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    mCurrentFolder = position;
                    showFolderAlbumFiles(mCurrentFolder);
                }
            });
        }
        if (!mFolderDialog.isShowing()) mFolderDialog.show();
    }

    /**
     * Update data source.
     */
    private void showFolderAlbumFiles(int position) {
        this.mCurrentFolder = position;
        MediaFolder mediaFolder = mMediaFolders.get(position);
        mView.bindMediaFolder(mediaFolder);
    }

    @Override
    public void clickCamera(View v) {
        int hasCheckSize = mCheckedList.size();
        if (hasCheckSize >= mLimitCount) {
            int messageRes;
            switch (mFunction) {
                case Media.FUNCTION_CHOICE_IMAGE: {
                    messageRes = R.plurals.media_check_image_limit_camera;
                    break;
                }
                case Media.FUNCTION_CHOICE_VIDEO: {
                    messageRes = R.plurals.media_check_video_limit_camera;
                    break;
                }
                default: {
                    throw new AssertionError("This should not be the case.");
                }
            }
            mView.toast(getResources().getQuantityString(messageRes, mLimitCount, mLimitCount));
        } else {
            switch (mFunction) {
                case Media.FUNCTION_CHOICE_IMAGE: {
                    takePicture();
                    break;
                }
                case Media.FUNCTION_CHOICE_VIDEO: {
                    takeVideo();
                    break;
                }
                /*case Media.FUNCTION_CHOICE_ALBUM: {
                    if (mCameraPopupMenu == null) {
                        mCameraPopupMenu = new PopupMenu(this, v);
                        mCameraPopupMenu.getMenuInflater().inflate(R.menu.album_menu_item_camera, mCameraPopupMenu.getMenu());
                        mCameraPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int id = item.getItemId();
                                if (id == R.id.album_menu_camera_image) {
                                    takePicture();
                                } else if (id == R.id.album_menu_camera_video) {
                                    takeVideo();
                                }
                                return true;
                            }
                        });
                    }
                    mCameraPopupMenu.show();
                    break;
                }*/
                default: {
                    throw new AssertionError("This should not be the case.");
                }
            }
        }
    }

    private void takePicture() {
        String filePath;
        if (mCurrentFolder == 0) {
            filePath = MediaUtils.randomJPGPath();
        } else {
            File file = new File(mMediaFolders.get(mCurrentFolder).getMediaFiles().get(0).getPath());
            filePath = MediaUtils.randomJPGPath(file.getParentFile());
        }
        Media.camera(this)
                .image()
                .filePath(filePath)
                .onResult(mCameraAction)
                .start();
    }

    private void takeVideo() {
        String filePath;
        if (mCurrentFolder == 0) {
            filePath = MediaUtils.randomMP4Path();
        } else {
            File file = new File(mMediaFolders.get(mCurrentFolder).getMediaFiles().get(0).getPath());
            filePath = MediaUtils.randomMP4Path(file.getParentFile());
        }
        Media.camera(this)
                .video()
                .filePath(filePath)
                .quality(mQuality)
                .limitDuration(mLimitDuration)
                .limitBytes(mLimitBytes)
                .onResult(mCameraAction)
                .start();
    }

    private Action<String> mCameraAction = new Action<String>() {
        @Override
        public void onAction(@NonNull String result) {
            if (mMediaScanner == null) {
                mMediaScanner = new MediaScanner(MediaActivity.this);
            }
            mMediaScanner.scan(result);

            PathConversion conversion = new PathConversion(sSizeFilter, sMimeFilter, sDurationFilter);
            PathConvertTask task = new PathConvertTask(conversion, MediaActivity.this);
            task.execute(result);
        }
    };

    @Override
    public void onConvertStart() {
        showLoadingDialog();
        mLoadingDialog.setMessage(R.string.media_converting);
    }

    @Override
    public void onConvertCallback(MediaFile mediaFile) {
        mediaFile.setChecked(!mediaFile.isDisable());
        //if (mediaFile.isDisable()) {
        //    if (mFilterVisibility) addFileToList(albumFile);
        //    else mView.toast(getString(R.string.media_take_file_unavailable));
        //} else {
            addFileToList(mediaFile);
        //}

        dismissLoadingDialog();
    }

    private void addFileToList(MediaFile mediaFile) {
        if (mCurrentFolder != 0) {
            List<MediaFile> mediaFiles = mMediaFolders.get(0).getMediaFiles();
            if (mediaFiles.size() > 0) mediaFiles.add(0, mediaFile);
            else mediaFiles.add(mediaFile);
        }

        MediaFolder mediaFolder = mMediaFolders.get(mCurrentFolder);
        List<MediaFile> mediaFiles = mediaFolder.getMediaFiles();
        if (mediaFiles.isEmpty()) {
            mediaFiles.add(mediaFile);
            mView.bindMediaFolder(mediaFolder);
        } else {
            mediaFiles.add(0, mediaFile);
            mView.notifyInsertItem(mHasCamera ? 1 : 0);
        }

        mCheckedList.add(mediaFile);
        int count = mCheckedList.size();
        mView.setCheckedCount(count);
        mView.setSubTitle(count + "/" + mLimitCount);

        switch (mChoiceMode) {
            case Media.MODE_SINGLE: {
                callbackResult();
                break;
            }
            case Media.MODE_MULTIPLE: {
                // Nothing.
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
    }

    @Override
    public void tryCheckItem(CompoundButton button, int position) {
        MediaFile mediaFile = mMediaFolders.get(mCurrentFolder).getMediaFiles().get(position);
        if (button.isChecked()) {
            if (mCheckedList.size() >= mLimitCount) {
                int messageRes;
                switch (mFunction) {
                    case Media.FUNCTION_CHOICE_IMAGE: {
                        messageRes = R.plurals.media_check_image_limit;
                        break;
                    }
                    case Media.FUNCTION_CHOICE_VIDEO: {
                        messageRes = R.plurals.media_check_video_limit;
                        break;
                    }
                    default: {
                        throw new AssertionError("This should not be the case.");
                    }
                }
                mView.toast(getResources().getQuantityString(messageRes, mLimitCount, mLimitCount));
                button.setChecked(false);
            } else {
                mediaFile.setChecked(true);
                mCheckedList.add(mediaFile);
                setCheckedCount();
            }
        } else {
            mediaFile.setChecked(false);
            mCheckedList.remove(mediaFile);
            setCheckedCount();
        }
    }

    private void setCheckedCount() {
        int count = mCheckedList.size();
        mView.setCheckedCount(count);
        mView.setSubTitle(count + "/" + mLimitCount);
    }

    @Override
    public void tryPreviewItem(int position) {
        switch (mChoiceMode) {
            case Media.MODE_SINGLE: {
                MediaFile mediaFile = mMediaFolders.get(mCurrentFolder).getMediaFiles().get(position);
//                albumFile.setChecked(true);
//                mView.notifyItem(position);
                mCheckedList.add(mediaFile);
                setCheckedCount();

                callbackResult();
                break;
            }
            case Media.MODE_MULTIPLE: {
                GalleryActivity.sMediaFiles = mMediaFolders.get(mCurrentFolder).getMediaFiles();
                GalleryActivity.sCheckedCount = mCheckedList.size();
                GalleryActivity.sCurrentPosition = position;
                GalleryActivity.sCallback = this;
                Intent intent = new Intent(this, GalleryActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
    }

    @Override
    public void tryPreviewChecked() {
        if (mCheckedList.size() > 0) {
            GalleryActivity.sMediaFiles = new ArrayList<>(mCheckedList);
            GalleryActivity.sCheckedCount = mCheckedList.size();
            GalleryActivity.sCurrentPosition = 0;
            GalleryActivity.sCallback = this;
            Intent intent = new Intent(this, GalleryActivity.class);
            intent.putExtras(getIntent());
            startActivity(intent);
        }
    }

    @Override
    public void onPreviewComplete() {
        callbackResult();
    }

    @Override
    public void onPreviewChanged(MediaFile mediaFile) {
        ArrayList<MediaFile> mediaFiles = mMediaFolders.get(mCurrentFolder).getMediaFiles();
        int position = mediaFiles.indexOf(mediaFile);
        int notifyPosition = mHasCamera ? position + 1 : position;
        mView.notifyItem(notifyPosition);

        if (mediaFile.isChecked()) {
            if (!mCheckedList.contains(mediaFile)) mCheckedList.add(mediaFile);
        } else {
            if (mCheckedList.contains(mediaFile)) mCheckedList.remove(mediaFile);
        }
        setCheckedCount();
    }

    @Override
    public void complete() {
        if (mCheckedList.isEmpty()) {
            int messageRes;
            switch (mFunction) {
                case Media.FUNCTION_CHOICE_IMAGE: {
                    messageRes = R.string.media_check_image_little;
                    break;
                }
                case Media.FUNCTION_CHOICE_VIDEO: {
                    messageRes = R.string.media_check_video_little;
                    break;
                }
                default: {
                    throw new AssertionError("This should not be the case.");
                }
            }
            mView.toast(messageRes);
        } else {
            callbackResult();
        }
    }

    @Override
    public void onBackPressed() {
        if (mMediaReadTask != null) mMediaReadTask.cancel(true);
        callbackCancel();
    }

    /**
     * Callback result action.
     */
    private void callbackResult() {
        ThumbnailBuildTask task = new ThumbnailBuildTask(this, mCheckedList, this);
        task.execute();
    }

    @Override
    public void onThumbnailStart() {
        showLoadingDialog();
        mLoadingDialog.setMessage(R.string.media_thumbnail);
    }

    @Override
    public void onThumbnailCallback(ArrayList<MediaFile> mediaFiles) {
        if (sResult != null) sResult.onAction(mediaFiles);
        dismissLoadingDialog();
        finish();
    }

    /**
     * Callback cancel action.
     */
    private void callbackCancel() {
        if (sCancel != null) sCancel.onAction("User canceled.");
        finish();
    }

    /**
     * Display loading dialog.
     */
    private void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setupViews(mWidget);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    /**
     * Dismiss loading dialog.
     */
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void finish() {
        sSizeFilter = null;
        sMimeFilter = null;
        sDurationFilter = null;
        sResult = null;
        sCancel = null;
        super.finish();
    }
}
