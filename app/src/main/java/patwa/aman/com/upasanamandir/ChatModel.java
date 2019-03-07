package patwa.aman.com.upasanamandir;

/**
 * Created by dell on 06-02-2019.
 */

public class ChatModel {


    private String display_name;
    private String mess;
    private String uid;

    public ChatModel() {
    }

    public ChatModel(String display_name, String mess, String uid) {
        this.display_name = display_name;
        this.mess = mess;
        this.uid=uid;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}