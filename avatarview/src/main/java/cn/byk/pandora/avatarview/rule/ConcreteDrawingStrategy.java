package cn.byk.pandora.avatarview.rule;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextPaint;

import cn.byk.pandora.avatarview.AvatarView;
import cn.byk.pandora.avatarview.bean.DisplayInfo;
import cn.byk.pandora.avatarview.bean.ResInfo;
import cn.byk.pandora.avatarview.layout.ILayoutManager;
import cn.byk.pandora.avatarview.util.GraphsMan;

/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午6:06
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :   具体内部元素的显示实现.
 * 实现效果: 圆形头像.  当控件需要展示多张图片为QQ群组元素样式
 */
public class ConcreteDrawingStrategy implements IDrawingStrategy {

    /** qq群组的不同数量时的对应旋转数组 **/
    private static final float[][] ROTATIONS = {new float[]{360.0f}, new float[]{45.0f, 360.0f},
                                                new float[]{120.0f, 0.0f, -120.0f},
                                                new float[]{90.0f, 179.0f, -90.0f, 0.0f},
                                                new float[]{144.0f, 72.0f, 0.0f, -72.0f, -144.0f},};

    /**
     * 默认两张图片间隔距离系数
     */
    private float mSpacing = 0.15f;

    /**
     * 控制是开启qq群组图片两张图片重叠的效果
     */
    private boolean mIsPicRotate = true;

    private float mBorderWidth;

    private final Paint mPaint;
    /**
     * 描边画笔
     */
    private final Paint mBorderPaint;

    /**
     * 文字画笔
     */
    private final Paint mTextPaint;

    private boolean mIsDrawText;

