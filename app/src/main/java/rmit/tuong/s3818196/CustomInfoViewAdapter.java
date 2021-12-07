package rmit.tuong.s3818196;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter{
   private final Context mContext;
    private final LayoutInflater mInflater;
    private DatabaseHelper databaseHelper;



    public CustomInfoViewAdapter(LayoutInflater inflater, Context mContext) {
        this.mInflater = inflater;
        this.mContext = mContext;
    }

    @Override public View getInfoWindow(Marker marker) {
        databaseHelper = new DatabaseHelper(mContext);
        final View popup = mInflater.inflate(R.layout.custom_window_info, null);

        ((TextView) popup.findViewById(R.id.title)).setText(marker.getTitle());



        String id = marker.getSnippet();
        SiteModel site = databaseHelper.getSiteByID(Integer.parseInt(id));
        String snippets = "Leader: "+ site.getLeaderName()+"\nNumber of volunteer: "+site.getNumOfVolunteer();
        ((TextView) popup.findViewById(R.id.snippet)).setText(snippets);



        return popup;
        //return null;
    }

    @Override public View getInfoContents(Marker marker) {
        final View popup = mInflater.inflate(R.layout.custom_window_info, null);

        ((TextView) popup.findViewById(R.id.title)).setText(marker.getTitle());
        ((TextView) popup.findViewById(R.id.snippet)).setText(marker.getSnippet());
        return popup;
    }
}
