
## 6、图片上实现水波纹效果
添加外层CardView
给其设置foreground属性，
注意要给CardView设置点击事件，并且设置clickable="true"

```
<!--CardView就需要使用foreground属性设置点击的波纹效果。-->
<android.support.v7.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/album_card"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:cardCornerRadius="0dp"
    android:clickable="true"
    android:foreground="@drawable/selector_green_button"
    app:cardElevation="@dimen/cardview_default_elevation">
```

注意兼容4.4的版本，写两个selector_green_button文件

```
<?xml version="1.0" encoding="utf-8"?><!--<ripple xmlns:android="http://schemas.android.com/apk/res/android"--><!--android:color="@color/deep_green">--><!--<item android:drawable="@color/green" />&lt;!&ndash;这里是扩散水波纹的色值&ndash;&gt;--><!--&lt;!&ndash;<item android:id="@android:id/mask" android:drawable="@color/white" />&ndash;&gt;--><!--</ripple>-->
<!-- v21 drawable-->
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:color="@color/md_grey_500"
    tools:targetApi="lollipop">
    <item
        android:id="@android:id/mask"
        android:drawable="@color/md_amber_500" />
</ripple>
```


```
<?xml version="1.0" encoding="utf-8"?>
<!--drawable-->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@color/deep_green" android:state_pressed="true" />
    <item android:drawable="@color/green" />
</selector>
```
## 5、判断scrollview下拉上滑
自定义scrollView
重写onTouchEvent方法
自定义接口OnScrollListener

```
boolean isFirstTouch = true;
int lastY;
OnScrollListener onScrollListener;

public interface OnScrollListener {
/**
* @param type 1  下拉 0上滑
*/
void onScrollY(int type);
}

@Override
public boolean onTouchEvent(MotionEvent ev) {
   int y = (int) ev.getRawY();
   int deltaY = y - lastY;
   switch (ev.getAction()) {
       case MotionEvent.ACTION_DOWN:
           break;
       case MotionEvent.ACTION_MOVE:
           if (isFirstTouch) {
               deltaY = 0;
               isFirstTouch = false;
           }
           if (deltaY > 0) {
               Log.i("BBBB", "下拉---");
               //下拉
               if (onScrollListener != null) {
                   onScrollListener.onScrollY(1);
               }
           } else if (deltaY < 0) {
               //上滑
               Log.i("BBBB", "上滑---");
               if (onScrollListener != null) {
                   onScrollListener.onScrollY(0);
               }
           }
           break;
       case MotionEvent.ACTION_UP:
       case MotionEvent.ACTION_CANCEL:
           Log.i("BBBB", "ACTION_UP---ACTION_CANCEL");
           isFirstTouch = true;
           break;
   }
   lastY = y;

   return super.onTouchEvent(ev);
}

```

## 4、通过代码让图片变色
以下代码可更改R.drawable.ic_scrollbar的颜色，

```
//得到drawable对象
//方法一
Drawable drawable;
try {
drawable = VectorDrawableCompat.create(this.getResources(), R.drawable.ic_scrollbar, null);
} catch (Resources.NotFoundException e) {
drawable = ContextCompat.getDrawable(this, R.drawable.ic_scrollbar);
}
//方法二 getDrawable要求api21以上
drawable=getDrawable(R.mipmap.ic_launcher);
```

### setColorFilter
```
//setColorFilter以后样式直接发生变化，以下两种方法亲测在有的手机上不适用
//方法一
drawable.setColorFilter(Color.parseColor("#3F51B5"), PorterDuff.Mode.SRC_ATOP);
//方法二
PorterDuffColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATO);
drawable.setColorFilter(filter);
```

###  SetTint
```
//将wrapDrawable给相应的view设置上
Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
//方法一
DrawableCompat.setTint(wrapDrawable, Color.parseColor("#ffaadd"));
//方法二 setTint要求api21以上
wrapDrawable.setTint(Color.parseColor("#ffaadd"));
btn2.setBackgroundDrawable(wrapDrawable);
```

## 3、scrollView的scrollbar样式
在ListView/ScrollView/RecyclerView中添加属性：

```
<!-- 情况A ：垂直滚动条-->
android:scrollbars="vertical"
android:scrollbarTrackVertical="@drawable/xxx_vertical_track"
android:scrollbarThumbVertical="@drawable/xxx_vertical_thumb"
<!-- 情况B ：水平滚动条-->
android:scrollbars="horizontal"
android:scrollbarTrackHorizontal="@drawable/xxx_horizontal_track"
android:scrollbarThumbHorizontal="@drawable/xxx_horizontal_thumb"

<!-- 其他通用的属性 -->
<!-- 1.定义滚动条的样式和位置 -->
android:scrollbarStyle="outsideInset"
<!-- 2.定义滚动条的大小，垂直时指宽度，水平时指高度 -->
android:scrollbarSize="4dp"
```

