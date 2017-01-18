package cn.byk.pandora.avatarview.bean;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Byk on 2017/1/11.
 * <p>
 * Single Res
 */
public class ResInfo {

    // 显示模式：图片 or 文字
    private boolean isForceText;

    // 无图片显示文字
    private String briefText;
    private int textColor;
    private int bgColor;

    // 有图片
    private Bitmap bitmap;
    private String url;

    private ResInfo() {
        this.briefText = "";
        this.textColor = Color.WHITE;
        this.bgColor = Color.LTGRAY;
    }

    public static ResInfo build() {
        return new ResInfo();
    }

    public boolean isForceText() {
        return isForceText;
    }

    public ResInfo setForceText(boolean forceText) {
        this.isForceText = forceText;
        return this;
    }

    public String getBriefText() {
        return briefText;
    }

    public ResInfo setBriefText(String briefText) {
        this.briefText = briefText;
        return this;
    }

    public int getTextColor() {
        return textColor;
    }

    public ResInfo setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public int getBgColor() {
        return bgColor;
    }

    public ResInfo setBgColor(int bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public ResInfo setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ResInfo setUrl(String url) {
        this.url = url;
        return this;
    }
}
