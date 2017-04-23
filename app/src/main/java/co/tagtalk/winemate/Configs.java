package co.tagtalk.winemate;

/**
 * Created by Zhaoyu on 2016/5/22.
 */
public class Configs {

    public static final String SERVER_ADDRESS = "50.18.207.106";
    public static final int    PORT_NUMBER = 7890;
//    public static final String SERVER_ADDRESS = "192.168.1.169";
    public static final String TAG_TYPE = "tagtalk/winemate";
    public static final String USER_ID = "USER_ID";
    public static final String LOGIN_STATUS = "LOGIN_STATUS";
    public static final String HAS_WATCHED_INTRO = "HAS_WATCHED_INTRO";

    // Turn off debug mode for final product
    public static final boolean DEBUG_MODE = true;

    public static final int MINIMAL_REVIEW_CONTENT_CHARS = 10;

    public static int userId = 0;

    public static int    COUNTRY_ID = 1; //1 for English, 2 for Chinese
    public static String    wechat_login_code = "";

    public static final int AUTHENTICATION_CODE_LENGTH = 32; // 32 bytes
    public static final int AUTHENTICATION_CODE_PAGE_OFFSET = 26; //0x1A

    public static final int RANDOM_KEY_RANGE = 1000000000;

    public enum SHARE_TYPE{
        SHARE_TO_WECHAT_FRIENDS,
        SHARE_TO_WECHAT_MOMENTS,
        SHARE_TO_OTHERS,
    }
}
