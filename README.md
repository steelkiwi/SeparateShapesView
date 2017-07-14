# SeparateShapesView

[![Made in SteelKiwi](https://github.com/steelkiwi/SeparateShapesView/blob/master/assets/made_in_steelkiwi.png)](http://steelkiwi.com/blog/)

# Description

Simple custom ViewGroup with two shapes inside and simple scale animation

# View

![Animation](https://github.com/steelkiwi/SeparateShapesView/blob/master/assets/shape_animation_center.gif)
![Animation](https://github.com/steelkiwi/SeparateShapesView/blob/master/assets/shape_animation_left.gif)

# Download

For project API 21+.

## Gradle

# Usage

First of all, need add SeparateShapesView to your xml layout

```xml
<com.steelkiwi.library.SeparateShapesView
    android:id="@+id/view"
    android:layout_width="match_parent"
    android:layout_height="@dimen/view_height"
    android:layout_alignParentBottom="true"
    android:layout_alignParentLeft="true" or android:layout_centerInParent="true" // it will animate view left or center position
    android:layout_marginRight="@dimen/margin_40"
    android:layout_marginLeft="@dimen/margin_40"
    android:layout_marginBottom="@dimen/margin_20"
    app:ssv_all_text_caps="true"
    app:ssv_done_drawable="@drawable/done"
    app:ssv_left_shape_drawable="@drawable/left_rounded_corner_drawable"
    app:ssv_right_shape_drawable="@drawable/right_rounded_corner_drawable"
    app:ssv_left_shape_text="@string/left_title"
    app:ssv_right_shape_text="@string/right_title"
    app:ssv_text_color="@android:color/white"
    app:ssv_text_size="14sp"
    app:ssv_single_shape="false"
    app:ssv_center_shape_text="@string/center_title"/>
```

You can customize view, through this attributes

* app:ssv_all_text_caps - set all text cups
* app:ssv_done_drawable - set icon when view is finish animate
* app:ssv_left_shape_drawable - left shape drawable
* app:ssv_right_shape_drawable - right shape drawable
* app:ssv_left_shape_text - left shape title
* app:ssv_right_shape_text -  right shape title
* app:ssv_text_color - shape text color
* app:ssv_text_size - shape text size
* app:ssv_single_shape - set flag to not divide shapes
* app:ssv_center_shape_text -  shape center text

Inside your client code need to implement listener SeparateShapesView.OnButtonClickListener to handle view click state.
OnButtonClickListener methods:

* boolean onLeftButtonClick()
* boolean onRightButtonClick()
* boolean onMiddleButtonClick() // will call when use ssv_single_shape and ssv_center_shape_text is not null

If any of the method will returns true, view will animate. Otherwise nothing happens

# License

```
Copyright © 2017 SteelKiwi, http://steelkiwi.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```