package love.kotori.lovelive.util;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtil {

    private static int getColor(String colorStr) {
        int color;
        switch (colorStr) {
            case "red":
            default:
                color = 0xfff44336;
                break;
            case "green":
                color = 0xff4caf50;
                break;
            case "blue":
                color = 0xff2195f3;
                break;
            case "orange":
                color = 0xffffc107;
                break;
        }
        return color;
    }

    public static void showLong(View view, CharSequence text, String color) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        show(snackbar, color);
    }

    public static void showShort(View view, CharSequence text, String color) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        show(snackbar, color);
    }

    private static void show(Snackbar snackbar, String color) {
        snackbar.getView().setBackgroundColor(getColor(color));
        snackbar.show();
    }
}
