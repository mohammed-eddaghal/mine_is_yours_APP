package com.ensias.mine_is_yoursapp.model;

import android.net.Uri;
import android.widget.ImageView;

import com.ensias.mine_is_yoursapp.adapters.SliderAdapter;

import java.util.ArrayList;
import java.util.List;

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

    // transforme une liste de String à une Liste de Uri
    public static List<SliderItem> getSliderItemsFromUrisItems(List<String> urisList){
        List<SliderItem> sliderItemsList = new ArrayList<>();
        if ( urisList != null){
            for ( String uri : urisList){
                SliderItem sliderItem = new SliderItem(Uri.parse(uri));
                sliderItemsList.add(sliderItem);
            }
        }
        return sliderItemsList;
    }
}
