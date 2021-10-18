package stfalcon.universalpickerdialog;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import stfalcon.universalpickerdialog.slice.MainAbilitySlice;

/**
 * MainAbility.
 */
public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
