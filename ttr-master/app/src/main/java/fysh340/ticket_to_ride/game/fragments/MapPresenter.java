package fysh340.ticket_to_ride.game.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import fysh340.ticket_to_ride.R;
import fysh340.ticket_to_ride.game.fragments.mapsupport.MapHelper;
import fysh340.ticket_to_ride.game.fragments.mapsupport.MapRoute;
import interfaces.IServer;
import interfaces.Observer;
import model.AbstractPlayer;
import model.ClientModel;
import model.Game;
import model.MapModel;
import model.Route;
import serverproxy.ServerProxy;


public class MapPresenter extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnPolylineClickListener, Observer {

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    private GoogleMap mMap;
    private Marker savedMarker;
    private MapModel mMapModel = MapModel.getMapInstance();
    private IServer mServerProxy = new ServerProxy();
    private Game mGame = Game.getGameInstance();
    private ClientModel mClientModel = ClientModel.getMyClientModel();

    public MapPresenter() {
        // Required empty public constructor
    }

    public Marker getSavedMarker() {
        return savedMarker;
    }

    public void setSavedMarker(Marker savedMarker) {
        this.savedMarker = savedMarker;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMapModel.register(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MapHelper.initMap(getActivity(), mMap);

        mMap.setOnPolylineClickListener(this);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });


        if (savedMarker != null) {
            onMarkerClick(savedMarker);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(savedMarker.getPosition()));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng clickCoords) {
                Float detectionRange = 300000f;
                Polyline myClosePolyLine = null;
                float myCloseResult = detectionRange;
                for (Polyline polyline : MapHelper.getMyPolyLines()) {
                    for (LatLng polyCoords : polyline.getPoints()) {
                        float[] results = new float[1];
                        Location.distanceBetween(clickCoords.latitude, clickCoords.longitude,
                                polyCoords.latitude, polyCoords.longitude, results);

                        if (results[0] < detectionRange && results[0] < myCloseResult) {
                            myCloseResult = results[0];
                            myClosePolyLine = polyline;
                        }
                    }
                }
                if (myClosePolyLine != null){
                    onPolylineClick(myClosePolyLine);
                }
            }
        });

        for (AbstractPlayer player : mGame.getVisiblePlayerInformation()) {
            for (Integer routeID : player.getClaimedRoutes()) {
                int color = ContextCompat.getColor(getContext(), player.getColor());
                MapHelper.changeColor(MapRoute.getRoute(routeID), color);
            }
        }
    }


    public static MapPresenter newInstance() {
        MapPresenter fragment = new MapPresenter();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onPolylineClick(Polyline polyline) {
        MapRoute route = (MapRoute) polyline.getTag();
        int key = route.getKey();
        //Toast.makeText(getActivity(), String.valueOf(key), Toast.LENGTH_SHORT).show();
        mGame.setCurrentlySelectedRouteID(key);
    }

    @Override
    public void update() {
        //int color = getResources().getOriginalColor(mMapModel.getOriginalColor());
        int color = ContextCompat.getColor(getContext(), mMapModel.getColor());
        int routeID = mMapModel.getLastRoute();
        MapRoute route = MapRoute.getRoute(routeID);
        MapHelper.changeColor(route, color);
    }
}
