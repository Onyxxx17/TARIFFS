package com.tariff.dto;

public class GoogleOAuth2UserInfo {

    private final String id;
    private final String email;
    private final String name;
    private final String imageUrl;
    private final String firstName;
    private final String lastName;
    private final Boolean emailVerified;

    public GoogleOAuth2UserInfo(String id, String email, String name, String imageUrl,
            String firstName, String lastName, Boolean emailVerified) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailVerified = emailVerified;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }
}