    public ConcreteDrawingStrategy() {
        // 创建内容画笔和描边画笔 并设置属性
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(1);
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setAntiAlias(true);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void algorithm(Canvas canvas, int childTotal, int curChild, ResInfo res, DisplayInfo info) {
        int display = info.displayType;            // 显示的类型

        ILayoutManager.LayoutInfoGroup layoutInfoGroup = info.coordinates.get(curChild - 1);
        float maxHeight = layoutInfoGroup.innerHeight;
        float maxWidth = layoutInfoGroup.innerWidth;

        // 对描边进行边界的最大长度进行判断 不得超过半径的1/6
        float borderLimit = maxWidth > maxHeight ? maxHeight : maxWidth;
        if (mBorderWidth * 6 > borderLimit) {
            mBorderWidth = borderLimit / 6;
        }
        // 描边宽度
        mBorderWidth = info.borderWidth;
        mBorderPaint.setColor(info.borderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        Bitmap newBitmap = null;
        Bitmap opeBitmap = res.getBitmap();
        mIsDrawText = (res.isForceText() || opeBitmap == null);
        if (mIsDrawText) {
            mPaint.setColor(res.getBgColor());
            mTextPaint.setColor(res.getTextColor());
            mIsPicRotate = false;
        } else {
            int mBitmapWidth = opeBitmap.getWidth();   // 需要处理的bitmap宽度和高度
            int mBitmapHeight = opeBitmap.getHeight();
            // 布局调整
            int dx = 0;
            int dy = 0;
            float scale;
            if (mBitmapWidth > mBitmapHeight) {
                scale = maxHeight / (float) mBitmapHeight;
                dx = ((mBitmapWidth - mBitmapHeight) / 2);
            } else if (mBitmapHeight > mBitmapWidth) {
                scale = maxWidth / (float) mBitmapWidth;
                dy = ((mBitmapHeight - mBitmapWidth) / 2);
            } else {
                scale = maxHeight / (float) mBitmapHeight;
            }

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            // 缩放,  通过偏移的dx, dy先对bitmap进行多余部分的过滤摘除, 可以尽可能生成小的bitmap.
            newBitmap = Bitmap.createBitmap(opeBitmap, dx < 0 ? -dx : dx, dy < 0 ? -dy : dy,
                                            (opeBitmap.getWidth() + (dx < 0 ? dx : -dx)),
                                            (opeBitmap.getHeight() + (dy < 0 ? dy : -dy)), matrix, true);
        }

        // 正式开始绘制
        canvas.save();
        adjustMaskBitmapDisplay(canvas, res, newBitmap, (int) maxWidth, (int) maxHeight,
                                childTotal > 5 ? 360 : ROTATIONS[childTotal - 1][curChild - 1], mSpacing, mIsPicRotate,
                                display);
        canvas.restore();
    }

    private void adjustMaskBitmapDisplay(Canvas canvas, ResInfo res, Bitmap bitmap, int viewBoxW, int viewBoxH,
                                         float rotation, float gapSize, boolean isRotate, int displayType) {
        // 取最小值的中间值
        int center;
        if (viewBoxH >= viewBoxW) {
            center = Math.round(viewBoxW / 2f);
        } else {
            center = Math.round(viewBoxH / 2f);
        }

        if (AvatarView.TYPE_CIRCLE == displayType) {
            // qq群组效果
            // 先处理成圆形头像。如果没有旋转规则, 那么对半径进行0.95的缩小, 防止描边丢失
            GraphsMan.drawCircle(canvas, bitmap, center, center, center * 0.98f, mPaint, isRotate ? 0 : mBorderWidth,
                                 mBorderPaint);

            if (isRotate && rotation != 360f) {
                Matrix matrix = new Matrix();
                // 根据原图的中心位置旋转
                matrix.setRotate(rotation, viewBoxW / 2, viewBoxH / 2);
                canvas.setMatrix(matrix);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawCircle(viewBoxW * (1.5f - gapSize), center, center, mPaint);
                mPaint.setXfermode(null);
            }
        } else if (AvatarView.TYPE_RECT == displayType) {
            // 原图头像
            GraphsMan.drawRect(canvas, bitmap, viewBoxW, viewBoxH, 0, 0, mPaint, mBorderWidth, mBorderPaint);
        } else if (AvatarView.TYPE_OVAL == displayType) {
            // 椭圆头像
            GraphsMan.drawOval(canvas, bitmap,
                               new RectF(viewBoxW * 0.05f, viewBoxH * 0.2f, viewBoxW * 0.95f, viewBoxH * 0.8f), 0, 0,
                               mPaint, mBorderWidth, mBorderPaint);
        } else if (AvatarView.TYPE_STAR_FRAME == displayType) {
            // 五角星头像
            GraphsMan.drawStarFrame(canvas, bitmap, (int) (center * 0.9f), 0, 0, mPaint, mBorderWidth, mBorderPaint);
        } else if (AvatarView.TYPE_ROUND_RECT == displayType) {
            // 有圆角的头像
            GraphsMan.drawCornerRectBorder(canvas, bitmap, viewBoxW, viewBoxH, viewBoxW / 8, viewBoxW / 8, 0, 0, mPaint,
                                           mBorderWidth, mBorderPaint);
        }

        if (mIsDrawText) {
            mTextPaint.setTextSize(viewBoxH * 0.4f);
            GraphsMan.drawText(canvas, res.getBriefText(), center, center, mTextPaint);
        }
    }

    /**
     * 设置两张图片的间距
     *
     * @param spacingQuality 接收处理范围 0~2 ; 2的时候空隙为最大, 0的时候会重叠. 默认为1
     */
    public void setSpacing(float spacingQuality) {
        if (spacingQuality > 2) {
            spacingQuality = 2;
        } else if (spacingQuality < 0) {
            spacingQuality = 0;
        }

        mSpacing *= spacingQuality;
    }

    public boolean isPicRotate() {
        return mIsPicRotate;
    }

    /**
     * 设置qq群组图片去除重叠方法
     *
     * @param mIsPicRotate 为true时, 为QQ群组的样式, 默认属性
     *                     为false时: 可去除两个图片重叠确实的效果
     */
    public void setIsPicRotate(boolean mIsPicRotate) {
        this.mIsPicRotate = mIsPicRotate;
    }

    public float getSpacingQuality() {
        return Math.round((mSpacing / 0.15f) * 100) / 100;
    }

}
