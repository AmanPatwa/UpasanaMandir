package patwa.aman.com.upasanamandir;

/**
 * Created by dell on 25-12-2018.
 */

public class VideoModel {

    private String name;
    private String videoUrl;
    private String key;
    private String video_thumbnail;

    public VideoModel() {
    }

    public VideoModel(String name, String imageUrl) {
        if(name.trim().equals(""))
        {
            name="No name";
        }

        this.name = name;
        this.videoUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String imageUrl) {
        this.videoUrl = imageUrl;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
