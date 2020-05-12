# # StyleBook
> 北京理工大学计算机学院《Android技术开发基础》课程设计

1. App的运行与开发环境
	* 运行环境： Android8.0（oreo）以上版本的Android手机
	* 部署方法： 直接安装stylebook.apk即可
	* 开发环境： Android Studio 3.4.1
	* 代码行数： 约4900行（Java）
2. App功能说明
> StyleBook是一款符合Material Design标准，可以帮助用户管理衣柜，收集不同着装搭配图片的生活管理应用。具有天气查看、图片管理等功能,其实现功能如下:

* 主界面:可添加新单品到衣柜,查看实时天气、查看为不同日期设定的搭配、应用内置的搭配建议
	* 右上模块-添加按钮:点击进入单品添加界面（添加页面详情见page2）
	* 左上模块-天气卡片
		* 当前定位
		* 当前日期时间
		* 实时天气情况、温度
		* 未来三天天气预报
		* 点击卡片左上角刷新按钮可更新
	* 右下模块-搭配日历卡片: 选择日期可在上方小图展示用户为当日选定的搭配，点击可跳转到计划页（见page3）
	* 左下模块-推荐卡片: 随机展示当季搭配示范，点击可更换
* 衣柜界面:瀑布流展示当前衣柜中所有分类，应用内置十个种类（当前不支持自由增加）: ~Coat Sweater Hoodie Shirt Tshirt Dress Skirt Jeans Trouser Shorts~
> *点击各分类可进入对应的分类子界面*
> *点击右下角添加按钮可进入添加单品*
	* ←各分类子界面，显示该分类已有的衣物列表以卫衣类为例，进入后显示该用户已添加的五件不同的卫衣）
	* 点击右下角的添加按钮可进入添加界面
	* ←继续以卫衣类为例，点击该类中第二件卫衣，可进入该衣物的详细信息页面。右上角依次为返回主页和删除按钮；右下角为编辑悬浮按钮，点击可编辑该衣服详细信息
	* ←衣物添加界面和编辑界面较为类似，可以添加不同衣物。右上角为返回首页按钮，以及更多选项按钮（编辑界面为删除按钮）
	* 衣物目前支持八个维度的信息（暂时不支持扩展）：
		* ←填写品牌
		* ←填写材质
		* ←通过颜色选择器选择颜色,颜色选择器有两种模式，预设和自定义，可调整透明度
		* ←选择季节
		* ←选择购入日期 
		* ←选择种类
		* ←选择衣物适宜温度（1～8），为后续添加的根据气温推荐合适衣物而设置
		* ←点击按钮可从相册拍照或添加照片
		* 图片选择完后下方出现预览
		* ←编辑或添加以后点击右下角完成按钮，弹出修改/保存确认框
		* ←确认后刷新，Tshirt类下出现一个条目
* 计划界面：上方为今日搭配预览，下方为日历，点击日历的不同天可以查询到相关的衣物
	* ←点击下拉条，展示所有用户添加的的搭配卡片，目前仅支持10个look，后续加入自由添加按钮
	* 点击不同搭配卡片可进入其设置界面
	* ←搭配设置界面，左侧框为搭配预览，右侧框为链接的单品，下侧为可替代的单品。
	* 点击左侧图片可选择相册中的照片替代当前搭配图片
	* 点击右侧和下侧的各单品框可进入选择衣柜中的单品进行替换

3. App架构设计及技术实现方案
	1. 本应用程序未涉及算法，图片压缩算法使用Github项目zelory；
	2. 安卓应用不适用于MVC模式，故本应用使用MVP模式架构，但在实际开发过程中并未完全按照标准进行，本应用中各类之间的关系如下图：
	> Model-放置控制器，向View传递数据，包括database包和adapter包
	> View-放置界面控件：所有的Activity类和Fragment类
	> Presenter-View类和Model类的桥梁：util包用于存放工具类，gson包中的类用于解析API返回的数据，service包中的类用于提供刷新服务
	3. Model中提供stylebook的所有控制器，各个类提供的接口如下
	4. View中提供stylebook的所有界面控件的类，各个类提供的接口如下
	5. 程序导入两个外部sdk工具：
		* BaiduLBS_Android.jar用于提供定位；
		* sdk_HeWeather_Public_Android_V3.0.jar用于提供天气数据
	6. 除去为程序建立Material Design交互界面设计标准程序引用的几个界面控件库外，程序共引用6个外部框架/库
		1. [glide库：安全的将占用内存过大的图片加载到控件](com.github.bumptech.glide:glide:4.6.1)
		2. [quick adapter框架：快速为RecycleView建立绑定容器](com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.22 )
		3. [Litepal：数据库管理支持](org.litepal.android:core:2.0.0)
		4. [色彩选择器](com.jrummyapps:colorpicker:2.1.6)
		5. [图片压缩算法库](id.zelory:compressor:2.1.0)
		6. [okHttp：网络框架](com.squareup.okhttp3:okhttp:3.10.0)
