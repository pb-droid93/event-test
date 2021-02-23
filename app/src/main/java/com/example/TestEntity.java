package com.example;

import com.google.gson.annotations.SerializedName;

public class TestEntity {

    @SerializedName("version")
    public String version;

    @SerializedName("url")
    public String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TestEntity(String version, String url) {
        this.version = version;
        this.url = url;
    }
}
