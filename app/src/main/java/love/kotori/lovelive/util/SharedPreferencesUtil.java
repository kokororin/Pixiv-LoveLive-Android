package love.kotori.lovelive.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {

    private static final String CURRENT_PAGE_KEY = "current_image_position";

    private static SharedPreferences getSharePreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveCurrentImagePositionAndPage(Context context,int page, int type){

        SharedPreferences.Editor editor = getSharePreferences(context).edit();
        editor.putInt(CURRENT_PAGE_KEY+type,page);
        editor.commit();
    }

    public static int getCurrentPage(Context context,int type){

        SharedPreferences prefs= getSharePreferences(context);
        int page = prefs.getInt(CURRENT_PAGE_KEY+type,1);
        return  page;
    }
}
