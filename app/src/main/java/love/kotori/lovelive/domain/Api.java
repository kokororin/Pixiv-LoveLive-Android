package love.kotori.lovelive.domain;

public class Api {

    public static String tags[] = new String[]{"ラブライブ", "高坂穂乃果", "南ことり", "園田海未", "西木野真姫", "星空凛", "小泉花陽", "矢澤にこ", "絢瀬絵里", "东条希"};

    public static String getApiUrl(int type, int page, int perPage) {
        return "https://api.kotori.love/pixiv/search.php?q=" + tags[type] + "&page=" + page + "&per_page=" + perPage;
    }

}
