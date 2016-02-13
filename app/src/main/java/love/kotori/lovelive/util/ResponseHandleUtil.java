package love.kotori.lovelive.util;

import android.content.Context;

import love.kotori.lovelive.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import io.realm.Realm;

public class ResponseHandleUtil {

    public static void HandleSearchResponseFromHttp(Context context, String response, int currentImagePosition, int onceLoad, int type) throws ExecutionException, InterruptedException, JSONException {

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("response");

            for (int i = currentImagePosition; currentImagePosition < jsonArray.length() && i < currentImagePosition + onceLoad; i++) {
            jsonObject = jsonArray.getJSONObject(i);
            String url = jsonObject.getJSONObject("image_urls").getString("px_480mw");

            Image image = new Image(url);
            //            Bitmap bitmap = Glide.with(context).load(image.getUrl()).asBitmap().thumbnail(0.1f)
            //                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            //                    .get();
            //            image.setWidth(bitmap.getWidth());
            //            image.setHeight(bitmap.getHeight());
            image.setType(type);
            realm.copyToRealm(image);

        }

        realm.commitTransaction();//提交事务
        realm.close();
    }
}
