package cn.byk.pandora.avatarview.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by Byk on 2017/1/9.
 * <p>
 * Bitmap Decoder
 */
public class BitmapMan {

    private static final String TAG = BitmapMan.class.getSimpleName();

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(1, 1, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                                             BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 对一个Resources的资源文件进行指定长宽来加载进内存, 并把这个bitmap对象返回
     *
     * @param res       资源文件对象
     * @param resId     要操作的图片id
     * @param reqWidth  最终想要得到bitmap的宽度
     * @param reqHeight 最终想要得到bitmap的高度
     * @return 返回采样之后的bitmap对象
     */
    public static Bitmap decodeFixedSizeForResources(Resources res, int resId, int reqWidth, int reqHeight) {
        // 首先先指定加载的模式 为只是获取资源文件的大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        //Calculate Size  计算要设置的采样率 并把值设置到option上
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 关闭只加载属性模式, 并重新加载的时候传入自定义的options对象
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 一个计算工具类的方法, 传入图片的属性对象和 想要实现的目标大小. 通过计算得到采样值
     * 如果目标大于原图片那么是不进行处理的.
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqHeight == 0 || reqWidth == 0) {
            return 1;
        }

        //Raw height and width of image
        //原始图片的宽高属性
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        // 如果想要实现的宽高比原始图片的宽高小那么就可以计算出采样率, 否则不需要改变采样率
        if (reqWidth < height || reqHeight < width) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;

            // 判断原始长宽的一半是否比目标大小小, 如果小那么增大采样率2倍, 直到出现修改后原始值会比目标值大的时候
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.i(TAG, "进行图片的inSampleSize计算的值: " + inSampleSize);

        return inSampleSize;
    }

}
