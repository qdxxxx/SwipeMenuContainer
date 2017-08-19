# SwipeMenuContainer
[![](https://www.jitpack.io/v/qdxxxx/SwipeMenuContainer.svg)](https://www.jitpack.io/#qdxxxx/SwipeMenuContainer)

仿QQ向左滑动弹出菜单的布局.

 ![image](https://github.com/qdxxxx/SwipeMenuContainer/blob/master/appGif/demo.gif)
 
  ---
 ### Download
 [https://fir.im/6q2m]( https://fir.im/6q2m)
 
 
  ---
### 集成方式

 - 注入依赖
 Step 1. Add the JitPack repository to your build file
 Step 2. Add the dependency
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```
	dependencies {
 	   compile 'com.github.qdxxxx:SwipeMenuContainer:v1.0.1'
	}
```

#### Layout
```
    <qdx.swipemenucontainer.SwipeMenuLayout
        android:layout_width="match_parent"
        android:layout_height="70dp"
        qdx:autoCloseType="smooth"
        qdx:isLeftMenu="false"
        ...
        />
```


#### 兼容超强的BaseAdapter[BaseRecyclerViewAdapterHelper]( https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

##### 普通RecyclerView item.
 ![image](https://github.com/qdxxxx/SwipeMenuContainer/blob/master/appGif/normal.gif)
 
 ##### 多种itemType
  ![image](https://github.com/qdxxxx/SwipeMenuContainer/blob/master/appGif/mulit.gif)
  
##### 长按拖拽
   ![image](https://github.com/qdxxxx/SwipeMenuContainer/blob/master/appGif/drag.gif)
   
##### 多级item
   ![image](https://github.com/qdxxxx/SwipeMenuContainer/blob/master/appGif/expandable.gif)
 




