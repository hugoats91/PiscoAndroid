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
import com.pisco.app.PiscoApplication;
import com.pisco.app.R;
import com.pisco.app.Screen.Dialogs.MenuDialogFragment;
import com.pisco.app.Utils.AppConstantList;
import com.pisco.app.Utils.DownloadImageTask;
import com.pisco.app.Utils.UtilAnalytics;
import com.pisco.app.Utils.UtilMap;
import com.pisco.app.Utils.ViewInstanceList;
import com.pisco.app.Utils.ViewModelInstanceList;
import com.pisco.app.ViewModel.LiveData.CityData;
import com.pisco.app.ViewModel.LiveData.CitySaleData;
import com.squareup.picasso.Picasso;

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
    private LatLng currentPoint;
    private LatLng nearPoint;
    private boolean searchFirst = true;

    public WhereBuyFragment() {
    }

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
        ivBack.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "¿Donde Comprar?", "Boton", "Flecha Volver");
            requireActivity().onBackPressed();
        });

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
                        mapView.getMapAsync(WhereBuyFragment.this);
                        //getLocationPermission();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
            }

        });


    }



    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMinZoomPreference(11.0f);
        googleMap.setMaxZoomPreference(14.0f);
        mapMarkerSet(pointSaleCityId, true);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relMap.setVisibility(View.INVISIBLE);
                int cityId = ViewModelInstanceList.getHomeViewModelInstance().cityIdList.get(position);
                mapMarkerSet(cityId, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        map.setOnMarkerClickListener(marker -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "¿Donde comprar?", "Clic", "Donde comprar - Seleccion");
            int pointId = Integer.parseInt(marker.getSnippet());
            int pointCityId = ViewModelInstanceList.getHomeViewModelInstance().cityIdList.get(spinnerCity.getSelectedItemPosition());
            sellPoint(pointId, pointCityId);
            return false;
        });
    }

    public void sellPoint(int pointId, int cityPointId) {
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
                    String imageUrl = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath() + AppConstantList.RUTA_PUNTO_VENTA + pointId + "/" + pointImage;
                    Picasso.get().load(imageUrl).into(ivPoint);
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
                                    String imageUrl = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath() + AppConstantList.RUTA_TIENDA_ONLINE + storeId + "/" + storeIcon;
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
                        public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
            }

        });
    }

    public void mapMarkerSet(int pointCityId, boolean first) {
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
                        int index = 0;
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
                        LatLng latLng = new LatLng(arrObject.get(0).getAsJsonObject().get("PuntLatitud").getAsFloat(), arrObject.get(index).getAsJsonObject().get("PuntAltitud").getAsFloat());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
            }

        });
    }

    @NonNull
    public static BitmapDescriptor getBitmapDescriptorFromDrawable(@NonNull Drawable drawable) {
        BitmapDescriptor bitmapDescriptor;
        float scale = 0.8f;
        int width = (int) (drawable.getIntrinsicWidth() * scale);
        int height = (int) (drawable.getIntrinsicHeight() * scale);
        drawable.setBounds(0, 0, width, height);
        Bitmap markerBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(markerBitmap);
        drawable.draw(canvas);
        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(markerBitmap);
        return bitmapDescriptor;
    }

    private void searchNear() {
        ViewModelInstanceList.getHomeViewModelInstance().postGetCountryListFront(new Callback<ArrayList<JsonObject>>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                ArrayList<JsonObject> list = response.body();
                if (list == null) {
                    return;
                }
                try {
                    float min = 0;
                    for (int i = 0; i < list.size(); i++) {
                        float latitud = list.get(i).getAsJsonObject().get("PuntLatitud").getAsFloat();
                        float altitud = list.get(i).getAsJsonObject().get("PuntAltitud").getAsFloat();
                        LatLng latLng = new LatLng(latitud, altitud);
                        if (i == 0) {
                            min = UtilMap.distanceBetween(currentPoint, latLng);
                            nearPoint = latLng;
                        } else {
                            float temp = UtilMap.distanceBetween(currentPoint, latLng);
                            if (temp < min) {
                                min = temp;
                                nearPoint = latLng;
                            }
                        }
                    }
                    mapMarkerSet(pointSaleCityId, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
            }

        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                mapView.getMapAsync(WhereBuyFragment.this);
            } else {
                Navigation.findNavController(view).popBackStack();
            }
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