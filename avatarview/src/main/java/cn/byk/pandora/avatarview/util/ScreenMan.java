package cn.byk.pandora.avatarview.util;

import android.content.Context;

/**
 * Created by Byk on 2017/1/6.
 * <p>
 * Screen Size Trans
 */
public class ScreenMan {

    /**
     * dip转换px
     *
     * @param context context
     * @param dip     dip
     * @return px
     */
    public static float dip2px(Context context, float dip) {
        final float scale = context.getResources()
                                   .getDisplayMetrics().density;
        return Math.round(dip * scale * 100) / 100;
    }

    /**
     * px转换dip
     *
     * @param context context
     * @param px      px
     * @return dip
     */
    public static float px2dip(Context context, float px) {
        final float scale = context.getResources()
                                   .getDisplayMetrics().density;
        return Math.round(px / scale * 100) / 100;
    }

}
