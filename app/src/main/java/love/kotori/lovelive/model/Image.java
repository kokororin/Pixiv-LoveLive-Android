package love.kotori.lovelive.model;

import io.realm.RealmObject;

public class Image extends RealmObject {

    private String url;
    private String title;
    private int type;

    public Image() {
    }

    public Image(String url) {
        this.setUrl(url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
