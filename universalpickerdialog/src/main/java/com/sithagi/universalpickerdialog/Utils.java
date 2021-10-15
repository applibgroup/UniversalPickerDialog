package com.sithagi.universalpickerdialog;

import ohos.agp.components.AttrHelper;
import ohos.app.Context;

/**
 * Utility class for helper methods.
 */
public class Utils {
    /**
     * convert pixel value to fp value.
     *
     * @param context application context
     * @param px      pixel value
     * @return fp
     */
    public static int pixelsToFp(Context context, int px) {
        return (int) (px / AttrHelper.getDensity(context));
    }

    /**
     * convert fp to pixel value.
     *
     * @param context application context
     * @param fp      fp
     * @return pixel
     */
    public static int fpToPixels(Context context, int fp) {
        return AttrHelper.fp2px(fp, context);
    }

}
