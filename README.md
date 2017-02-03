## AvatarView

[![](https://jitpack.io/v/bluesofy/AvatarView.svg)](https://jitpack.io/#bluesofy/AvatarView)

多头像显示视图，适用于普通用户头像以及群组多头像显示
[README on GitHub](https://github.com/bluesofy/AvatarView/blob/master/README.md)
  
  
### 效果图  Preview
![Review](https://github.com/bluesofy/AvatarView/blob/master/preview/pic.png)


### 使用说明  Usage
- 在Gradle中添加JitPack声明
```gradle
allprojects {
    repositories {
        …
        maven { url 'https://jitpack.io' }
    }
}
```
- 添加引用
```gradle
compile 'com.github.bluesofy:AvatarView:1.1'

compile 'com.github.bumptech.glide:glide:3.7.0'  // Glide库
compile "com.android.support:support-v4:25.1.0"  // v4库
```

- 代码非原创，在项目 [SImageView](https://github.com/suzeyu1992/SImageView) 的基础上修改，原有的使用方法请通过传送门查看
- 修改了资源实体，配置灵活，删除了原有的缓存策略，采用 [Glide](https://github.com/bumptech/glide) 加载，新增了显示文字头像，文字和图片混合显示等方法，类似于钉钉的群组头像显示

```java

// 单张文字头像
mSingleAvatarView.setBriefName("张三", Color.WHITE, Color.parseColor("#7FDAD0"));

// 混合头像
mMixAvatarView.setLayoutManager(new WeChatLayoutManager(this));
mMixAvatarView.setDisplayShape(AvatarView.TYPE_RECT);
mMixAvatarView.setErrPicResID(R.mipmap.ic_launcher);
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

```

- 更多请查看 [Demo](https://github.com/bluesofy/AvatarView/blob/master/app/src/main/java/cn/byk/pandora/avatarview/sample/MainActivity.java)


### 特别鸣谢  Tks to
- [SImageView](https://github.com/suzeyu1992/SImageView)
- [Glide](https://github.com/bumptech/glide)


### 联系方式  Support or Contact
- E-Mail: bluesofy@qq.com
- E-Mail: bluesofy@live.cn
