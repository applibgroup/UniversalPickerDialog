package com.sithagi.universalpickerdialog;

import ohos.agp.components.AttrHelper;
import ohos.app.Context;

/**
 * Utility class for converting between pixel value & font pixel(fp) value.
 */
public class FpCalculationUtil {
    public Context context;

    public FpCalculationUtil(Context context) {
        super();
        this.context = context;
    }

    /**
     * convert pixel value to fp value.
     *
     * @param px      pixel value
     * @return fp
     */
    public int pixelsToFp(int px) {
        return (int) (px / AttrHelper.getDensity(context));
    }

    /**
     * convert fp to pixel value.
     *
     * @param fp      fp
     * @return pixel
     */
    public int fpToPixels( int fp) { return AttrHelper.fp2px(fp, context);
    }

}
