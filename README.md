# UniversalPickerDialog
HMOS 3rd party library to make implementing Dialog more easier. It includes two abilities :
1. Single Picker 
2. Multi picker

### Screenshots
---
![Single picker](https://github.com/applibgroup/UniversalPickerDialog/blob/main/images/single.png?raw=true)
![Multi picker](https://github.com/applibgroup/UniversalPickerDialog/blob/main/images/multi.png?raw=true)
![Default Style](https://github.com/applibgroup/UniversalPickerDialog/blob/main/images/default.png?raw=true)


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

Check the example app for more information.


<h1><a id="user-content-license" class="anchor" aria-hidden="true" href="#license"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M7.775 3.275a.75.75 0 001.06 1.06l1.25-1.25a2 2 0 112.83 2.83l-2.5 2.5a2 2 0 01-2.83 0 .75.75 0 00-1.06 1.06 3.5 3.5 0 004.95 0l2.5-2.5a3.5 3.5 0 00-4.95-4.95l-1.25 1.25zm-4.69 9.64a2 2 0 010-2.83l2.5-2.5a2 2 0 012.83 0 .75.75 0 001.06-1.06 3.5 3.5 0 00-4.95 0l-2.5 2.5a3.5 3.5 0 004.95 4.95l1.25-1.25a.75.75 0 00-1.06-1.06l-1.25 1.25a2 2 0 01-2.83 0z"></path></svg></a>License</h1>
<blockquote>
<p>Copyright 2016 mzelzoghbi</p>
</blockquote>
<blockquote>
<p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p>
</blockquote>
<blockquote>
<p><a href="http://www.apache.org/licenses/LICENSE-2.0" rel="nofollow">http://www.apache.org/licenses/LICENSE-2.0</a></p>
</blockquote>
<blockquote>
<p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>
</blockquote>