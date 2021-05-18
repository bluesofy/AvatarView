package cn.byk.pandora.avatarview.bean;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import cn.byk.pandora.avatarview.layout.ILayoutManager;

/**
 * Created by Byk on 2017/1/11.
 * <p>
 * AvatarView DisplayInfo
 */
public class DisplayInfo implements Cloneable {

    public int height;                                      // 控件的高度
    public int width;                                       // 控件的宽度

    public float borderWidth = 0;                           // 描边宽度
    public int borderColor = Color.WHITE;                   // 描边颜色

    public ArrayList<ILayoutManager.LayoutInfoGroup> coordinates;  // 测量过程返回的每个元素的对应位置信息

    public int displayType;                                 // 子元素的显示类型
    public int scaleType;                                   // 矩形的缩放类型

    public Drawable placeholder;
    public Drawable errorDrawable;

    public ArrayList<ResInfo> resInfos = new ArrayList<>();
    public ArrayList<ResInfo> resInfoForUrls = new ArrayList<>();

    public void addRes(ResInfo res) {
        resInfos.add(res);
    }

    public void addUrlRes(ResInfo res) {
        resInfoForUrls.add(res);
    }

    public void addBitmap(Bitmap bitmap) {
        resInfos.add(ResInfo.build()
                            .setBitmap(bitmap));
    }

    public void addBitmapByKey(String url, Bitmap bitmap) {
        boolean notExist = true;
        for (ResInfo res : resInfoForUrls) {
            if (res.getUrl()
                   .equals(url)) {
                addRes(res.setBitmap(bitmap));
                notExist = false;
            }
        }

        if (notExist && bitmap != null) {
            addBitmap(bitmap);
        }
    }

    public void addUrl(String url) {
        resInfos.add(ResInfo.build()
                            .setUrl(url));
    }

    public void clear() {
        resInfos.clear();
    }

    @Override
    public Object clone() {
        DisplayInfo clone;
        try {
            clone = (DisplayInfo) super.clone();
            if (coordinates != null) {
                clone.coordinates = (ArrayList<ILayoutManager.LayoutInfoGroup>) coordinates.clone();
            }
            clone.resInfos = (ArrayList<ResInfo>) resInfos.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return this;
        }
        return clone;
    }
}
