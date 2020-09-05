package com.pisco.app.Screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonObject;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.R;
import com.pisco.app.Utils.AppConstantList;
import com.pisco.app.Utils.DownloadImageTask;
import com.pisco.app.Utils.UtilDialog;
import com.pisco.app.Utils.ViewModelInstanceList;
import com.pisco.app.Utils.ViewInstanceList;
import com.pisco.app.ViewModel.LiveData.CityData;
import com.pisco.app.ViewModel.LiveData.CitySaleData;
import com.pisco.app.Screen.Dialogs.MenuDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class WhereBuyFragment extends Fragment implements OnMapReadyCallback {

    private static final int DEFAULT_ZOOM = 14;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private final LatLng defaultLocation = new LatLng(-12.0520704, -77.0473984);

    private View view;
    private MapView mapView;
    private GoogleMap googleMap;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Spinner spinnerCity = null;
    private OnFragmentInteractionListener listener;
    private ViewGroup containerContent;
    private int pointSaleCityId = 0;
    private RelativeLayout relMap;
    private ArrayList<JsonObject> storeList;

    public WhereBuyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_in_donde_comprar, container, false);
        ViewInstanceList.setViewInstances("in-donde-comprar-fragment", view);
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        containerContent = container;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvAvailable = view.findViewById(R.id.Id_app_es_disponible);
        if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 1) {
            tvAvailable.setText(R.string.app_en_disponible);
        } else {
            tvAvailable.setText(R.string.app_es_disponible);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        spinnerCity = view.findViewById(R.id.IdEditTextCiudad);
        ViewModelInstanceList.getHomeViewModelInstance().cityListFront(spinnerCity);
        relMap = view.findViewById(R.id.IDDondeComprarMapa);
        relMap.setVisibility(View.INVISIBLE);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        ImageView ivMenu = view.findViewById(R.id.IDMenuModal);
        ivMenu.setOnClickListener(v -> {
            DialogFragment newFragment = new MenuDialogFragment();
            newFragment.show(getChildFragmentManager(), "missiles");
        });
        try {
            Activity activity = getActivity();
            if (activity != null) {
                MapsInitializer.initialize(activity.getApplicationContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView ivClose = view.findViewById(R.id.IDPuntClose);
        ivClose.setOnClickListener(v -> relMap.setVisibility(View.INVISIBLE));
        ImageView ivBack = view.findViewById(R.id.ImageViewButtonBackMap);
        ivBack.setOnClickListener(v -> requireActivity().onBackPressed());

        ViewModelInstanceList.getHomeViewModelInstance().postGetCityListFront(new Callback<ArrayList<JsonObject>>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                ArrayList<JsonObject> arrCity = response.body();
                if (arrCity == null) {
                    return;
                }
                if (arrCity.size() > 0) {
                    pointSaleCityId = arrCity.get(0).getAsJsonObject().get("PuntoCiudadId").getAsInt();
                    mapView.getMapAsync(WhereBuyFragment.this);
                }else{
                    if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 0) {
                        UtilDialog.infoMessage(getContext(), getString(R.string.app_name), getString(R.string.app_es_no_puntos_venta), () -> {
                            Navigation.findNavController(view).popBackStack();
                        });
                    }else{
                        UtilDialog.infoMessage(getContext(), getString(R.string.app_name), getString(R.string.app_en_no_puntos_venta), () -> {
                            Navigation.findNavController(view).popBackStack();
                        });
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {}

        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMinZoomPreference(11.0f);
        googleMap.setMaxZoomPreference(14.0f);
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        ViewModelInstanceList.getHomeViewModelInstance().postGetCityListFront(new Callback<ArrayList<JsonObject>>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                ArrayList<JsonObject> arrCity = response.body();
                if (arrCity == null) {
                    return;
                }
                try {
                    if (arrCity.size() > 0) {
                        pointSaleCityId = arrCity.get(0).getAsJsonObject().get("PuntoCiudadId").getAsInt();
                        mapMarkerSet(pointSaleCityId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {}

        });
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relMap.setVisibility(View.INVISIBLE);
                int cityId = ViewModelInstanceList.getHomeViewModelInstance().cityIdList.get(position);
                mapMarkerSet(cityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });
        map.setOnMarkerClickListener(marker -> {
            int pointId = Integer.parseInt(marker.getSnippet());
            int pointCityId = ViewModelInstanceList.getHomeViewModelInstance().cityIdList.get(spinnerCity.getSelectedItemPosition());
            sellPoint(pointId, pointCityId);
            return false;
        });
    }
    
    public void sellPoint(int pointId, int cityPointId){
        CitySaleData citySaleData = new CitySaleData(pointId, cityPointId);
        ViewModelInstanceList.getHomeViewModelInstance().postPointSaleDetailFront(citySaleData, new Callback<ArrayList<JsonObject>>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                ArrayList<JsonObject> arrBussiness = response.body();
                if (arrBussiness == null || arrBussiness.isEmpty()) {
                    return;
                }
                try {
                    JsonObject jsonObject = arrBussiness.get(0);
                    int pointId = jsonObject.getAsJsonObject().get("PuntId").getAsInt();
                    String pointImage = jsonObject.getAsJsonObject().get("PuntImagen").getAsString();
                    String pointName = jsonObject.getAsJsonObject().get("PuntNombre").getAsString();
                    String pointPhone = jsonObject.getAsJsonObject().get("PuntTelefono").getAsString();
                    String pointSchedule = jsonObject.getAsJsonObject().get("PuntHorario").getAsString();
                    String pointAddress = jsonObject.getAsJsonObject().get("PuntDireccion").getAsString();
                    float pointLat = jsonObject.getAsJsonObject().get("PuntLatitud").getAsFloat();
                    float pointAlt = jsonObject.getAsJsonObject().get("PuntAltitud").getAsFloat();
                    String pointUrlFacebook = jsonObject.getAsJsonObject().get("PuntUrlFacebook").getAsString();
                    String pointUrlWhatsapp = jsonObject.getAsJsonObject().get("PuntUrlWhatsapp").getAsString();
                    String pointUrlInstagram = jsonObject.getAsJsonObject().get("PuntUrlInstagram").getAsString();
                    relMap.setVisibility(View.VISIBLE);

                    ImageView ivPoint = view.findViewById(R.id.IDPuntImagen);
                    TextView tvName = view.findViewById(R.id.IDPuntNombre);
                    TextView tvPhone = view.findViewById(R.id.IDPuntTelefono);
                    TextView tvAddress = view.findViewById(R.id.IDPuntDireccion);
                    TextView tvSchedule = view.findViewById(R.id.IDPuntHorario);
                    ImageView ivFacebook = view.findViewById(R.id.IDPuntUrlFacebook);
                    ImageView ivWhatsapp = view.findViewById(R.id.IDPuntUrlWhatsapp);
                    ImageView ivInstagram = view.findViewById(R.id.IDPuntUrlInstagram);
                    ImageView ivMap = view.findViewById(R.id.IDPuntMapa);

                    tvName.setText(pointName);
                    tvPhone.setText(pointPhone);
                    tvAddress.setText(pointAddress);
                    tvSchedule.setText(pointSchedule);
                    String imageUrl = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath()+ AppConstantList.RUTA_PUNTO_VENTA + pointId + "/" + pointImage;
                    new DownloadImageTask(ivPoint).execute(imageUrl);
                    ivFacebook.setOnClickListener(v -> {
                        Uri uri = Uri.parse(pointUrlFacebook);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    });
                    ivWhatsapp.setOnClickListener(v -> {
                        Uri uri = Uri.parse(pointUrlWhatsapp);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    });
                    ivInstagram.setOnClickListener(v -> {
                        Uri uri = Uri.parse(pointUrlInstagram);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    });
                    ivMap.setOnClickListener(v -> {
                        String mapUri = "http://maps.google.com/maps?q=" + pointLat + "," + pointAlt + "";
                        Uri uri = Uri.parse(mapUri);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    });
                    ViewModelInstanceList.getHomeViewModelInstance().getSaleStoreListOnline(citySaleData, new Callback<ArrayList<JsonObject>>() {

                        @Override
                        public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                            storeList = response.body();
                            LinearLayout mainLayout = view.findViewById(R.id.IDTiendasOnlineLinearLayout);
                            mainLayout.removeAllViews();
                            if (storeList == null) {
                                return;
                            }
                            try {
                                for (int i = 0; i < storeList.size(); i++) {
                                    int storeId = storeList.get(i).getAsJsonObject().get("TienId").getAsInt();
                                    String storeIcon = storeList.get(i).getAsJsonObject().get("TienIcono").getAsString();
                                    String pointUrl = storeList.get(i).getAsJsonObject().get("PuntUrl").getAsString();
                                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                                    View view = layoutInflater.inflate(R.layout.item_tiendas, containerContent, false);
                                    ImageView imageView = view.findViewById(R.id.IdImageView);
                                    String imageUrl = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath()+ AppConstantList.RUTA_TIENDA_ONLINE + storeId+"/" + storeIcon;
                                    new DownloadImageTask(imageView).execute(imageUrl);
                                    imageView.setOnClickListener(v -> {
                                        Uri uri = Uri.parse(pointUrl);
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    });
                                    mainLayout.addView(view);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {}

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {}

        });
    }

    public void mapMarkerSet(int pointCityId) {
        CityData cityData = new CityData(pointCityId);
        Drawable drawable = AppCompatResources.getDrawable(view.getContext(), R.drawable.pinblue);
        BitmapDescriptor bitmapDescriptor = null;
        if (drawable != null) {
            bitmapDescriptor = getBitmapDescriptorFromDrawable(drawable);
        }
        BitmapDescriptor finalBitmapDescriptor = bitmapDescriptor;
        ViewModelInstanceList.getHomeViewModelInstance().citySaleList(cityData, new Callback<ArrayList<JsonObject>>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                googleMap.clear();
                ArrayList<JsonObject> arrObject = response.body();
                if (arrObject != null && finalBitmapDescriptor != null) {
                    try {
                        for (int i = 0; i < arrObject.size(); i++) {
                            int pointId = arrObject.get(i).getAsJsonObject().get("PuntId").getAsInt();
                            float latitud = arrObject.get(i).getAsJsonObject().get("PuntLatitud").getAsFloat();
                            float altitud = arrObject.get(i).getAsJsonObject().get("PuntAltitud").getAsFloat();
                            LatLng latLng = new LatLng(latitud, altitud);
                            googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .snippet(String.valueOf(pointId))
                                    .icon(finalBitmapDescriptor));
                        }
                        LatLng latLng = new LatLng(arrObject.get(0).getAsJsonObject().get("PuntLatitud").getAsFloat(), arrObject.get(0).getAsJsonObject().get("PuntAltitud").getAsFloat());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {}

        });
    }

    @NonNull
    public static BitmapDescriptor getBitmapDescriptorFromDrawable(@NonNull Drawable drawable) {
        BitmapDescriptor bitmapDescriptor;
        float scale = 0.8f;
        int width =  (int) (drawable.getIntrinsicWidth()* scale);
        int height = (int) ( drawable.getIntrinsicHeight()* scale);
        drawable.setBounds(0, 0, width, height);
        Bitmap markerBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(markerBitmap);
        drawable.draw(canvas);
        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(markerBitmap);
        return bitmapDescriptor;
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }
                    } else {
                        googleMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            e.printStackTrace();
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(view.getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions((Activity) view.getContext(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);

    }

}