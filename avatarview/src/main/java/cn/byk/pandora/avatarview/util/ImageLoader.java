package cn.byk.pandora.avatarview.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import cn.byk.pandora.avatarview.AvatarView;
import cn.byk.pandora.avatarview.bean.ResInfo;

/**
 * Created by Byk on 2017/1/10.
 * <p>
 * Glide
 */
public class ImageLoader {

    private static volatile ImageLoader sInstance;

    private ImageLoader() {}

    public static synchronized ImageLoader getInstance() {
        if (sInstance == null) {
            synchronized (ImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoader();
                }
            }
        }
        return sInstance;
    }

    public void load(Context context, String url, SimpleTarget target) {
        load(context, url, target, null, null);
    }

    public void load(Context context, String url, SimpleTarget target, Drawable placeholder, Drawable errorDrawable) {
        BitmapTypeRequest request = Glide.with(context)
                                         .load(url)
                                         .asBitmap();

        if (placeholder != null) {
            request.placeholder(placeholder);
        }

        if (errorDrawable != null) {
            request.error(errorDrawable);
        }

        request.into(target);
    }

    public void load(Context context, List<String> urls, MultiLoadTarget target) {
        load(context, urls, target, null);
    }

    public void load(Context context, List<String> urls, MultiLoadTarget target, Drawable errorDrawable) {
        target.max(urls.size());
        for (String url : urls) {
            SimpleTarget simpleTarget = createGlideTarget(null, url, target.getWidth(), target.getHeight(), false,
                                                          target);
            load(context, url, simpleTarget, null, errorDrawable);
        }
    }

    public SimpleTarget createGlideTarget(final AvatarView view, String url, int width, int height,
                                          boolean fromNormal) {
        return createGlideTarget(view, url, width, height, fromNormal, null);
    }

    public SimpleTarget createGlideTarget(final AvatarView view, final String url, int width, int height,
                                          final boolean fromNormal, final MultiLoadTarget multiLoadTarget) {
        return new SimpleTarget<Bitmap>(width, height) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                if (view != null) {
                    view.setBitmap(resource, fromNormal);
                }

                if (multiLoadTarget != null) {
                    multiLoadTarget.add(url, resource);
                }
            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                if (view != null) {
                    view.setDrawable(placeholder, fromNormal);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);

                if (view != null) {
                    view.setDrawable(errorDrawable, fromNormal);
                }

                if (multiLoadTarget != null) {
                    multiLoadTarget.add(url, BitmapMan.toBitmap(errorDrawable));
                }
            }
        };
    }

    public MultiLoadTarget createMultiTarget(final AvatarView view, int width, int height) {
        return new MultiLoadTarget(view).size(width, height);
    }

    public void clearMemory(Context context) {
        Glide.get(context)
             .clearMemory();
    }

    //========================================================================================//
    public static class MultiLoadTarget {

        private ArrayList<ResInfo> mResInfos;

        private AvatarView mView;
        private int mWidth;
        private int mHeight;

        private int mMax;
        private int mCount;

        public MultiLoadTarget(AvatarView view) {
            mResInfos = new ArrayList<>();
            mView = view;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }

        public MultiLoadTarget size(int width, int height) {
            mWidth = width;
            mHeight = height;
            return this;
        }

        public MultiLoadTarget max(int amount) {
            mMax = amount;
            return this;
        }

        public void ready(ArrayList<ResInfo> resInfos) {
            mView.setImagesWithMix(resInfos);
        }

        public void add(String url, Bitmap bitmap) {
            mResInfos.add(ResInfo.build()
                                 .setUrl(url)
                                 .setBitmap(bitmap));

            mCount++;
            if (mCount >= mMax) {
                ready(mResInfos);
            }
        }
    }

}
