package stfalcon.universalpickerdialog.slice;

import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.app.Context;
import org.junit.Before;
import org.junit.Test;
import stfalcon.universalpickerdialog.UniversalPickerDialog;

import static org.junit.Assert.assertEquals;

public class MainAbilitySliceOhosTest {
    UniversalPickerDialog universalPickerDialog;

    @Before
    public void setUp() {
        Context context = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
        universalPickerDialog = new UniversalPickerDialog.Builder(context)
                .setTitle("Pick A city")
                .setPositiveButtonText("Select")
                .setNegativeButtonText("Cancel")
                .build();
    }

    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("stfalcon.universalpickerdialog", actualBundleName);
    }

    @Test
    public void testTitle() {
        final String title = universalPickerDialog.getTitle();
        assertEquals("Pick A city", title);
    }

    @Test
    public void testPositiveText() {
        final String positiveText = universalPickerDialog.getPositiveText();
        assertEquals("Select", positiveText);
    }

    @Test
    public void testNegativeText() {
        final String negativeButtonText = universalPickerDialog.getNegativeButtonText();
        assertEquals("Cancel", negativeButtonText);
    }
}