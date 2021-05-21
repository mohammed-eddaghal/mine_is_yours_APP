package com.ensias.mine_is_yoursapp.model;

public class User {
    private String id;
    private Double langitude;
    private Double lantitude;
    private String firstName;
    private String lastName;
    private String email;
    private String image;

    public User(){

    }
    public User( String id,Double langitude, Double lantitude, String firstName, String lastName, String email, String image) {
        this.id = id;
        this.langitude = langitude;
        this.lantitude = lantitude;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
    }

    public Double getLangitude() {
        return langitude;
    }

    public Double getLantitude() {
        return lantitude;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public void setLangitude(Double langitude) {
        this.langitude = langitude;
    }

    public void setLantitude(Double lantitude) {
        this.lantitude = lantitude;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
