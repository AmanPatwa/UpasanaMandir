package patwa.aman.com.upasanamandir;

/**
 * Created by dell on 06-01-2019.
 */

public class UserModel {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    private String username,email,ImageUrl,imageThumbnail;

    public UserModel() {
    }

    public UserModel(String username, String email, String imageUrl, String imageThumbnail) {
        this.username = username;
        this.email = email;
        ImageUrl = imageUrl;
        this.imageThumbnail = imageThumbnail;
    }


}
