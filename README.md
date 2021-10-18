# UniversalPickerDialog
HMOS 3rd party library to make implementing Dialog more easier. It includes two abilities :
1. Single Picker 
2. Multi picker

### Screenshots
---
![Screenshots](https://github.com/prasanta352/UniversalPickerDialog-1/blob/main/images/all.png?raw=true)


### Source
---
This library has been inspired by [stfalcon-studio/UniversalPickerDialog](https://github.com/stfalcon-studio/UniversalPickerDialog)

### Integration
---
1. For using UniversalPickerDialog module in sample app, include the source code and add the below dependencies in entry/build.gradle to generate hap/support.har.
    ```groovy
    implementation project(path: ':universalpickerdialog')
    ```
2. For using UniversalPickerDialog module in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.
    ```groovy
   implementation fileTree(dir: 'libs', include: ['*.har'])
   ```
### Usages
---
implement callback interfaces:
```java
public class MainAbilitySlice extends AbilitySlice implements ListContainer.ItemClickedListener,
        UniversalPickerDialog.OnPickListener {
```

Then implement OnPickListener.onPick(int[], int) method:
```java
@Override
public void onPick(int[] selectedValues, int key) {
    String str = list.get(selectedValues[0]);
    Object obj = array[selectedValues[0]];

    /*do some logic*/
}
```

Now you can build the dialog and show it. Just add these few lines:

```java
new UniversalPickerDialog.Builder(this)
                .setTitle("UniversalPickerDialog")
                .setListener(this)
                .setInputs(
                        new UniversalPickerDialog.Input(0, list),
                        new UniversalPickerDialog.Input(2, array)
                )
                .show();
```

Data set is passing to Picker using Input class that supports lists as well as arrays, so no data conversion is required :)). It takes in constructor default item position in carousel as the first argument and data set as the second.

Builder was extended by a many methods for more flexibility and convenience of use. Here's the full list (you can find the javadoc on each of these methods):

```java
new UniversalPickerDialog.Builder(this)
    .setTitle(ResourceTable.String_entry_MainAbility)
    .setTitle("Hello!")
    .setTitleColorRes(ResourceTable.Color_green)
    .setTitleColor(Color.GREEN)
    .setBackgroundColorRes(ResourceTable.Color_white)
    .setBackgroundColor(Color.WHITE)
    .setContentTextColorRes(ResourceTable.Color_green)
    .setContentTextColor(Color.GREEN)
    .setPositiveButtonText(ResourceTable.String_ok_text)
    .setPositiveButtonText("Yep!")
    .setNegativeButtonText(ResourceTable.String_cancel_text)
    .setNegativeButtonText("Nope!")
    .setButtonsColor(Color.GREEN)
    .setButtonsColorRes(ResourceTable.Color_green)
    .setPositiveButtonColorRes(ResourceTable.Color_green)
    .setPositiveButtonColor(Color.GREEN)
    .setNegativeButtonColorRes(ResourceTable.Color_red)
    .setNegativeButtonColor(Color.RED)
    .setContentTextSize(16)
    .setListener(this)
    .setInputs(
        new UniversalPickerDialog.Input(2, list),
        new UniversalPickerDialog.Input(0, array)
    )
    .setKey(123)
    .build()
    .show();
```

Take a look at the [sample project](entry) for more information.

### License

```
Copyright (C) 2017 stfalcon.com

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