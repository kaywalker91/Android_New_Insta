package com.kaywalker.new_insta.Model;

public class Image {

    private String ImageUrl;
    private String Key;
    private String StorageKey;

    public Image(String imageUrl, String key, String storageKey) {
        ImageUrl = imageUrl;
        Key = key;
        StorageKey = storageKey;
    }

    public Image() {}

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getStorageKey() {
        return StorageKey;
    }

    public void setStorageKey(String storageKey) {
        StorageKey = storageKey;
    }

}