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


### 兼容超强的BaseAdapter[BaseRecyclerViewAdapterHelper]( https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
---

#### 普通RecyclerView item.
 ![image](https://github.com/qdxxxx/SwipeMenuContainer/blob/master/appGif/normal.gif)
 
 ##### 多种itemType
  ![image](https://github.com/qdxxxx/SwipeMenuContainer/blob/master/appGif/mulit.gif)
  
#### 长按拖拽
需要设置```enableParentLongClick(tren)```允许父类拖拽.

   ![image](https://github.com/qdxxxx/SwipeMenuContainer/blob/master/appGif/drag.gif)
   
#### 多级item
   ![image](https://github.com/qdxxxx/SwipeMenuContainer/blob/master/appGif/expandable.gif)
 
 

### 方法及属性介绍
---

name                   | format         |中文解释
----                   |------          |----
setDrawByTouch         | boolean  	|触摸显示的字母圆的y轴中心点与手指触摸相同
setCircleRadius        | float    	|触摸显示的字母圆的半径
setCircleDuration      | integer   	|触摸显示的字母圆显示时间
setCircleColor         | integer	|圆的颜色
setIndexBarWidth       | integer 	|字母导航栏的宽度
setIndexBarHeightRatio | float          |字母导航栏高度相对父高度占比
getIndexBar	       |                |获取字母导航栏

<br/>




###  License
---

```
MIT License

Copyright (c) 2017 qdx

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```



