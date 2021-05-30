package com.ensias.mine_is_yoursapp.model;

import android.net.Uri;
import android.widget.ImageView;

public class SliderItem {

    Uri url;

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri backgroundImage) {
        this.url = backgroundImage;
    }

    public SliderItem(Uri backgroundImage) {
        this.url = backgroundImage;
    }
}
