/*
 * Copyright (C) 2015 Kasual Business
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stfalcon.universalpickerdialog;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Picker;
import ohos.agp.utils.Color;
import ohos.app.Context;


/**
 * custom picker for UniversalPickerDialog.
 */
public class MaterialNumberPicker extends Picker {

    //#region default values
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 10;
    private static final int DEFAULT_VALUE = 1;
    private static final int TEXT_SIZE = 24;
    private static final Color SELECTED_TEXT_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(Color.argb(50, 255, 255, 155));
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    //#endregion default values
    //#region param
    FpCalculationUtil fpCalculationUtil;
    private Builder mBuilder;
    private int mTextColor;
    private int mTextSize;
    //#endregion param


    /**
     * constructor call for the material number picker.
     *
     * @param builder builder object to build the popup with custom properties
     */
    public MaterialNumberPicker(Builder builder) {
        super(builder.context);
        initViews();
        fpCalculationUtil = new FpCalculationUtil(getContext());

        mBuilder = builder;

        setMinValue(builder.minValue);
        setMaxValue(builder.maxValue);
        setValue(builder.defaultValue);

        setNormalTextColor(builder.textColor);
        setSelectedTextColor(builder.selectedTextColor);
        setNormalTextSize(fpCalculationUtil.fpToPixels(builder.textSize));
        setSelectedTextSize(fpCalculationUtil.fpToPixels((int) (builder.textSize * 1.2)));

        setFormatter(builder.formatter);
        setWheelModeEnabled(builder.wrapSelectorWheel);
        setFocusable(builder.enableFocusability ? Component.FOCUS_ENABLE : Component.FOCUS_DISABLE);

        UniversalPickerDialog.applyBackgroundColor(this, builder.backgroundColor);

    }


    /**
     * override this constructor to init the picker with default style.
     *
     * @param context builder context
     * @param attrSet attributes
     * @noinspection checkstyle:SingleLineJavadoc, CheckStyle, unused
     */
    public MaterialNumberPicker(Builder context, AttrSet attrSet) {
        super(context.context, attrSet);
        initViews();
    }

    /**
     * override this constructor to init the picker with default style.
     *
     * @param context   builder context
     * @param attrSet   attributes
     * @param styleName styleName
     * @noinspection checkstyle:SingleLineJavadoc, CheckStyle, unused
     */
    public MaterialNumberPicker(Builder context, AttrSet attrSet, String styleName) {
        super(context.context, attrSet, styleName);
        initViews();

    }

    /**
     * initialize the picker with default values.
     */
    public void initViews() {
        setMinValue(MIN_VALUE);
        setMaxValue(MAX_VALUE);
        setValue(DEFAULT_VALUE);

        setNormalTextColor(TEXT_COLOR);
        setSelectedTextColor(SELECTED_TEXT_COLOR);
        setNormalTextSize(TEXT_SIZE);
        setSelectedTextSize((int) (TEXT_SIZE * 1.2));

        setClickable(true);
        setWheelModeEnabled(false);
        setFocusable(Component.FOCUS_DISABLE);

        UniversalPickerDialog.applyBackgroundColor(this, BACKGROUND_COLOR);

    }

    //#region methods
    // setter
    public void setTextColor(int textColor) {
        mTextColor = textColor;
        updateTextAttributes();
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
        updateTextAttributes();
    }

    // functional methods
    private void updateTextAttributes() {
        setTextColor(mTextColor);
        setTextSize(fpCalculationUtil.pixelsToFp(mTextSize));
    }

    @Override
    public String toString() {
        return super.toString() + " -> " + mBuilder.defaultValue;
    }

    //#endregion methods

    //#region builder

    /**
     * builder class to configure the popup.
     *
     * @noinspection UnusedReturnValue
     */
    public static class Builder {
        private final Context context;
        private Formatter formatter;
        private Color backgroundColor = BACKGROUND_COLOR;
        private Color textColor = TEXT_COLOR;
        private Color selectedTextColor = SELECTED_TEXT_COLOR;
        private int textSize = TEXT_SIZE;
        private int minValue = MIN_VALUE;
        private int maxValue = MAX_VALUE;
        private int defaultValue = DEFAULT_VALUE;
        private boolean enableFocusability = false;
        private boolean wrapSelectorWheel = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder formatter(Formatter formatter) {
            this.formatter = formatter;
            return this;
        }

        public Builder backgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder textColor(Color textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder selectedTextColor(Color selectedTextColor) {
            this.selectedTextColor = selectedTextColor;
            return this;
        }

        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder minValue(int minValue) {
            this.minValue = minValue;
            return this;
        }

        public Builder maxValue(int maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        public Builder defaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder wrapSelectorWheel(boolean wrapSelectorWheel) {
            this.wrapSelectorWheel = wrapSelectorWheel;
            return this;
        }

        public Builder enableFocusability(boolean enableFocusability) {
            this.enableFocusability = enableFocusability;
            return this;
        }

        public MaterialNumberPicker build() {
            return new MaterialNumberPicker(this);
        }

    }

    //#endregion builder
}
