package im.vector;

import android.view.View;

import java.util.Calendar;

public class Utils {

    private static int count = 0;
    private static long currentTime = 0;
    private static long lastTime = 0;

    public static void detectClicks(MultipleClickListener multipleClickListener) {

        if (lastTime == 0) {
            lastTime = System.currentTimeMillis();
        }
        currentTime = System.currentTimeMillis();
        if (currentTime - lastTime <= 1000) {
            count++;
            if (count == 5) {
                multipleClickListener.on5TimesClicked();
                count = 0;
                currentTime = 0;
                lastTime = 0;
            }
        } else {
            count = 0;
            count++;
        }
        lastTime = currentTime;
    }
}
