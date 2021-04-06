package com.ensias.mine_is_yoursapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    List<User> users;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listView = findViewById(R.id.list_item);
        users = new ArrayList<>();
        //Exemples d'utilisateurs pour tester le front en attendant le back end !
        User  user = new User("Yasser" ,"Faleh" );
        User  user1 = new User("Said" ,"EL Aboudi" );
        users.add(user);
        users.add(user1);

        //Adapter
        ArrayAdapter adapter = new ArrayAdapter(this , R.layout.user_item , R.id.username , users);
        listView.setAdapter(adapter);
    }
    public class User {
        private String  url_img= null;
        private String  name;
        private String  lastName;

        public User(String name , String lastName , String url){
            this.name = name;
            this.lastName = lastName;
            this.url_img = url;
        }
        public User(String name , String lastName ){
            this.name = name;
            this.lastName = lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl_img() {
            return url_img;
        }

        public void setUrl_img(String url_img) {
            this.url_img = url_img;
        }

        @Override
        public String toString() {
            return  name +  " " + lastName ;
        }
    }

}