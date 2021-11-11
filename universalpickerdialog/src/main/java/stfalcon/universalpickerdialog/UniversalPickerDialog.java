/*
 * Copyright (C) 2016 Alexander Krol stfalcon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stfalcon.universalpickerdialog;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Picker;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.BaseDialog;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * UniversalPickerDialog.
 */
public class UniversalPickerDialog implements BaseDialog.DialogListener {

    protected Builder builder;
    protected ArrayList<MaterialNumberPicker> pickers;
    protected CommonDialog dialog;
    //#region parameters
    private FpCalculationUtil fpCalculationUtil;
    private DirectionalLayout layout;

    //#endregion parameters

    //#region implementations

    /**
     * UniversalPickerDialog constructor.
     *
     * @param builder builder
     */
    protected UniversalPickerDialog(Builder builder) {
        fpCalculationUtil = new FpCalculationUtil(builder.context);
        this.builder = builder;
        initPickers(builder.inputs);
        createView();
        createDialog();
    }

    /**
     * apply background color to a component.
     */
    static void applyBackgroundColor(Component component, Color color) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(RgbColor.fromArgbInt(color.getValue()));
        component.setBackground(shapeElement);
    }

    /**
     * Displays the dialog.
     */
    public void show() {
        this.dialog.show();
    }

    /**
     * Cancel the dialog.
     */
    public void cancel() {
        // NOTE: should we destroy the dialog when the cancel is called ?
        dialog.hide();
    }

    /**
     * init the picker with given inputs.
     *
     * @param inputs inputs
     */
    public void initPickers(Input... inputs) {
        this.pickers = new ArrayList<>();
        if (inputs != null) {
            for (Input input : inputs) {
                pickers.add(getPicker(input));
            }
        }
    }

    //#endregion implementations
    //#region create layout for the dialog

    /**
     * get the picker with given input.
     *
     * @param input input class
     * @return MaterialNumberPicker
     */
    public MaterialNumberPicker getPicker(Input input) {
        MaterialNumberPicker.Builder pickerBuilder = new MaterialNumberPicker.Builder(this.builder.context);
        pickerBuilder.minValue(0);
        pickerBuilder.maxValue(input.list.size() - 1);
        pickerBuilder.defaultValue(input.defaultPosition);
        pickerBuilder.wrapSelectorWheel(true);
        pickerBuilder.backgroundColor(Color.TRANSPARENT);

        if (this.builder.contentTextSize != 0) {
            pickerBuilder.textSize(this.builder.contentTextSize);
        }
        if (this.builder.contentTextColor.getValue() != 0) {
            pickerBuilder.textColor(this.builder.contentTextColor);
        }

        if (input.formatter != null) {
            pickerBuilder.formatter(input.formatter);
        } else {
            pickerBuilder.formatter(value -> input.list.get(value).toString());
        }

        return pickerBuilder.build();
    }

    /**
     * design diagram.
     * [------------------------]
     * |<--title                |
     * |------------------------|
     * |                        |
     * |   ->     body    <-    |
     * |                        |
     * |------------------------|
     * |        action buttons->|
     * |------------------------|
     *
     * @param context context
     */
    public DirectionalLayout makeDialogTitle(Context context) {
        final int paddingHorizontal = AttrHelper.vp2px(18, context);
        final int paddingVertical = AttrHelper.vp2px(12, context);
        int textSize = AttrHelper.vp2px(30, context);

        final DirectionalLayout dialogTitleLayout = new DirectionalLayout(context);
        Text dialogTitle = new Text(context);

        dialogTitle.setTextSize(textSize);
        dialogTitle.setText(getTitle());
        dialogTitle.setTextColor(builder.titleColor);
        dialogTitleLayout.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

        dialogTitleLayout.addComponent(dialogTitle);
        return dialogTitleLayout;
    }

    DirectionalLayout getBody() {
        final int paddingHorizontal = AttrHelper.vp2px(18, builder.context);
        final int paddingVertical = AttrHelper.vp2px(18, builder.context);
        DirectionalLayout body = new DirectionalLayout(builder.context);
        final DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig();

        body.setVerticalPadding(paddingVertical, paddingVertical);
        body.setHorizontalPadding(paddingHorizontal, paddingHorizontal);
        body.setAlignment(LayoutAlignment.CENTER);
        body.setOrientation(Component.HORIZONTAL);

        layoutConfig.alignment = LayoutAlignment.CENTER;

        body.setLayoutConfig(layoutConfig);
        applyBackgroundColor(body, builder.backgroundColor);
        return body;
    }

    /**
     * creates the bottom action layout.
     *
     * @return the bottom action layout
     */
    public DirectionalLayout createBottomActionBar() {
        final int paddingHorizontal = AttrHelper.vp2px(18, builder.context);
        final int paddingVertical = AttrHelper.vp2px(12, builder.context);
        final int btnTextSize = fpCalculationUtil.fpToPixels(Builder.ACTION_BTN_SIZE);

        DirectionalLayout bottomLayout = new DirectionalLayout(builder.context);

        bottomLayout.setAlignment(LayoutAlignment.END);
        bottomLayout.setOrientation(Component.HORIZONTAL);
        bottomLayout.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingHorizontal);

        DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(
                DirectionalLayout.LayoutConfig.MATCH_PARENT,
                DirectionalLayout.LayoutConfig.MATCH_CONTENT
        );

        layoutConfig.alignment = LayoutAlignment.END;
        layoutConfig.weight = pickers.size();
        bottomLayout.setLayoutConfig(layoutConfig);


        // #region positive button
        Button positiveBtn = new Button(builder.context);
        positiveBtn.setTextSize(btnTextSize);
        positiveBtn.setTextColor(builder.positiveButtonColor);
        positiveBtn.setText(getPositiveText());
        positiveBtn.setClickedListener(c -> {
            int[] selectedValues = new int[pickers.size()];
            for (int i = 0; i < selectedValues.length; i++) {
                selectedValues[i] = pickers.get(i).getValue();
            }
            if (builder.listener != null) {
                builder.listener.onPick(selectedValues, builder.key);
            }
            dialog.hide();
        });
        //#endregion

        //#region negative button
        Button negativeBtn = new Button(builder.context);
        negativeBtn.setTextSize(btnTextSize);
        negativeBtn.setTextColor(builder.negativeButtonColor);
        negativeBtn.setText(getNegativeButtonText());
        negativeBtn.setMarginRight(paddingHorizontal);
        negativeBtn.setClickedListener(c -> dialog.hide());
        //#endregion


        bottomLayout.addComponent(negativeBtn);
        bottomLayout.addComponent(positiveBtn);

        return bottomLayout;
    }

    /**
     * make the dialog view.
     */
    public void createView() {
        final int paddingHorizontal = AttrHelper.vp2px(18, builder.context);
        layout = new DirectionalLayout(builder.context);
        final DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(
                DirectionalLayout.LayoutConfig.MATCH_CONTENT,
                DirectionalLayout.LayoutConfig.MATCH_CONTENT
        );
        final DirectionalLayout titleTextComponent = makeDialogTitle(builder.context);
        final DirectionalLayout bottomActionComponent = createBottomActionBar();
        final DirectionalLayout bodyComponetText = getBody();


        layout.setOrientation(Component.VERTICAL);
        layout.setMarginsLeftAndRight(paddingHorizontal, paddingHorizontal);
        layoutConfig.weight = pickers.size();
        applyBackgroundColor(layout, builder.backgroundColor);
        layout.setLayoutConfig(layoutConfig);

        for (MaterialNumberPicker picker : pickers) {
            ComponentContainer.LayoutConfig pickerParams = new ComponentContainer.LayoutConfig(
                    ComponentContainer.LayoutConfig.MATCH_CONTENT,
                    ComponentContainer.LayoutConfig.MATCH_CONTENT
            );
            pickerParams.setMarginsLeftAndRight(paddingHorizontal, paddingHorizontal);
            picker.setLayoutConfig(pickerParams);
            bodyComponetText.addComponent(picker);
        }

        layout.addComponent(titleTextComponent);
        layout.addComponent(bodyComponetText);
        layout.addComponent(bottomActionComponent);

    }

    /**
     * creates a CommonDialog with a custom layout.
     */
    public void createDialog() {
        dialog = new CommonDialog(builder.context)
                .setContentCustomComponent(layout);
        dialog.setSize(
                DirectionalLayout.LayoutConfig.MATCH_CONTENT,
                DirectionalLayout.LayoutConfig.MATCH_CONTENT
        );
        dialog.setSwipeToDismiss(true);
        dialog.setDialogListener(this);
    }

    @Override
    public boolean isTouchOutside() {
        dialog.hide();
        return false;
    }

    /**
     * get title from the builder.
     *
     * @return title
     */
    public String getTitle() {
        return builder.title;
    }

    /**
     * get negativeButtonText from the builder.
     *
     * @return negativeButtonText
     */
    public String getNegativeButtonText() {
        return builder.negativeButtonText != null
                ? builder.negativeButtonText
                : builder.context.getString(ResourceTable.String_cancel_text);
    }

    /**
     * get positiveButtonText from the builder.
     *
     * @return positiveButtonText
     */
    public String getPositiveText() {
        return builder.positiveButtonText != null
                ? builder.positiveButtonText
                : builder.context.getString(ResourceTable.String_ok_text);
    }

    /**
     * get the title color of the dialog.
     *
     * @return title color of the dialog
     */
    public Color getTitleColor() {
        return builder.titleColor;
    }

    /**
     * get the positive button color of the dialog.
     *
     * @return positive button color of the dialog
     */
    public Color getPositiveButtonColor() {
        return builder.positiveButtonColor;
    }

    /**
     * get the negative button color of the dialog.
     *
     * @return negative button color of the dialog
     */
    public Color getNegativeButtonColor() {
        return builder.negativeButtonColor;
    }

    /**
     * get the content button color of the dialog.
     *
     * @return content button color of the dialog
     */
    public Color getContentTextColor() {
        return builder.contentTextColor;
    }

    //#endregion implementations

    public Color getBackgroundColor() {
        return builder.backgroundColor;
    }


    //#region input

    /**
     * Interface definition for a callback to be invoked when data is picked.
     */
    public interface OnPickListener {
        /**
         * Called when data has been picked.
         *
         * @param selectedValues array with selected indices in the order in which {@link Input}s were added
         * @param key            key
         */
        void onPick(int[] selectedValues, int key);
    }

    /**
     * input class to add data to the UniversalPickerDialog.
     */
    public static class Input {
        private final int defaultPosition;
        private final AbstractList<?> list;
        private Picker.Formatter formatter;

        /**
         * Constructor for data represented in {@link AbstractList}.
         *
         * @param defaultPosition is a position of item which selected by default
         * @param list            list of objects
         */
        public Input(int defaultPosition, AbstractList<?> list) {
            this.defaultPosition = defaultPosition;
            this.list = list;
        }

        /**
         * Constructor for data represented in array.
         *
         * @param defaultPosition is a position of item which selected by default
         * @param array           array of objects
         * @param <T>             array type
         */
        public <T> Input(int defaultPosition, T[] array) {
            this.defaultPosition = defaultPosition;
            this.list = new ArrayList<>(Arrays.asList(array));
        }

        /**
         * Set {@link Picker.Formatter} for format current value into a string for presentation.
         *
         * @param formatter formatter
         */
        public void setFormatter(Picker.Formatter formatter) {
            this.formatter = formatter;
        }
    }

    //#endregion input

    //#region builder

    /**
     * Builder class to add setting to the UniversalPickerDialog.
     */
    public static class Builder {
        public static final int ACTION_BTN_SIZE = 32;
        private Context context;
        private Color positiveButtonColor = Color.BLACK;
        private Color negativeButtonColor = Color.BLACK;
        private Color titleColor = Color.BLACK;
        private Color backgroundColor;
        private Color contentTextColor;
        private int contentTextSize;
        private int key;
        private String title;
        private String negativeButtonText;
        private String positiveButtonText;
        private OnPickListener listener;
        private Input[] inputs;

        /**
         * Constructor using a context for this builder and the {@link UniversalPickerDialog} it creates.
         *
         * @param context context
         */
        public Builder(Context context) {
            this.context = context;
            contentTextColor = Color.BLACK;
            backgroundColor = Color.WHITE;

        }

        /**
         * Set text color resource for negative and positive buttons.
         *
         * @param color color resource id
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setButtonsColorRes(int color) {
            return this.setButtonsColor(
                    new Color(context.getColor(color))
            );
        }

        /**
         * Set text color int for negative and positive buttons.
         *
         * @param color color
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setButtonsColor(Color color) {
            this.positiveButtonColor = negativeButtonColor = color;
            return this;
        }

        /**
         * Set text color resource for positive button.
         *
         * @param color color resource id
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButtonColorRes(int color) {
            return this.setPositiveButtonColor(
                    new Color(context.getColor(color))
            );
        }

        /**
         * Set text color int for positive button.
         *
         * @param color color
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButtonColor(Color color) {
            this.positiveButtonColor = color;
            return this;
        }

        /**
         * Set text color resource for negative button.
         *
         * @param color color resource id
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButtonColorRes(int color) {
            return this.setNegativeButtonColor(
                    new Color(context.getColor(color))
            );
        }

        /**
         * Set text color int for negative button.
         *
         * @param color color
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButtonColor(Color color) {
            this.negativeButtonColor = color;
            return this;
        }

        /**
         * Set text color resource for title.
         *
         * @param color color resource id
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitleColorRes(int color) {
            return this.setTitleColor(
                    new Color(context.getColor(color))
            );
        }

        /**
         * Set text color int for title.
         *
         * @param color color
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitleColor(Color color) {
            this.titleColor = color;
            return this;
        }

        /**
         * Set background color resource for dialog.
         *
         * @param color color resource id
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setBackgroundColorRes(int color) {
            return this.setBackgroundColor(
                    new Color(context.getColor(color))
            );
        }

        /**
         * Set background color int for dialog.
         *
         * @param color color
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setBackgroundColor(Color color) {
            this.backgroundColor = color;
            return this;
        }

        /**
         * Set text color resource data set items.
         *
         * @param color color resource id
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setContentTextColorRes(int color) {
            return this.setContentTextColor(
                    new Color(context.getColor(color))
            );
        }

        /**
         * Set text color int for data set items.
         *
         * @param color color
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setContentTextColor(Color color) {
            this.contentTextColor = color;
            return this;
        }

        /**
         * Set text size sp for data set items.
         *
         * @param size text size
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setContentTextSize(int size) {
            this.contentTextSize = size;
            return this;
        }

        /**
         * Set key(tag) for dialog.
         * It may be helpful if you using more than one picker in your activity/fragment.
         * This key will be returned in {@link OnPickListener#onPick(int[], int)}
         *
         * @param key key
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setKey(int key) {
            this.key = key;
            return this;
        }

        /**
         * Set title text resource.
         *
         * @param title title
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(int title) {
            return setTitle(context.getString(title));
        }

        /**
         * Set title text string.
         *
         * @param title title
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set positive button text resource.
         *
         * @param text string resource id
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButtonText(int text) {
            return setPositiveButtonText(context.getString(text));
        }

        /**
         * Set positive button text string.
         *
         * @param text positive button text
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButtonText(String text) {
            this.positiveButtonText = text;
            return this;
        }

        /**
         * Set negative button text resource.
         *
         * @param text string resource id
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButtonText(int text) {
            return setNegativeButtonText(context.getString(text));
        }

        /**
         * Set negative button text string.
         *
         * @param text negative button text
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButtonText(String text) {
            this.negativeButtonText = text;
            return this;
        }

        /**
         * Set {@link OnPickListener} for picker.
         *
         * @param listener listener
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setListener(OnPickListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * Set list of {@link Input}'s using varargs.
         * Each {@link Input} is representing an spinner in dialog with list of items from its {@link Input#list}
         *
         * @param inputs inputs
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setInputs(Input... inputs) {
            this.inputs = inputs;
            return this;
        }

        /**
         * Creates a {@link UniversalPickerDialog} with the arguments supplied to this builder. It does not
         * {@link UniversalPickerDialog#show()} the dialog. This allows the user to do any extra processing
         * before displaying the dialog. Use {@link #show()} if you don't have any other processing
         * to do and want this to be created and displayed.
         */
        public UniversalPickerDialog build() {
            return new UniversalPickerDialog(this);
        }

        /**
         * Creates a {@link UniversalPickerDialog} with the arguments supplied to this builder and
         * {@link UniversalPickerDialog#show()}'s the dialog.
         */
        public UniversalPickerDialog show() {
            UniversalPickerDialog dialog = build();
            dialog.show();
            return dialog;
        }
    }

    //#endregion builder
}