| 属性 | 效果 |
| :-: | :-: |
| scrollbarThumbVertical[Horizontal] | 短条 |
| scrollbarTrackVertical[Horizontal] | 长条，即背景 |
其中，scrollbaTrackxxx、scrollbarThumbxxx可以使用：

* Shape自定义 Drawable
* 图片
* .9.png
* @color/xxx的方式使用颜色值
* 不可以直接使用#xxxxxx颜色值

参考链接
[Android必知必会-自定义Scrollbar样式](http://likfe.com/2016/05/16/diyScrollbar/)

#### android:scrollbarStyle
| 属性值 | 效果|
| :-: | :-: |
| insideOverlay | 默认值，表示在padding区域内并且覆盖在view上|
| insideInset	| 表示在padding区域内并且插入在view后面
| outsideOverlay	| 表示在padding区域外并且覆盖在view上
| outsideInset| 	表示在padding区域外并且插入在view后面
## 2、ripple

>ripple是5.0以上才能显出水波纹效果，4.4暂不支持，显示的是selector样式

```
 <Button
        android:id="@+id/btn2"
        android:layout_below="@id/btn1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Button2"
        android:layout_marginTop="10dp"
        style="@style/GreenButton"
       />
```
```
 <style name="GreenButton">
        <item name="android:layout_width">match_parent</item>
        <item name="android:layout_height">60dp</item>
        <item name="android:background">@drawable/selector_green_button</item>
        <item name="android:textColor">@color/white</item>
        <item name="android:textSize">27sp</item>
    </style>

```

> drawable和drawable-v21下新建selector_green_button文件分别为：

```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:drawable="@color/deep_green" android:state_pressed="true" />

    <item android:drawable="@color/green" />
</selector>
```
```
<?xml version="1.0" encoding="utf-8"?>
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="@color/deep_green">
    <item android:drawable="@color/green" />
    <!--<item android:id="@android:id/mask" android:drawable="@color/white" />-->
</ripple>
```


## 1、热更新
### 常见方案
微信开源：[Tinker](https://github.com/Tencent/tinker)
大众点评：[Nuwa](https://github.com/jasonross/Nuwa)
阿里巴巴：[Dexposed](https://github.com/alibaba/dexposed)
阿里巴巴：[AndFix](https://github.com/alibaba/AndFix)
美团：[Robust](http://tech.meituan.com/android_robust.html)
QQ空间：[qq](https://zhuanlan.zhihu.com/p/20308548?columnSlug=magilu)
### 原理
* Android使用的是PathClassLoader作为其类的加载器
* 一个ClassLoader可以包含多个dex文件，每个dex文件是一个Element，多个dex排列成一个有序的dexElements数组
* 当找类的时候会遍历dexElements数组，从dex文件中找类，找到则返回，否则继续下一个dex文件查找
* 热补丁的方案，其实就是将有问题的类单独打包成一个dex文件（如：patch.dex），然后将这个dex插入到dexElements数组的最前面去。
### 热修复的基本原理并不多，目前已知可用的热修复实现的原理主要有以下几种：

1. 基于 Xposed 实现的无侵入的运行时 AOP (Aspect-oriented Programming)  框架，可以实现在线修复 Bug，修复粒度方法级别，但是由于对 ART 虚拟机不支持，导致其对 Android 5.0、6.0 均不支持，使用局限性太大。目前基于这一原理实现的解决方案是手淘团队开源的 Dexposed项目。
2. native hook 方式，其核心部分在 JNI 层对方法进行替换，替换有问题的方法，修复粒度方法级别，无法在类中新增和删减字段，可以做到即时生效，该原理的实现方案主要是阿里团队开源的 AndFix 。
3. 该原理由 QQ 空间技术团队提出，使用新的 ClassLoader 加载 patch.dex，hack 默认的 ClassLoader，替换有问题的类，修复粒度类级别，一般无法做到即时生效，需要在应用下一次启动时生效。目前基于该原理实现的方案有 Nuwa、HotFix、RocooFix 。
4. dex 文件全量替换，基于 DexDiff 技术，对比修复前后的 dex 文件，生成 patch.dex，再根据 patch.dex 更新有问题的 dex 文件。该方案由微信团队提出：微信Android热补丁实践演进之路，暂时还未开源。目前基于这一原理实现的开源方案只有一个：Tinker_imitator 。

## 开源的热修复方案对比
#####Dexposed
该项目明确表示对 ART 虚拟机的不支持，对于 5.1 和 6.0 系统都没法支持，因此该项目基本没有实际应用到项目的意义，毕竟现在 5.0 以上的份额也挺大了。

* 作者：手淘团队
* 修复粒度：方法级别
* 实现原理：基于 Xposed 实现的无侵入的运行时 AOP 框架

##### AndFix
目前阿里百川公测的 阿里百川-HotFix 服务应该就是基于 AndFix 技术，具体的使用细节可以看这篇 阿里百川 HotFix Android 接入说明 ，可以看到其具体的限制基本和 AndFix 项目类似：
HotFix 的使用中不被允许的情况

* 暂时不支持新增方法、新增类
* 不支持新增 Field
* 不支持针对同一个方法的多次 patch，如果客户端已经有一个 patch 包在运行，则下一个 patch 不会立即生效。
* 三星 note3、S4、S5 的 5.0 设备以及 X8 6设备不支持（点击查看具体支持的机型）
* 参数包括：long、double、float 的方法不能被 patch
* 被反射调用的方法不能被 patch
* 使用 Annotation 的类不能 patch
* 参数超过 8 的方法不能被 patch
* 泛型参数的方法如果 patch 存在兼容性问题
* 作者：阿里技术团队
* 修复粒度：方法级别
* 实现原理：native hook 方式
* 优点：运行时即可修复，修复及时
* 缺点：
* 只能修复方法，无法新加类和字段
* 对部分机型不支持
* 方法的参数类型有限制
* 打补丁限制较多，以上的限制在打补丁时均需要注意

##### Nuwa

该项目在去年刚出现时应该算比较火热，但是由于存在的兼容性问题，让作者也渐渐放弃了该项目，目前来说将该方案应用到项目中是有一定风险的。

* 作者： Jason Ross
* 修复粒度：类级别
* 实现原理：ClassLoader 方式
* 优点：兼容性较好，补丁限制较少，类级别的可以增减少字段，补丁自动化做的很完整
* 缺点：
* 需要在应用重启后才能应用补丁，实现修复
* 需要在每个类默认构造方法插入一段代码，防止类打上 CLASS_ISPREVERIFIED 标志，对运行效率有影响
* 目前 issue 中反馈的兼容性问题较多，源码中确实未对各个 Android 版本做差异化处理，存在兼容性问题
* 作者已经停止维护

##### HotFix

基于 ClassLoader 方式实现，实际使用存在兼容问题，基本类似 Nuwa ，作者已弃坑，新开项目 RocooFix，该项目停止维护。

    * 作者：dodola
    * 修复粒度：类级别
    * 实现原理：ClassLoader 方式

##### RocooFix

总体来说，该开源方案应该是算比较完整的解决方案，作者目前还在维护，对各个 Android 版本的兼容性也做了不少工作，期待作者的后续更新。

* 作者：dodola
* 修复粒度：类级别
* 实现原理：ClassLoader 方式
* 优点：
* 兼容性较好，源码中对各 Android 进行了差异化处理，一定程度上解决了兼容性问题
* 实现了两种修复方式：静态修复和动态修复，分别是需要重启修复和无需重启即可修复
* 简化了补丁制作流程
* 缺点：
* 需要在每个类默认构造方法插入一段代码（也叫做插桩），防止类打上 CLASS_ISPREVERIFIED 标志，对运行效率有影响
* 目前就项目下的 issue 来看，还是会存在兼容性问题，对于采用了 APT 技术的项目也存在一些问题
* 动态修复方式还有待检验，使用的是 Legend 项目中的相关技术

##### Tinker_imitator
总体来说，该方案目前还停留在 demo 状态，感觉离实际应用到项目中还需要一段时间，基于 dex 文件全量替换的方式我们更多还是期待微信团队的开源。

* 作者：zzz40500
* 修复粒度：dex 级别
* 实现原理：dex 文件全量替换
* 优点：基于 dex 文件全量替换的实现原理相对于 ClassLoader 方式，在性能上有很大优势
* 缺点：
* 该方案虽然类似微信提出的热修复解决方案，但是 patch.dex 文件的生成并不是依赖于 DexDiff 算法，而是基于 bsdiff ，所以并不是完整实现了微信提出的方案
* 需要重启应用，下次启动时生效
* 生成新的 dex 文件时内存占用较大









