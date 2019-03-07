package patwa.aman.com.upasanamandir;

/**
 * Created by dell on 23-12-2018.
 */

public class GalleryModel {

    private String name;
    private String imageUrl;
    private String key;

    public GalleryModel(){

    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public GalleryModel(String name, String imageUrl) {
        if(name.trim().equals(""))
        {
            name="No name";
        }
        this.name = name;
        this.imageUrl = imageUrl;

    }
}