4. 技术亮点、技术难点及其解决方案,本程序的特别点：
* 本App作为生活管理应用，本身属于较为小众的App需求。而比之同类的App，大多仅仅侧重于衣物管理，本App：
	1. 加入与日历绑定的搭配管理，用户可根据需要安排某日的搭配；
	2. 加入温度元素，可以根据“26℃穿衣法则”，结合用户为每件衣服设置的温度数值，得到每套搭配对应的温度，最终通过获取的每日气温向用户推送库中最适合当日的搭配；
	3. 符合Material Design设计理念，注重交互界面的美观
* 本程序的亮点：
	* 主页的导航以及界面跳转动画
	* Closet的瀑布流和横纵向绑定滑动列表（两个RecycleView的绑定滑动）
	* 通过压缩算法将用户输入的图片导入到数据库
* 遇到的困难以及解决方案：
	1. activity和fragment结构混乱引起的程序闪退
	> 解决方法：仔细阅读log并找到引起闪退的原因，闪退问题大多可通过抛出异常解决；
	2. bottom_navigation遮挡fragment：
	> 解决方法：上网查阅，网络建议利用layout_above等属性或添加View控件，并未解决问题，最后尝试使用margin属性并定义具体数值解决；
	3. widget7和support库的冲突：
	> 解决方法：小心选择；
	4. collaspingToolbar.setText不起作用：
	> 解决方法：上网查阅，在xml界面文件输入以下代码解决
	`app:expandedTitleTextAppearance=“@android:color/transparent”`
	5. cardview设置点击事件监听器不起作用：
	> 解决方法：上网查阅，使用网上所说的在xml文件设置clickable(true)并未起作用，最终未解决，在其他控件上设置了监听器；
	6. 系统原生谷歌定位api信号弱
	> 解决方法：申请百度api，加入百度定位sdk，目前暂时使用直接设置地区为北京的方法
	7. 坑：尽量不要使用内置的Color类和Calendar类，会遇到很多转换的麻烦，直接用int和String存数据比什么都好
	8. 将用户从相册导入的图片转成bitmap存到SQLite数据库时应用卡住，死机，反复闪退：
	> 解决方法：上网查阅，是图片太大的原因，在GitHub找到图片压缩库zelory并使用
	9. EditText在实际运行中会导致键盘无法收起：
	> 解决方法：上网查阅，可以为EditText设置监听器onForcus并在其中设置失去焦点时是键盘收起的方法。想法是美好的，现实是残酷的，实际运行中仍然存在问题，真机运行中输入法键盘会自带收起按钮遂放弃解决该问题
	10. 多级Activity返回时逻辑混乱：
	> 解决方法：重读Activity部分教程，使用singleTask模式
	11. Litepal反复报错 find no column并闪退：
	> 解决方法：Litepal有两个坑，其一是Litepal 2.0用LitePal方法取代原先的DataSupport方法，同时又存在LitePalSupport方法，容易导致使用错误；其二是若在数据库未找到符合条件的对象，会返回空对象，设置一个Default量并抛出异常可解决闪退；
	12. 没有通过图片id获得图片文件的方法，而使用的图片压缩库要求传入文件：
	> 解决方法：使用bitmap转file或者File方法通过uri获得file或者拆解图片压缩库源码；
