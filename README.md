## JPlayer 
>一个Material Design 风格的 Android 音乐播放器

### 整体效果
>![demo](http://7xlo4n.com1.z0.glb.clouddn.com/demo_JPlayer.gif)

## JPlayer （锦瑟）
>一款 Android 音乐播放器，尝试采用 Material Design 风格设计，力求呈现出简洁大方的用户体验。程序会联网更新专辑及歌手数据，动态收缩或展示次要信息，背景及标题栏会随着专辑封面改变颜色，可滑动切歌，缺省专辑封面会以首字符配上随机鲜亮背景色填充。

### 整体效果
>![demo](http://7xlo4n.com1.z0.glb.clouddn.com/demo_JPlayer.gif)

### 功能及特色
- 联网更新专辑封面及歌手信息（程序会尝试从 Last.fm 下载并更新专辑封面及歌手图片和信息，网络请求使用volley，Json解析使用Gson，图片加载使用Glide）
- 缺省专辑封面填充（缺省专辑封面会以首字符配上随机鲜亮背景色填充）
- 上划隐藏标题栏同时显示播放控制栏，反之下滑则隐藏播放控制栏显示标题栏（CoordinatorLayout配合AppBarLayout设置layout_scrollFlags及listview设置layout_behavior实现）
- 专辑信息背景色取专辑封面里的鲜亮色，保证展示效果多彩且和谐（通过palette实现）
- 沉浸式歌手详情界面，上划隐藏歌手图片以展示更多信息（CoordinatorLayout配合CollapsingToolbarLayout实现）
- 沉浸式音乐播放界面，上划隐藏专辑封面并打开播放列表（实现方式同上）
- 滑动切歌（ViewPager实现）
- 歌词展示（测试中，从歌词迷下载歌词数据并加载在CollapsingToolbar的ExpandedTitle上）
- 桌面播放控件（测试中）

### 界面详情

- 歌曲列表
>![歌曲列表](http://7xlo4n.com1.z0.glb.clouddn.com/%E6%AD%8C%E6%9B%B2%E5%88%97%E8%A1%A8.jpg)

- 歌手列表
>![歌手列表](http://7xlo4n.com1.z0.glb.clouddn.com/%E6%AD%8C%E6%89%8B%E5%88%97%E8%A1%A82.jpg)

- 歌手详情
>![歌手界面](http://7xlo4n.com1.z0.glb.clouddn.com/%E6%AD%8C%E6%89%8B%E7%95%8C%E9%9D%A2.jpg)

- 专辑列表
>![专辑列表](http://7xlo4n.com1.z0.glb.clouddn.com/%E4%B8%93%E8%BE%91%E5%88%97%E8%A1%A82.jpg)

- 专辑详情
>![专辑界面](http://7xlo4n.com1.z0.glb.clouddn.com/%E4%B8%93%E8%BE%91%E7%95%8C%E9%9D%A2.jpg)

- 播放界面
>![播放界面](http://7xlo4n.com1.z0.glb.clouddn.com/%E6%92%AD%E6%94%BE%E7%95%8C%E9%9D%A2.jpg)

- 播放列表
>![播放列表](http://7xlo4n.com1.z0.glb.clouddn.com/%E6%92%AD%E6%94%BE%E5%88%97%E8%A1%A8.jpg)




