package com.ensias.mine_is_yoursapp;

public class Tool {
    String toolName;
    String desc;
    String etat;
    String idOwner;
    String title;
    String type;



    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public String getEtat() {
        return etat;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public Tool(String toolName) {
        this.toolName = toolName;
    }

    public String getToolName() {
        return toolName;
    }

    @Override
    public String toString() {
        return "Tool{" +
                "toolName='" + toolName + '\'' +
                ", desc='" + desc + '\'' +
                ", etat='" + etat + '\'' +
                ", idOwner='" + idOwner + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
