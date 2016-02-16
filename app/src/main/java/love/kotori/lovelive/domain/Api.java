package love.kotori.lovelive.domain;

public class Api {

    public static String tags[] = new String[]{"ラブライブ", "高坂穂乃果", "南ことり", "園田海未", "西木野真姫", "星空凛", "小泉花陽", "矢澤にこ", "絢瀬絵里", "东条希"};

    public static String actionBarColors[] = new String[]{"#3F51B5", "#FFB11B", "#FFFFFF", "#0000FF", "#FF0000", "#FFFF00", "#ADFF2F", "#FFC0CB", "#00FFFF", "#800080"};

    public static String statusBarColors[] = new String[]{"#303F9F", "#ff9000", "#eddcdc", "#1f1fca", "#bb1d1d", "#cbcb1e", "#7ebb21", "#e48f9e", "#1fcfcf", "#5f0d5f"};

    public static String titleColors[] = new String[]{"white", "white", "black", "white", "white", "black", "white", "white", "white", "white"};

    public static String getApiUrl(int type, int page, int perPage) {
        return "https://api.kotori.love/pixiv/search.php?q=" + tags[type] + "&page=" + page + "&per_page=" + perPage;
    }

}
