package com.sithagi.universalpickerdialog;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import com.sithagi.universalpickerdialog.slice.MainAbilitySlice;

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