5. 简要开发过程
```
4.27	设计初版程序界面，初版应用以天气界面为主，衣柜管理界面辅助插入
4.28	建立Git资源ClosetWeather衣柜天气，在Android Studio中新建项目
	https://github.com/Anne416wu/ClosetWeather
	复现《第一行代码》第二版中的实例项目《酷欧天气》
	编写xml页面文件，加入DrawLayout和BottomNavigation
	新建db包中的实体类City、Province、County
4.29	编写Gson包中文件，实现从Gullin.com获得城市列表
	编写service包中文件
	编写util包中文件，利用okHttp搭建网络框架
4.30-5.5 五一出游，暂停开发
5.6	编写MainActivity、WeatherActivity和ChooseCityFragment
	实现各部件的响应事件

5.7	复现《酷欧天气》部分功能
	界面逻辑混乱，重构并确认第二版应用StyleBook
	抽取第一版应用中获得天气的功能
	重新设计StyleBook界面，跳转事件，Activity和Fragment关系等
	此版应用将以资源管理作为主要功能
	建立Git资源StyleBook，在Android Studio中新建项目
	https://github.com/Anne416wu/StyleBook
	根据设计建立应用框架，建立MainActivity，三个主界面的Fragment文件



5.8	完成MainActivity和对应的三个Fragment界面框架
	完成HomeFragment的四个主卡片布局
5.9	ClosetAddActivity和对应的xml界面的框架编写
	三种floatingActionBar: Add, Edit, Done和对应事件编写
5.10	ClosetFragment界面框架，各子项对应的ClothAdapter和CardView界面
	ClosetParentActiyity, ClosetChildActivity框架建立
	Cloth对象的数据库建立
5.11	解决存在的Bug，进入ClosetParent界面闪退问题
	ClosetFragment的StaggeredGridLayout瀑布流界面编写
	横向的horizontal RecycleView界面编写，两者的绑定事件
5.13	add_item界面重构，对应ClosetAddActivity各部件绑定
	引入QuickAdapter框架
	ClosetFragment添加ClosetAdapter、ClosetAdapterRound容器
	ClosetParentActivity添加HoodieAdapter容器
	Closet系列功能大致完成
5.14	申请HeWeather SDK key，重构天气数据获取方式
	申请BaiduLBS API key，解决Google GPS定位信号不足问题
	再次重构条目的添加界面，与编辑界面、查看界面三者分离
5.16	完善HomeFragment所有功能：天气卡片功能、日历选择功能、随机搭配搭配
	完善条目的添加界面，spinner菜单，日期选择对话框、颜色选择对话框
	完成调取系统相机、从相册选择图片并预览等功能
5.17	建立LitePal数据库，包含Cloth表和Match表
	添加界面调用save() 编辑界面调用updateAll() 删除按钮调用deleteAll()
	测试数据库
5.18	改变通过uri调取用户添加的图片为向数据库写入Bitmap图片
	导入zelory图片压缩库，解决图片过大程序卡住的Bug
	测试功能
5.19	完善ScheduleFragment，添加动态库支持部分界面的动态隐藏的显示
	完善MatchActivity及界面，完善其调取衣物子类并展示的功能
5.20	对程序进行集成测试
	程序开发工作完毕，编写及整理文档
```
6. 学习感悟及对本课程的建议（可选）
> 	本次学习让我获益匪浅，金老师的课件以及教学都非常有用，直接带我了解了安卓开发中比较核心的内容，同时又不仅仅着局限于很小的一部分上，而是围绕它的方方面面，让我们能对全局有一个着眼而不是把精力花在细枝末节上。课上老师反复的强调了Kotlin的优势，但是实际开发中仍然是使用Java更得心应手一些——网上能查阅的相关资料也更多。
> 	这次大作业让我熟悉了一个安卓应用从无到有的过程，同时我也认识到程序开发需要多人合作的必要性：一个应用程序开发涉及到的各方各面和工作量都庞杂而繁重，从前端到后端。个人能做的事情是有限的。还有在开发一个程序伊始的需求分析和设计非常重要，否则越到后面所得到的程序越加繁杂，结果往往是多次重构代码的痛苦，我也是在最终撰写文档的时候才发现安卓开发中始终遵循设计模式有多么困难——各个类中数据调用和界面维护混杂在一起，写的时候很方便但是最终可读性极差。
> 	安卓程序设计的确是很有趣也容易让人上瘾的事情，本次的作业的提交的程序仍然存在大量的问题和功能等待完善，在日后的时间我会继续更新和维护这个自己一行一行建立的小角落。
