package com.pisco.app.Screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.gson.JsonElement;
import com.pisco.app.Entity.RecipeItem;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.R;
import com.pisco.app.Screen.Dialogs.OnboardDialogFragment;
import com.pisco.app.Utils.ViewModelInstanceList;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class RecipeObjectFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    public static String ReceId ="ReceId";
    public static String ReceUsuarioMeGusta = "ReceUsuarioMeGusta";
    public static String ReceUsuarioId = "ReceUsuarioId";
    public static String ReceTitulo = "ReceTitulo";
    public static String RecePreparacion = "RecePreparacion";
    public static String ReceDescripcion = "ReceDescripcion";
    public static String ReceImagenReceta = "ReceImagenReceta";
    public static String ReceImagenRecetaCabecera = "ReceImagenRecetaCabecera";

    private ImageView ivHeart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_receta_v2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        ImageView imageView = view.findViewById(R.id.ivItem);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvRecipeId = view.findViewById(R.id.IDRECEID);
        TextView tvUserLike = view.findViewById(R.id.IDReceUsuarioMeGusta);
        TextView tvUserId = view.findViewById(R.id.IDReceUsuarioId);
        Picasso.get().load(args.getString(ReceImagenReceta)).into(imageView);
        tvName.setText(args.getString(ReceTitulo));
        tvRecipeId.setText(args.getString(ReceId));
        tvUserLike.setText(args.getString(ReceUsuarioMeGusta));
        tvUserId.setText(args.getString(ReceUsuarioId));
        ivHeart = view.findViewById(R.id.ivFavorite);
        int receUsuarioMeGusta = Integer.parseInt(args.getString(ReceUsuarioMeGusta, ""));
        if(receUsuarioMeGusta == 0){
            ivHeart.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.corazonnocontorno));
        }else{
            ivHeart.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.megustacorazoncirculo));
        }
        Button tnShow = view.findViewById(R.id.bvShow);
        if(AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId()==0) {
            tnShow.setText(R.string.app_es_ver_receta);
        }else{
            tnShow.setText(R.string.app_en_ver_receta);
        }
        tnShow.setOnClickListener(v -> {
            String recipeIdText = tvRecipeId.getText().toString();
            int recipeId = Integer.parseInt(recipeIdText);
            AppDatabase.INSTANCE.userDao().setUpdateReceId(recipeId);
            if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 2.1").getSesiState()==1 &&AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 2.2").getSesiState()==1 ) {
                Navigation.findNavController(view).navigate(RecipeFragmentDirections.actionRecipeFragmentToOnBoardFragment("onBoard-receta"));
                /*Bundle bundle = new Bundle();
                bundle.putString("onboard", "onboard-receta");
                OnboardDialogFragment onboardDialogFragment = new OnboardDialogFragment(response -> {
                    Navigation.findNavController(view).navigate(R.id.action_in_receta_to_in_inicio_receta);
                });
                onboardDialogFragment.setArguments(bundle);
                onboardDialogFragment.show(getChildFragmentManager(), "missiles");*/
            }else{
                Navigation.findNavController(view).navigate(R.id.action_in_receta_to_in_inicio_receta);
            }
        });
        ivHeart.setOnClickListener(v -> {
            String userLikeText = tvUserLike.getText().toString();
            String recipeIdText = tvRecipeId.getText().toString();
            int userLike = Integer.parseInt(userLikeText);
            int recipeId = Integer.parseInt(recipeIdText);
            RecipeItem recipeItem;
            if (userLike == 0) {
                recipeItem = new RecipeItem(1, recipeId);
                tvUserLike.setText(String.valueOf(1));
            }else{
                recipeItem = new RecipeItem(0, recipeId);
                tvUserLike.setText(String.valueOf(0));
            }
            ViewModelInstanceList.getHomeViewModelInstance().postUpdateLikeFront(recipeItem, new Callback<JsonElement>() {

                @EverythingIsNonNull
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    if (userLike == 0) {
                        ivHeart.setImageDrawable( AppCompatResources.getDrawable(view.getContext(), R.drawable.megustacorazoncirculo));
                    } else {
                        ivHeart.setImageDrawable( AppCompatResources.getDrawable(view.getContext(), R.drawable.corazonnocontorno));
                    }
                }

                @EverythingIsNonNull
                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {}

            });
        });
    }

}