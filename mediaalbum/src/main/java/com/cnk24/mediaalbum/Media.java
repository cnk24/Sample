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

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.IntDef;
import android.util.Log;

import com.cnk24.mediaalbum.api.*;
import com.cnk24.mediaalbum.api.camera.*;
import com.cnk24.mediaalbum.api.choice.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 20180817 SJK: Created
 */
public class Media
{
    // All.
    public static final String KEY_INPUT_WIDGET = "KEY_INPUT_WIDGET";
    public static final String KEY_INPUT_CHECKED_LIST = "KEY_INPUT_CHECKED_LIST";

    // Album.
    public static final String KEY_INPUT_FUNCTION = "KEY_INPUT_FUNCTION";
    public static final int FUNCTION_CHOICE_IMAGE = 0;
    public static final int FUNCTION_CHOICE_VIDEO = 1;

    public static final int FUNCTION_CAMERA_IMAGE = 0;
    public static final int FUNCTION_CAMERA_VIDEO = 1;

    public static final String KEY_INPUT_CHOICE_MODE = "KEY_INPUT_CHOICE_MODE";
    public static final int MODE_MULTIPLE = 1;
    public static final int MODE_SINGLE = 2;
    public static final String KEY_INPUT_COLUMN_COUNT = "KEY_INPUT_COLUMN_COUNT";
    public static final String KEY_INPUT_ALLOW_CAMERA = "KEY_INPUT_ALLOW_CAMERA";
    public static final String KEY_INPUT_LIMIT_COUNT = "KEY_INPUT_LIMIT_COUNT";

    // Camera.
    public static final String KEY_INPUT_FILE_PATH = "KEY_INPUT_FILE_PATH";
    public static final String KEY_INPUT_CAMERA_QUALITY = "KEY_INPUT_CAMERA_QUALITY";
    public static final String KEY_INPUT_CAMERA_DURATION = "KEY_INPUT_CAMERA_DURATION";
    public static final String KEY_INPUT_CAMERA_BYTES = "KEY_INPUT_CAMERA_BYTES";

    @IntDef({FUNCTION_CHOICE_IMAGE, FUNCTION_CHOICE_VIDEO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ChoiceFunction {
    }

    @IntDef({FUNCTION_CAMERA_IMAGE, FUNCTION_CAMERA_VIDEO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraFunction {
    }

    @IntDef({MODE_MULTIPLE, MODE_SINGLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ChoiceMode {
    }

    private static MediaConfig sMediaConfig;

    /**
     * Initialize Media.
     *
     * @param mediaConfig {@link MediaConfig}.
     */
    public static void initialize(MediaConfig mediaConfig) {
        if (sMediaConfig == null) sMediaConfig = mediaConfig;
        else Log.w("Media", new IllegalStateException("Illegal operation, only allowed to configure once."));
    }

    /**
     * Get the album configuration.
     */
    public static MediaConfig getMediaConfig() {
        if (sMediaConfig == null) {
            sMediaConfig = MediaConfig.newBuilder(null).build();
        }
        return sMediaConfig;
    }

    /**
     * Open the camera from the activity.
     */
    public static Camera<ImageCameraWrapper, VideoCameraWrapper> camera(Context context) {
        return new MediaCamera(context);
    }

    /**
     * Select images.
     */
    public static Choice<ImageMultipleWrapper, ImageSingleWrapper> image(Context context) {
        return new ImageChoice(context);
    }

    /**
     * Select videos.
     */
    public static Choice<VideoMultipleWrapper, VideoSingleWrapper> video(Context context) {
        return new VideoChoice(context);
    }

    /**
     * Preview picture.
     */
    public static GalleryWrapper gallery(Context context) {
        return new GalleryWrapper(context);
    }

    /**
     * Preview Album.
     */
    public static GalleryAlbumWrapper galleryAlbum(Context context) {
        return new GalleryAlbumWrapper(context);
    }

    /**
     * Open the camera from the activity.
     */
    public static Camera<ImageCameraWrapper, VideoCameraWrapper> camera(Activity activity) {
        return new MediaCamera(activity);
    }

    /**
     * Select images.
     */
    public static Choice<ImageMultipleWrapper, ImageSingleWrapper> image(Activity activity) {
        return new ImageChoice(activity);
    }

    /**
     * Select videos.
     */
    public static Choice<VideoMultipleWrapper, VideoSingleWrapper> video(Activity activity) {
        return new VideoChoice(activity);
    }

    /**
     * Preview picture.
     */
    public static BasicGalleryWrapper<GalleryWrapper, String, String, String> gallery(Activity activity) {
        return new GalleryWrapper(activity);
    }

    /**
     * Preview Album.
     */
    public static BasicGalleryWrapper<GalleryAlbumWrapper, AlbumFile, String, AlbumFile> galleryAlbum(Activity activity) {
        return new GalleryAlbumWrapper(activity);
    }

    /**
     * Open the camera from the activity.
     */
    public static Camera<ImageCameraWrapper, VideoCameraWrapper> camera(Fragment fragment) {
        return new MediaCamera(fragment.getActivity());
    }

    /**
     * Select images.
     */
    public static Choice<ImageMultipleWrapper, ImageSingleWrapper> image(Fragment fragment) {
        return new ImageChoice(fragment.getActivity());
    }

    /**
     * Select videos.
     */
    public static Choice<VideoMultipleWrapper, VideoSingleWrapper> video(Fragment fragment) {
        return new VideoChoice(fragment.getActivity());
    }

    /**
     * Preview picture.
     */
    public static BasicGalleryWrapper<GalleryWrapper, String, String, String> gallery(Fragment fragment) {
        return new GalleryWrapper(fragment.getActivity());
    }

    /**
     * Preview Album.
     */
    public static BasicGalleryWrapper<GalleryAlbumWrapper, AlbumFile, String, AlbumFile> galleryAlbum(Fragment fragment) {
        return new GalleryAlbumWrapper(fragment.getActivity());
    }

    /**
     * Open the camera from the activity.
     */
    public static Camera<ImageCameraWrapper, VideoCameraWrapper> camera(android.support.v4.app.Fragment fragment) {
        return new MediaCamera(fragment.getContext());
    }

    /**
     * Select images.
     */
    public static Choice<ImageMultipleWrapper, ImageSingleWrapper> image(android.support.v4.app.Fragment fragment) {
        return new ImageChoice(fragment.getContext());
    }

    /**
     * Select videos.
     */
    public static Choice<VideoMultipleWrapper, VideoSingleWrapper> video(android.support.v4.app.Fragment fragment) {
        return new VideoChoice(fragment.getContext());
    }

    /**
     * Preview picture.
     */
    public static BasicGalleryWrapper<GalleryWrapper, String, String, String> gallery(android.support.v4.app.Fragment fragment) {
        return new GalleryWrapper(fragment.getContext());
    }

    /**
     * Preview Album.
     */
    public static BasicGalleryWrapper<GalleryAlbumWrapper, AlbumFile, String, AlbumFile> galleryAlbum(android.support.v4.app.Fragment fragment) {
        return new GalleryAlbumWrapper(fragment.getContext());
    }
}