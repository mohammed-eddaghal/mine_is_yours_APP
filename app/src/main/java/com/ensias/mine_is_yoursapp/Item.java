package com.ensias.mine_is_yoursapp;


import com.ensias.mine_is_yoursapp.model.User;

public class Item {
    User user;
    Tool tool;

    public Item(User user, Tool tool) {
        this.user = user;
        this.tool = tool;
    }

    public User getUser() {
        return user;
    }

    public Tool getTool() {
        return tool;
    }
}
