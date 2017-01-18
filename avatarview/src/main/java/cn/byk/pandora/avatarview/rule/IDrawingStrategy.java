package cn.byk.pandora.avatarview.rule;

import android.graphics.Canvas;

import cn.byk.pandora.avatarview.AvatarView;
import cn.byk.pandora.avatarview.bean.DisplayInfo;
import cn.byk.pandora.avatarview.bean.ResInfo;

/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午6:02
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 控件内部图片的显示策略
 */
public interface IDrawingStrategy {

    /**
     * 根据提供的画布, 和可绘制的位置实现具体效果
     *
     * @param canvas     {@link AvatarView#onDraw(Canvas)} 中的画布
     * @param childTotal 图片的总个数
     * @param curChild   当前图片是第几张图片
     * @param res        需要操作的资源
     * @param info       每个内部元素应该摆放的位置信息类
     */
    void algorithm(Canvas canvas, int childTotal, int curChild, ResInfo res, DisplayInfo info);
}
