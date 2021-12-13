package rmit.tuong.s3818196;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer extends DefaultClusterRenderer<SiteModel> {

    private final Context mContext;



    @Override
    protected void onBeforeClusterItemRendered(@NonNull SiteModel item, @NonNull MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.icon(bitmapDescriptorFromVector(mContext,R.drawable.ic_site))
        .snippet(String.valueOf(item.getId()));

    }

    @Override
    protected boolean shouldRenderAsCluster(@NonNull Cluster<SiteModel> cluster) {
        return cluster.getSize() > 1;
    }

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<SiteModel> clusterManager) {
        super(context, map, clusterManager);
        this.mContext = context;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int
            vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth()
                , vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
