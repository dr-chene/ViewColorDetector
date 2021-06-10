package com.viper.viewcolordetector.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * created by viper on 2021/6/7
 * desc 检测工具
 */
public class DetectTools {

    public static Bitmap onCut(View view) {
        Bitmap bm = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bm);
        //为画布设置背景，不设置则自动生成透明，设置后会影响生成截屏的耗时
//        canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bm;
    }

    public static boolean onDetect(Bitmap bm) {
        float mx = bm.getWidth() / 4f;
        float my = bm.getHeight() / 4f;
        int color = bm.getPixel(0, 0);
        if (color != Color.WHITE && color != Color.BLACK) return false;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                //非米字格上的点，返回
                if (((i == 1 || i == 3) && (j == 0 || j == 4)) || ((i == 0 || i == 4) && (j == 1 || j == 3))) {
                    continue;
                }
                int x = (int) (i * mx);
                int y = (int) (j * my);
                //防止边界情况
                if (i == 4) x -= 1;
                if (j == 4) y -= 1;
                if (color != bm.getPixel(x, y)) return false;
            }
        }
        return true;
    }
}
