# Github
一款基于Github API的Github APP。以前都是通过电脑上Github网站，这个还是很不方便；后来网上搜索了下Github的APP,有那么几个，但都没有Github官网的Trending功能。因为有空闲时间，就自己做了一个。一个为了学习，一个为了有一个好的Github移动客户端。可以下载Github.apk体验。

##当前主要功能
1. 登录，直接使用github账号登录
2. 热点仓库，支持多种语言，三个时间段
3. 高级搜索，可以搜索repo和user，提供多种条件来过滤
4. 浏览仓库，可以浏览仓库的readme、contributors、forks、code、issues，支持star
5. 浏览用户，可以查看用户的public repos、star repos、followers、following、gist，支持follow
6. 文件查看，可以直接打开仓库的文件，当前已经支持打开源码、图片、gif，源码还可以保存到本地（后面考虑支持改为文件下载）
7. 浏览仓库的issues，当前可以发表评论，还不能发issue
8. 浏览用户发布的gist，当前可以对gist发表评论和关注，还不能新建gist。

##特性和开源项目
项目中使用了很多的开源项目，在此列举下，方便以后查找和学习。特别感谢众多开发者贡献的开源项目，加快了开发速度，通过他们的代码也让我学到了很多！

- 1、基本遵循Google Material Design风格，[Retrofit2](https://github.com/square/retrofit)+[rxjava](https://github.com/ReactiveX/RxJava)+[okhttp3](https://github.com/square/okhttp)+[dagger2](https://github.com/google/dagger)+MVP框架,使用butterknife注解
1. 2、[Glide](https://github.com/bumptech/glide)加载图片、gif，使用[Android-Iconics](https://github.com/mikepenz/Android-Iconics)提供矢量图标
2. 3、SwipeRefreshLayout+recyclerview + [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)+[recyclerview-flexibledivider](https://github.com/yqritc/RecyclerView-FlexibleDivider)展示数据,下拉刷新，上拉加载
3. 4、NavigationView + DrawerLayout或[materialdrawer](https://github.com/mikepenz/MaterialDrawer),快速实现侧边栏
4. 5、fragment + pageview 实现多页面效果
5. 6、[PagerSlidingTabStrip](https://github.com/astuetz/PagerSlidingTabStrip)实现pageview的页面指示器
6. 7、[BottomBar](https://github.com/roughike/BottomBar)实现底部工具栏，搭配CoordinatorLayout设置behavior可以实现它的隐藏和显示
7. 8、继承[BasePopup](https://github.com/razerdp/BasePopup)实现丰富的弹出框效果
8. 9、[FlycoLabelView](https://github.com/H07000223/FlycoLabelView)实现LabelView
9. 10、[MarkdownView-Android](https://github.com/mukeshsolanki/MarkdownView-Android)显示Markdown文件
10. 11、[highlightjs-android](https://github.com/PDDStudio/highlightjs-android)提供了代码语法的高亮显示
11. 12、[SmoothProgressBar](https://github.com/castorflex/SmoothProgressBar)实现平滑的进度条
12. 13、[logger](https://github.com/orhanobut/logger)提供log的详细打印
13. 14、抓取github的trending页面的数据，使用jsoup解析得到所需的数据
14. 15、部分控件水波纹点击效果的适配
15. 16、开源项目utilcode，提供了大量的工具类
16. 17、[FloatingActionButton](https://github.com/Clans/FloatingActionButton)提供了FloatingActionMenu,实现一组FloatingActionButton的管理
17. 18、CoordinatorLayout+AppBarLayout+CollapsingToolbarLayout实现可折叠toolbar的效果
18. 19、通过Animation、Animator、Transition创建多样动画效果，增加交互性





##效果预览
![](https://github.com/jbm714060/Github/blob/master/screenshot/login.png)![](https://github.com/jbm714060/Github/blob/master/screenshot/repo.png)
![](https://github.com/jbm714060/Github/blob/master/screenshot/repo_code.png)![](https://github.com/jbm714060/Github/blob/master/screenshot/search.png)
![](https://github.com/jbm714060/Github/blob/master/screenshot/trending.png)![](https://github.com/jbm714060/Github/blob/master/screenshot/user.png)