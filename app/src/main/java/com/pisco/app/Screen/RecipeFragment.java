package com.pisco.app.Screen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pisco.app.Adapter.HorizontalMarginItemDecoration;
import com.pisco.app.Adapter.RecipeBlockAdapter;
import com.pisco.app.Adapter.RecipePagerAdapter;
import com.pisco.app.Entity.RecipeItem;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.R;
import com.pisco.app.Utils.AppConstantList;
import com.pisco.app.Utils.ViewModelInstanceList;
import com.pisco.app.Utils.ViewInstanceList;
import com.pisco.app.ViewModel.HomeViewModel;
import com.pisco.app.Screen.Dialogs.TrophyDialogFragment;
import com.pisco.app.Screen.Dialogs.MenuDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class RecipeFragment extends Fragment {

    private ViewPager2 viewPagerRecipeList;
    private List<RecipeItem> recipeItemList;
    private List<RecipeItem> blockedRecipeItemList;
    private TabLayout tabLayout;
    private OnFragmentInteractionListener listener;

    public RecipeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_receta, container, false);
        ViewInstanceList.setViewInstances("in-receta-fragment", view);
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        ImageView ivMenu = view.findViewById(R.id.IDMenuModal);
        ivMenu.setOnClickListener(v -> {
            DialogFragment newFragment = new MenuDialogFragment();
            newFragment.show(getChildFragmentManager(), "missiles");
        });
        ViewModelInstanceList.getHomeViewModelInstance().postActiveRecipeListFront(new Callback<ArrayList<JsonObject>>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                ArrayList<JsonObject> arrayList = response.body();
                if (arrayList == null) {
                    return;
                }
                try {
                    recipeItemList = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        int recipeId = arrayList.get(i).getAsJsonObject().get("ReceId").getAsInt();
                        String recipeTitle = arrayList.get(i).getAsJsonObject().get("ReceTitulo").getAsString();
                        String recipeDescription = arrayList.get(i).getAsJsonObject().get("ReceDescripcion").getAsString();
                        String recipeImage = arrayList.get(i).getAsJsonObject().get("ReceImagenReceta").getAsString();
                        String recipeImageHeader = arrayList.get(i).getAsJsonObject().get("ReceImagenRecetaCabecera").getAsString();
                        int userLike = arrayList.get(i).getAsJsonObject().get("ReceUsuarioMeGusta").getAsInt();
                        int userId = arrayList.get(i).getAsJsonObject().get("ReceUsuarioId").getAsInt();
                        String imagePath = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath() + AppConstantList.RUTA_RECETA + recipeId + "/" + recipeImage;
                        recipeItemList.add(new RecipeItem(recipeTitle, "", recipeDescription, imagePath, recipeImageHeader, recipeId, userLike, userId));
                    }
                    viewPagerRecipeList = view.findViewById(R.id.recetas_pisco_view_pager);
                    RecipePagerAdapter adapter = new RecipePagerAdapter(getChildFragmentManager(), getLifecycle(), recipeItemList);

                    viewPagerRecipeList.setAdapter(adapter);
                    viewPagerRecipeList.setOffscreenPageLimit(1);
                    float nextItemVisiblePx = getResources().getDimension(R.dimen.viewpager_next_item_visible);
                    float currentItemHorizontalMarginPx = getResources().getDimension(R.dimen.viewpager_current_item_horizontal_margin);
                    float pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx;
                    viewPagerRecipeList.setPageTransformer(((page, position) ->
                    {
                        page.setTranslationX(-pageTranslationX*position);
                        page.setScaleY(1 - (0.25f * Math.abs(position)));
                        page.setAlpha(0.25f + (1 - Math.abs(position)));
                    }
                    ));
                    if (context != null) {
                        viewPagerRecipeList.addItemDecoration(new HorizontalMarginItemDecoration(context, R.dimen.viewpager_current_item_horizontal_margin));
                    }
                    tabLayout = view.findViewById(R.id.tab_receta_indicator);
                    new TabLayoutMediator(tabLayout, viewPagerRecipeList, ((tab, position) -> tab.setText("OBJECT " + (position + 1)))).attach();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {}

        });

        ViewModelInstanceList.getHomeViewModelInstance().postBlockedRecipeListFront(new Callback<ArrayList<JsonObject>>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                ArrayList<JsonObject> arrayList = response.body();
                if (arrayList == null) {
                    return;
                }
                blockedRecipeItemList = new ArrayList<>();
                try {
                    for (int i = 0; i < arrayList.size(); i++) {
                        int recipeId = arrayList.get(i).getAsJsonObject().get("ReceId").getAsInt();
                        String recipeTitle = arrayList.get(i).getAsJsonObject().get("ReceTitulo").getAsString();
                        String recipeDescription = arrayList.get(i).getAsJsonObject().get("ReceDescripcion").getAsString();
                        int userLike = arrayList.get(i).getAsJsonObject().get("ReceUsuarioMeGusta").getAsInt();
                        int userId = arrayList.get(i).getAsJsonObject().get("ReceUsuarioId").getAsInt();
                        String recipeRoundedImage = arrayList.get(i).getAsJsonObject().get("RecetaImagebRedonda").getAsString();
                        String imagePath = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath() + AppConstantList.RUTA_RECETA + recipeId + "/" + recipeRoundedImage;
                        blockedRecipeItemList.add(new RecipeItem(recipeTitle, "", recipeDescription, imagePath, "", recipeId, userLike, userId));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RecyclerView rvBlockReceta = view.findViewById(R.id.rvRecetasBloqueadas);
                rvBlockReceta.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                RecipeBlockAdapter recipeBlockAdapter = new RecipeBlockAdapter(blockedRecipeItemList);
                rvBlockReceta.setAdapter(recipeBlockAdapter);
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {}

        });
        ImageView ivBack = view.findViewById(R.id.ImageViewBtnBackRecetaInicio);
        ivBack.setOnClickListener(v -> requireActivity().onBackPressed());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvBlockedRecipe = view.findViewById(R.id.IDapp_es_receta_block);
        TextView tvSubtitle = view.findViewById(R.id.IDapp_es_receta_block_subitulo);
        TextView tvRecipeName = view.findViewById(R.id.IDapp_es_receta_nombre);
        TextView tvRecipeSubtitle = view.findViewById(R.id.IDapp_es_receta_subtitulo);
        Button btnPlayAgain = view.findViewById(R.id.IDButtonResultadoJuegoAgainJugarDenuevo);
        if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 0) {
            tvBlockedRecipe.setText(R.string.app_es_receta_block);
            tvSubtitle.setText(R.string.app_es_receta_block_subitulo);
            tvRecipeName.setText(R.string.app_es_receta_nombre);
            tvRecipeSubtitle.setText(R.string.app_es_receta_subtitulo);
            btnPlayAgain.setText(R.string.app_es_jugar);
        } else {
            tvBlockedRecipe.setText(R.string.app_en_receta_block);
            tvSubtitle.setText(R.string.app_en_receta_block_subitulo);
            tvRecipeName.setText(R.string.app_en_receta_nombre);
            tvRecipeSubtitle.setText(R.string.app_en_receta_subtitulo);
            btnPlayAgain.setText(R.string.app_en_jugar);
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
                        Navigation.findNavController(view).navigate(R.id.action_in_receta_to_inicioFragment);
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