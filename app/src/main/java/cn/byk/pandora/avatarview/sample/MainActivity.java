package cn.byk.pandora.avatarview.sample;

import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import cn.byk.pandora.avatarview.AvatarView;
import cn.byk.pandora.avatarview.bean.ResInfo;
import cn.byk.pandora.avatarview.layout.QQLayoutManager;
import cn.byk.pandora.avatarview.layout.WeChatLayoutManager;

public class MainActivity extends FragmentActivity {

    private AvatarView mSingleAvatarView;
    private AvatarView mMixAvatarView;
    private AvatarView mOriginAvatarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        // 单张文字头像
        mSingleAvatarView = (AvatarView) findViewById(R.id.view_avatar_single);
        mSingleAvatarView.setBriefName("张三", Color.WHITE, Color.parseColor("#7FDAD0"));

        // 混合头像
        mMixAvatarView = (AvatarView) findViewById(R.id.view_avatar_mix);
        mMixAvatarView.setLayoutManager(new WeChatLayoutManager(this))
                      .setDisplayShape(AvatarView.TYPE_RECT)
                      .setErrPicResID(R.mipmap.ic_launcher);
        List<ResInfo> infos = new ArrayList<>();
        // 文字头像
        infos.add(ResInfo.build()
                         .setForceText(true)
                         .setBriefText("李四")
                         .setTextColor(Color.WHITE)
                         .setBgColor(Color.parseColor("#74CAE3")));
        // 图片加载错误默认显示文字（如果有设置全局错误图片，则加载错误图片）
        infos.add(ResInfo.build()
                         .setUrl("http://wrong")
                         .setBriefText("王五")
                         .setTextColor(Color.WHITE)
                         .setBgColor(Color.parseColor("#9AE897")));
        // 加载错误强制显示文字
        infos.add(ResInfo.build()
                         .setForceText(true)
                         .setUrl("http://wrong_again")
                         .setBriefText("马六")
                         .setTextColor(Color.WHITE)
                         .setBgColor(Color.parseColor("#9AE897")));
        // Url下载图片
        infos.add(ResInfo.build()
                         .setUrl("http://www.3dmgame.com/uploads/allimg/141224/270_141224171322_1.jpg"));
        mMixAvatarView.setMixRes(infos);

        // 原作者的调用方法
        mOriginAvatarView = (AvatarView) findViewById(R.id.view_avatar_origin);
        mOriginAvatarView.setLayoutManager(new QQLayoutManager())
                         .setErrPicResID(R.mipmap.ic_launcher)
                         .setImageUrls("http://www.tshseo.com/uploads/allimg/141024/2234236415-0.jpg",
                                       "http://www.3dmgame.com/uploads/allimg/141224/270_141224171322_1.jpg",
                                       "https://images.plurk.com/f76339fbc1eb3a1381b811b2879ac8e3.jpg",
                                       "http://www.3dmgame.com/uploads/allimg/141224/270_141224171322_1.jpg");
    }
}
