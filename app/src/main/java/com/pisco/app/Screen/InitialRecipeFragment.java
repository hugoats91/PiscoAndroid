package com.pisco.app.Screen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pisco.app.Adapter.IngredientsAdapter;
import com.pisco.app.Entity.IngredientItem;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.R;
import com.pisco.app.Utils.AppConstantList;
import com.pisco.app.Utils.DownloadImageTask;
import com.pisco.app.Utils.ViewInstanceList;
import com.pisco.app.ViewModel.HomeViewModel;
import com.pisco.app.ViewModel.LiveData.RecipeData;
import com.pisco.app.Screen.Dialogs.TrophyDialogFragment;
import com.pisco.app.Screen.Dialogs.MenuDialogFragment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class InitialRecipeFragment extends Fragment {

    private OnFragmentInteractionListener listener;

    private IngredientsAdapter ingredientsAdapter;
    private RecyclerView rvIngredients;
    private List<IngredientItem> ingredientItemList;

    public InitialRecipeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_inicio_receta, container, false);
        ViewInstanceList.setViewInstances("in-inicio-receta-fragment", view);
        HomeViewModel homeViewModel = new HomeViewModel();
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        int recipeId = AppDatabase.INSTANCE.userDao().getEntityUser().getRecipeId();
        RecipeData recipeData = new RecipeData(recipeId);
        AppDatabase.INSTANCE.userDao().getEntityUser().setRecipeId(0);
        homeViewModel.postRecipeListFront(recipeData, new Callback<ArrayList<JsonObject>>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                ArrayList<JsonObject> arrayList = response.body();
                LinearLayout linearLayout = view.findViewById(R.id.IDLinearLayoutInRecetaPReparacion);
                if (arrayList == null || arrayList.isEmpty()) {
                    linearLayout.removeAllViews();
                    return;
                }
                try {
                    JsonObject jsonObject = arrayList.get(0).getAsJsonObject();
                    int like = jsonObject.get("ReceUsuarioMeGusta").getAsInt();
                    ImageView ivLike = view.findViewById(R.id.IDMeGustaCorazon);
                    ivLike.setVisibility(like == 0 ? View.INVISIBLE : View.VISIBLE);
                    int recipeId = jsonObject.get("ReceId").getAsInt();
                    String recipeTitle = jsonObject.get("ReceTitulo").getAsString();
                    TextView tvTitle = view.findViewById(R.id.IDTextViewInRecetaTitle);
                    tvTitle.setText(recipeTitle);
                    String recipeImageHeader = jsonObject.get("ReceImagenRecetaCabecera").getAsString();
                    String recipeDescription = jsonObject.get("ReceDescripcion").getAsString();
                    TextView tvDescription = view.findViewById(R.id.IDTextViewInRecetaDescription);
                    tvDescription.setText(recipeDescription);
                    ImageView ivHeader = view.findViewById(R.id.ImageViewINRecetaReceImagenRecetaCabecera);
                    String imageHeaderUrl = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath()+ AppConstantList.RUTA_RECETA + recipeId + "/" + recipeImageHeader;
                    Picasso.get().load(imageHeaderUrl).into(ivHeader);
                    ingredientItemList = new ArrayList<>();
                    JsonArray ingredientJsonArray = jsonObject.get("ListaIngredientes").getAsJsonArray();
                    for (int j = 0; j < ingredientJsonArray.size(); j++) {
                        int id = ingredientJsonArray.get(j).getAsJsonObject().get("IngrId").getAsInt();
                        String title = ingredientJsonArray.get(j).getAsJsonObject().get("IngrTitulo").getAsString();
                        String icon = ingredientJsonArray.get(j).getAsJsonObject().get("IngrIcono").getAsString();
                        String description = ingredientJsonArray.get(j).getAsJsonObject().get("IngrDescripcion").getAsString();
                        String urlIcon = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath() + AppConstantList.RUTA_INGREDIENTE + id + "/" + icon;
                        ingredientItemList.add(new IngredientItem(id, title, urlIcon, description));
                    }
                    rvIngredients = view.findViewById(R.id.rvIngredients);
                    rvIngredients.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    ingredientsAdapter = new IngredientsAdapter(ingredientItemList);
                    rvIngredients.setAdapter(ingredientsAdapter);

                    JsonArray preparationJsonArray = jsonObject.get("ListaPreparacion").getAsJsonArray();

                    ImageView imageView;
                    TextView textView;
                    for (int j = 0; j < preparationJsonArray.size(); j++) {
                        int id = preparationJsonArray.get(j).getAsJsonObject().get("RecePreparacionId").getAsInt();
                        String detail = preparationJsonArray.get(j).getAsJsonObject().get("RecePreparacionDetalle").getAsString();
                        String image = preparationJsonArray.get(j).getAsJsonObject().get("RecePreparacionImagen").getAsString();
                        String imageUrl = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath()+ AppConstantList.RUTA_RECETA_PREPARACION + id+"/" + image;
                        if(j % 2 == 0){
                            View viewLeft = inflater.inflate(R.layout.layout_preparacion_left, container, false);
                            textView = viewLeft.findViewById(R.id.IDLayoutPreparacionLeftContenido);
                            imageView = viewLeft.findViewById(R.id.IDLayoutPreparacionLeftImagen);
                            textView.setText(detail);
                            linearLayout.addView(viewLeft);
                        } else {
                            View viewRight = inflater.inflate(R.layout.layout_preparacion_right, container, false);
                            textView = viewRight.findViewById(R.id.IDLayoutPreparacionRightContenido);
                            imageView = viewRight.findViewById(R.id.IDLayoutPreparacionRightImagen);
                            textView.setText(detail);
                            linearLayout.addView(viewRight);
                        }
                        new DownloadImageTask(imageView).execute(imageUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {}

        });

        ImageView ivMenu = view.findViewById(R.id.IDMenuModal);
        ivMenu.setOnClickListener(v -> {
            DialogFragment newFragment = new MenuDialogFragment();
            newFragment.show(getChildFragmentManager(), "missiles");
        });
        view.findViewById(R.id.ImageViewBtnBackINRecetaInicio).setOnClickListener(v -> requireActivity().onBackPressed());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnPlayAgain = view.findViewById(R.id.IDButtonResultadoJuegoAgainJugarDenuevo);
        TextView tvPreparation = view.findViewById(R.id.IDapp_en_preparacion);
        TextView tvIngredientList = view.findViewById(R.id.IDapp_es_ingredientes);
        CardView cardHeader = view.findViewById(R.id.CardViewINRecetaReceImagenRecetaCabecera);
        cardHeader.setBackgroundResource(R.drawable.border_image);
        if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 0) {
            btnPlayAgain.setText(R.string.app_es_jugar_denuevo);
            tvPreparation.setText(R.string.app_es_preparacion);
            tvIngredientList.setText(R.string.app_es_ingredientes);
        } else {
            btnPlayAgain.setText(R.string.app_en_jugar_denuevo);
            tvPreparation.setText(R.string.app_en_preparacion);
            tvIngredientList.setText(R.string.app_en_ingredientes);
        }
        HomeViewModel homeViewModel = new HomeViewModel();
        btnPlayAgain.setOnClickListener(v -> homeViewModel.currentScore(new Callback<JsonElement>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement jsonElement = response.body();
                if (jsonElement == null) {
                    return;
                }
                try {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    int currentScore = jsonObject.get("PartPuntajeAcumulado").getAsInt();
                    if (currentScore == AppDatabase.INSTANCE.userDao().getEntityUser().getNumberRoulette()) {
                        DialogFragment newFragment = new TrophyDialogFragment();
                        newFragment.show(getChildFragmentManager(), "missiles");
                    } else {
                        Navigation.findNavController(view).navigate(R.id.action_in_inicio_receta_to_inicioFragment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {}

        }));
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

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);

    }

}