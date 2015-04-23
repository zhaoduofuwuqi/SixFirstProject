package com.mobiletrain.www.activitybeen;

/**
 * Created by aaa on 15-4-20.
 */
public class FragmentqbyjData {
    private String id;
    private String name;
    private String url;
    private String description;
    private String photos_count;
    private String wishes_count;
    private String fans_count;
    private String comments_count;
    private String is_valuable;
    private String valuation_range;
    private String last_updated_at;
    private String views_count;
    private FragmentqbyjCover cover;
    private FragmentUser user;
    public FragmentqbyjData() {
    }

    public FragmentqbyjData(String id, String name, String url, String description, String photos_count, String wishes_count, String fans_count, String comments_count, String is_valuable, String valuation_range, String last_updated_at, String views_count, FragmentqbyjCover cover) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.description = description;
        this.photos_count = photos_count;
        this.wishes_count = wishes_count;
        this.fans_count = fans_count;
        this.comments_count = comments_count;
        this.is_valuable = is_valuable;
        this.valuation_range = valuation_range;
        this.last_updated_at = last_updated_at;
        this.views_count = views_count;
        this.cover = cover;
    }

    public FragmentUser getUser() {
        return user;
    }

    public void setUser(FragmentUser user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotos_count() {
        return photos_count;
    }

    public void setPhotos_count(String photos_count) {
        this.photos_count = photos_count;
    }

    public String getWishes_count() {
        return wishes_count;
    }

    public void setWishes_count(String wishes_count) {
        this.wishes_count = wishes_count;
    }

    public String getFans_count() {
        return fans_count;
    }

    public void setFans_count(String fans_count) {
        this.fans_count = fans_count;
    }

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public String getIs_valuable() {
        return is_valuable;
    }

    public void setIs_valuable(String is_valuable) {
        this.is_valuable = is_valuable;
    }

    public String getValuation_range() {
        return valuation_range;
    }

    public void setValuation_range(String valuation_range) {
        this.valuation_range = valuation_range;
    }

    public String getLast_updated_at() {
        return last_updated_at;
    }

    public void setLast_updated_at(String last_updated_at) {
        this.last_updated_at = last_updated_at;
    }

    public String getViews_count() {
        return views_count;
    }

    public void setViews_count(String views_count) {
        this.views_count = views_count;
    }

    public FragmentqbyjCover getCover() {
        return cover;
    }

    public void setCover(FragmentqbyjCover cover) {
        this.cover = cover;
    }
}
