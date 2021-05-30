package com.ensias.mine_is_yoursapp.model;

import android.net.Uri;

import java.util.ArrayList;

public class Outil {
    private String id;
    private String idOwner;
    private String type;
    private String etat = "available";
    private String titre;

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    private String description="";
    private ArrayList<String> uris;

    public Outil(String id,String idOwner,String type ,String titre, String description, ArrayList<String> uris) {
        this.idOwner=idOwner;
        this.id = id;
        this.type=type;
        this.titre = titre;
        this.description = description;
        this.uris = uris;
    }
    public Outil(String idOwner,String type ,String titre, String description, ArrayList<String> uris) {
        this.idOwner=idOwner;
        this.type = type;
        this.titre = titre;
        this.description = description;
        this.uris = uris;
    }
    public Outil(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getUris() {
        return uris;
    }

    public void setUris(ArrayList<String> uris) {
        this.uris = uris;
    }
}
