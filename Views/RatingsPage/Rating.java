package Views.RatingsPage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;

public class Rating {
    private Long rating_id;
    private String store_name;
    private ImageView image;
    private Integer total_ratings, number_of_ratings;
    private Integer store_rating;

    private Rating(Long rating_id, String store_name, Integer total_ratings, Integer number_of_ratings, Integer store_rating) {
        this.rating_id = rating_id;
        this.store_name = store_name;
        this.total_ratings = total_ratings;
        this.number_of_ratings = number_of_ratings;
        this.store_rating = store_rating;
    }

    public Rating(Long rating_id, String image, String store_name, Integer total_ratings, Integer number_of_ratings, Integer store_rating) {
        this(rating_id, store_name, total_ratings, number_of_ratings, store_rating);
        this.image = new ImageView("file:C:/xampp/htdocs/HYPEX/Dashboard/" + image.toLowerCase());
        this.image.setFitWidth(70);
        this.image.setFitHeight(70);
    }

    public Rating(Long rating_id, ImageView image, String store_name, Integer total_ratings, Integer number_of_ratings, Integer store_rating) {
        this(rating_id, store_name, total_ratings, number_of_ratings, store_rating);
        this.image = image;
        this.image.setFitWidth(55);
        this.image.setFitHeight(55);
    }

    public Long getRating_id() {
        return rating_id;
    }

    public void setRating_id(Long rating_id) {
        this.rating_id = rating_id;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public Integer getTotal_ratings() {
        return total_ratings;
    }

    public void setTotal_ratings(Integer total_ratings) {
        this.total_ratings = total_ratings;
    }

    public Integer getNumber_of_ratings() {
        return number_of_ratings;
    }

    public void setNumber_of_ratings(Integer number_of_ratings) {
        this.number_of_ratings = number_of_ratings;
    }

    public Integer getStore_rating() {
        return store_rating;
    }

    public void setStore_rating(Integer store_rating) {
        this.store_rating = store_rating;
    }

    @Override
    public String toString() {
        return this.store_name;
    }
}
