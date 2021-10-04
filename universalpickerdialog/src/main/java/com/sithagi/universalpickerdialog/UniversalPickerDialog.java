package com.sithagi.universalpickerdialog;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.BaseDialog;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

public class UniversalPickerDialog implements BaseDialog.DisplayCallback, BaseDialog.DialogListener {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0x00201, "-UniversalPickerDialog-");

    //#region parameters
    protected Builder builder;
    protected ArrayList<MaterialNumberPicker> pickers;
    protected CommonDialog dialog;
    private DirectionalLayout layout;

    //#endregion parameters

    //#region implementations

    /**
     * Displays the dialog
     */

    public void show() {
        this.dialog.show();
    }

    /**
     * Cancel the dialog
     */

    public void cancel() {
        // NOTE: should we destroy the dialog when the cancel is called ?
        dialog.hide();
    }


    public void initPickers(Input... inputs) {
        this.pickers = new ArrayList<>();
        if (inputs != null) {
            for (Input input : inputs) {
                pickers.add(getPicker(input));
            }
        }
    }


    public MaterialNumberPicker getPicker(Input input) {
        MaterialNumberPicker.Builder builder = new MaterialNumberPicker.Builder(this.builder.context);
        builder.minValue(0);
        builder.maxValue(input.list.size() - 1);
        builder.defaultValue(input.defaultPosition);
        builder.wrapSelectorWheel(true);
        builder.backgroundColor(Color.TRANSPARENT);

        if (this.builder.contentTextSize != 0) {
            builder.textSize(this.builder.contentTextSize);
        }
        if (this.builder.contentTextColor.getValue() != 0) {
            builder.textColor(this.builder.contentTextColor);
        }

        if (input.formatter != null) {
            builder.formatter(input.formatter);
        } else {
            builder.formatter(value -> input.list.get(value).toString());
        }

        return builder.build();
    }



    /**
     * apply background color to a component
     */
    static void applyBackgroundColor(Component component, Color color) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(RgbColor.fromArgbInt(color.getValue()));
        component.setBackground(shapeElement);
    }


    //#region create layout for the dialog

    /**
     * [------------------------]
     * |<--title                |
     * |------------------------|
     * |                        |
     * |   ->     body    <-    |
     * |                        |
     * |------------------------|
     * |        action buttons->|
     * |------------------------|
     */

    public DirectionalLayout makeDialogTitle(Context context) {
        int paddingHorizontal = AttrHelper.vp2px(18, context);
        int paddingVertical = AttrHelper.vp2px(12, context);
        int textSize = AttrHelper.vp2px(30, context);

        DirectionalLayout layout = new DirectionalLayout(context);
        Text text = new Text(context);

        text.setTextSize(textSize);
        text.setText(getTitle());
        text.setTextColor(builder.titleColor);
        layout.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

        layout.addComponent(text);
        return layout;
    }



    DirectionalLayout getBody() {
        int paddingHorizontal = AttrHelper.vp2px(18, builder.context);
        int paddingVertical = AttrHelper.vp2px(18, builder.context);
        DirectionalLayout body = new DirectionalLayout(builder.context);
        DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig();

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
     * @return the bottom action layout
     */
    public DirectionalLayout createBottomActionBar() {
        int paddingHorizontal = AttrHelper.vp2px(18, builder.context);
        int paddingVertical = AttrHelper.vp2px(12, builder.context);
        int btnTextSize = (int) MaterialNumberPicker.fpToPixels(builder.context, builder.ACTION_BTN_SIZE);

        DirectionalLayout bottomLayout = new DirectionalLayout(builder.context);

        bottomLayout.setAlignment(LayoutAlignment.END);
        bottomLayout.setOrientation(Component.HORIZONTAL);
        bottomLayout.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingHorizontal);

        DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(
                MATCH_PARENT,
                MATCH_CONTENT
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
            if (builder.listener != null)
                builder.listener.onPick(selectedValues, builder.key);
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



    public void createView() {
        int paddingHorizontal = AttrHelper.vp2px(18, builder.context);
        layout = new DirectionalLayout(builder.context);
        DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(MATCH_CONTENT, MATCH_CONTENT);
        DirectionalLayout titleTextComponent = makeDialogTitle(builder.context);
        DirectionalLayout bottomActionComponent = createBottomActionBar();
        DirectionalLayout bodyComponetText = getBody();


        layout.setOrientation(Component.VERTICAL);
        layout.setMarginsLeftAndRight(paddingHorizontal, paddingHorizontal);
        layoutConfig.weight = pickers.size();
        applyBackgroundColor(layout, builder.backgroundColor);
        layout.setLayoutConfig(layoutConfig);

        for (MaterialNumberPicker picker : pickers) {
            ComponentContainer.LayoutConfig pickerParams = new ComponentContainer.LayoutConfig(MATCH_CONTENT, MATCH_CONTENT);
            pickerParams.setMarginsLeftAndRight(paddingHorizontal, paddingHorizontal);
            picker.setLayoutConfig(pickerParams);
            bodyComponetText.addComponent(picker);
        }

        layout.addComponent(titleTextComponent);
        layout.addComponent(bodyComponetText);
        layout.addComponent(bottomActionComponent);

    }


    public void createDialog() {
        dialog = new CommonDialog(builder.context)
                .setContentCustomComponent(layout);
        dialog.setSize(MATCH_CONTENT, MATCH_CONTENT);
        dialog.setSwipeToDismiss(true);
        dialog.setDialogListener(this);
    }



    @Override
    public boolean isTouchOutside() {
        HiLog.info(LABEL_LOG, "setDialogListener: hide", "");
        dialog.hide();
        return false;
    }

    public String getTitle() {
        return builder.title;
    }

    public String getNegativeButtonText() {
        return builder.negativeButtonText != null
                ? builder.negativeButtonText
                : builder.context.getString(ResourceTable.String_cancel_text);
    }

    public String getPositiveText() {
        return builder.positiveButtonText != null
                ? builder.positiveButtonText
                : builder.context.getString(ResourceTable.String_ok_text);
    }


    @Override
    public void onDisplay(IDialog iDialog) {

    }
    //#endregion implementations

    protected UniversalPickerDialog(Builder builder) {
        this.builder = builder;
        initPickers(builder.inputs);
        createView();
        createDialog();
    }


    //#region input
    public static class Input {
        private final int defaultPosition;
        private final AbstractList<?> list;
        private Picker.Formatter formatter;

        /**
         * Constructor for data represented in {@link AbstractList}
         *
         * @param defaultPosition is a position of item which selected by default
         * @param list            list of objects
         */
        public Input(int defaultPosition, AbstractList<?> list) {
            this.defaultPosition = defaultPosition;
            this.list = list;
        }

        /**
         * Constructor for data represented in array
         *
         * @param defaultPosition is a position of item which selected by default
         * @param array           array of objects
         */
        public <T> Input(int defaultPosition, T[] array) {
            this.defaultPosition = defaultPosition;
            this.list = new ArrayList<>(Arrays.asList(array));
        }

        /**
         * Set {@link Picker.Formatter} for format current value into a string for presentation
         */
        public void setFormatter(Picker.Formatter formatter) {
            this.formatter = formatter;
        }
    }

    /**
     * Interface definition for a callback to be invoked when data is picked
     */
    public interface OnPickListener {
        /**
         * Called when data has been picked
         *
         * @param selectedValues array with selected indices in the order in which {@link Input}s were added
         */
        void onPick(int[] selectedValues, int key);
    }

    //#endregion input

    //#region builder
    public static class Builder {
        public final int ACTION_BTN_SIZE = 32;
        private Context context;
        private Color positiveButtonColor = Color.BLACK, negativeButtonColor = Color.BLACK,
                titleColor = Color.BLACK, backgroundColor, contentTextColor;
        private float contentTextSize;
        private int key;
        private String title;
        private String negativeButtonText, positiveButtonText;
        private OnPickListener listener;
        private Input[] inputs;

        /**
         * Constructor using a context for this builder and the {@link UniversalPickerDialog} it creates.
         */
        public Builder(Context context) {
            this.context = context;
            contentTextColor = Color.BLACK;
            backgroundColor = Color.WHITE;

        }

        /**
         * Set text color resource for negative and positive buttons
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("deprecation")
        public Builder setButtonsColorRes(Color color) {
            return this.setButtonsColor(
                    new Color(context.getColor(color.getValue()))
            );
        }

        /**
         * Set text color int for negative and positive buttons
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setButtonsColor(Color color) {
            this.positiveButtonColor = negativeButtonColor = color;
            return this;
        }

        /**
         * Set text color resource for positive button
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("deprecation")
        public Builder setPositiveButtonColorRes(int color) {
            return this.setPositiveButtonColor(
                    new Color(context.getColor(color))
            );
        }

        /**
         * Set text color int for positive button
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButtonColor(Color color) {
            this.positiveButtonColor = color;
            return this;
        }

        /**
         * Set text color resource for negative button
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("deprecation")
        public Builder setNegativeButtonColorRes(int color) {
            return this.setNegativeButtonColor(
                    new Color(context.getColor(color))
            );
        }

        /**
         * Set text color int for negative button
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButtonColor(Color color) {
            this.negativeButtonColor = color;
            return this;
        }

        /**
         * Set text color resource for title
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("deprecation")
        public Builder setTitleColorRes(int color) {
            return this.setTitleColor(
                    new Color(context.getColor(color))
            );
        }

        /**
         * Set text color int for title
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitleColor(Color color) {
            this.titleColor = color;
            return this;
        }

        /**
         * Set background color resource for dialog
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("deprecation")
        public Builder setBackgroundColorRes(int color) {
            return this.setBackgroundColor(
                    new Color(context.getColor(color))
            );
        }

        /**
         * Set background color int for dialog
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setBackgroundColor(Color color) {
            this.backgroundColor = color;
            return this;
        }

        /**
         * Set text color resource data set items
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("deprecation")
        public Builder setContentTextColorRes(int color) {
            return this.setContentTextColor(
                    new Color(context.getColor(color))
            );
        }

        /**
         * Set text color int for data set items
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setContentTextColor(Color color) {
            this.contentTextColor = color;
            return this;
        }

        /**
         * Set text size sp for data set items
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setContentTextSize(float size) {
            this.contentTextSize = size;
            return this;
        }

        /**
         * Set key(tag) for dialog.
         * It may be helpful if you using more than one picker in your activity/fragment.
         * This key will be returned in {@link OnPickListener#onPick(int[], int)}
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setKey(int key) {
            this.key = key;
            return this;
        }

        /**
         * Set title text resource
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(int title) {
            return setTitle(context.getString(title));
        }

        /**
         * Set title text string
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set positive button text resource
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButtonText(int text) {
            return setPositiveButtonText(context.getString(text));
        }

        /**
         * Set positive button text string
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButtonText(String text) {
            this.positiveButtonText = text;
            return this;
        }

        /**
         * Set negative button text resource
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButtonText(int text) {
            return setNegativeButtonText(context.getString(text));
        }

        /**
         * Set negative button text string
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButtonText(String text) {
            this.negativeButtonText = text;
            return this;
        }

        /**
         * Set {@link OnPickListener} for picker.
         *
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
