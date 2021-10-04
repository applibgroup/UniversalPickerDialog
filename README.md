#UniversalPickerDialog

## how to use this library
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
                .setTitle(R.string.hello)
                .setListener(this)
                .setInputs(
                        new UniversalPickerDialog.Input(0, list),
                        new UniversalPickerDialog.Input(2, array)
                )
                .show();
```
