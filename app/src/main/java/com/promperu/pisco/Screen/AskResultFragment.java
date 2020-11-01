package com.promperu.pisco.Screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.PiscoApplication;
import com.promperu.pisco.R;
import com.promperu.pisco.Utils.Query;
import com.promperu.pisco.Utils.UtilAnalytics;
import com.promperu.pisco.Utils.ViewInstanceList;
import com.promperu.pisco.ViewModel.HomeViewModel;
import com.promperu.pisco.Screen.Dialogs.TrophyDialogFragment;
import com.promperu.pisco.Screen.Dialogs.MenuDialogFragment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class AskResultFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public AskResultFragment() {}

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_resultado_ask, container, false);
        ViewInstanceList.setViewInstances("in-resultado-ask-fragment", view);
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_in_resultado_ask_to_inicioFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        HomeViewModel homeViewModel =new HomeViewModel();

        ImageView ivBack = view.findViewById(R.id.IDBackInResultadoAsk);
        ivBack.setOnClickListener(v -> requireActivity().onBackPressed());
        Button btnPlayAgain = view.findViewById(R.id.IDButtonResultadoJuegoAskJugarDenuevo);
        btnPlayAgain.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Resultados del Juego", "Boton", "Jugar de Nuevo - Respuesta correcta");
            homeViewModel.currentScore(new Callback<JsonElement>() {

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
                            TrophyDialogFragment newFragment = new TrophyDialogFragment();
                            newFragment.setListener(() -> {
                                requireActivity().onBackPressed();
                            });
                            newFragment.show(getChildFragmentManager(), "missiles");
                        } else {
                            Navigation.findNavController(view).navigate(R.id.action_in_resultado_ask_to_inicioFragment);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @EverythingIsNonNull
                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {}

            });
        });
        RelativeLayout relRecipe = view.findViewById(R.id.IDButtonResultadoJuegoAskRecetas);
        relRecipe.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Resultados del Juego", "Boton", "Recetas_Resultado");
            if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 2.1").getSesiState()==1 &&AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 2.2").getSesiState()==1 ) {
                Navigation.findNavController(view).navigate(AskResultFragmentDirections.actionResultadoAskFragmentToOnBoardFragment("onBoard-receta"));
            }else{
                Navigation.findNavController(view).navigate(R.id.action_in_resultado_ask_to_in_inicio_receta);
            }

        });
        Button btnWhereBuy = view.findViewById(R.id.IDButtonResultadoJuegoAskDondeComprar);
        btnWhereBuy.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Resultados del Juego", "Boton", "¿Donde Comprar?_Resultado");
            Navigation.findNavController(view).navigate(R.id.action_in_resultado_ask_to_in_donde_comprar);
        });
        Button btnLearnPisco = view.findViewById(R.id.IDButtonResultadoJuegoAskAprendePisco);
        btnLearnPisco.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Resultados del Juego", "Boton", "Aprende sobre Pisco_Resultado");
            String url=AppDatabase.INSTANCE.userDao().getEntityUser().getLearnPisco();
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        ImageView ivMenu = view.findViewById(R.id.IDMenuModal);
        ivMenu.setOnClickListener(v -> {
            DialogFragment newFragment = new MenuDialogFragment();
            newFragment.show(getChildFragmentManager(), "missiles");
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            String drinkTitle = bundle.getString("BebiTitulo");
            String drinkImagePath = bundle.getString("imagenRutaBebidaUrl");
            String recipeId = bundle.getString("ReceId");
            TextView tvRecipe = view.findViewById(R.id.IdTextViewReceta);
            String yourPerfectRecipe;
            if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 1) {
                yourPerfectRecipe = getString(R.string.app_en_resultado_juego_titulo);
            } else {
                yourPerfectRecipe = getString(R.string.app_es_resultado_juego_titulo);
            }
            tvRecipe.setText(yourPerfectRecipe + " " + drinkTitle + "!");
            ImageView ivHeader = view.findViewById(R.id.IDResultadoAskImageViewCabecera);
            Picasso.get().load(drinkImagePath).into(ivHeader);
            //new DownloadImageTask(ivHeader).execute(drinkImagePath);
            ImageView ivResult = view.findViewById(R.id.ImgRelativeResultadoAsk);
            ivResult.setOnClickListener(v -> {
                if (recipeId != null) {
                    AppDatabase.INSTANCE.userDao().setUpdateReceId(Integer.parseInt(recipeId));
                }
                if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 2.1").getSesiState()==1 &&AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 2.2").getSesiState()==1 ) {
                    Navigation.findNavController(view).navigate(AskResultFragmentDirections.actionResultadoAskFragmentToOnBoardFragment("onBoard-receta"));
                }else{
                    Navigation.findNavController(view).navigate(R.id.action_in_resultado_ask_to_in_inicio_receta);
                }
            });
        }
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvBodyTitle = view.findViewById(R.id.marg);
        TextView tvCongratulations = view.findViewById(R.id.Idapp_es_resultado_felicidad);
        CardView cardImageHeader = view.findViewById(R.id.CardViewINRecetaReceImagenRecetaCabecera);
        cardImageHeader.setBackgroundResource(R.drawable.border_image);
        TextView tvEarnPoint = view.findViewById(R.id.IDapp_es_resultado_ganaste_punto);
        TextView tvWinRecipe = view.findViewById(R.id.IDapp_es_resultado_ganaste_receta);
        Button btnWhereBuy = view.findViewById(R.id.IDButtonResultadoJuegoAskDondeComprar);
        Button btnLearnPisco = view.findViewById(R.id.IDButtonResultadoJuegoAskAprendePisco);
        Button btnPlayAgain = view.findViewById(R.id.IDButtonResultadoJuegoAskJugarDenuevo);
        if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 0) {
            tvBodyTitle.setText(R.string.app_es_resultado_juego_titulo_cuerpo);
            tvCongratulations.setText(R.string.app_es_resultado_felicidad);
            tvEarnPoint.setText(R.string.app_es_resultado_ganaste_punto);
            tvWinRecipe.setText(R.string.app_es_resultado_ganaste_receta);
            btnWhereBuy.setText(R.string.app_es_donde_comprar);
            btnLearnPisco.setText(R.string.app_es_aprende_sobre_pisco);
            btnPlayAgain.setText(R.string.app_es_jugar_denuevo);
        } else {
            tvBodyTitle.setText(R.string.app_en_resultado_juego_titulo_cuerpo);
            tvCongratulations.setText(R.string.app_en_resultado_felicidad);
            tvEarnPoint.setText(R.string.app_en_resultado_ganaste_punto);
            tvWinRecipe.setText(R.string.app_en_resultado_ganaste_receta);
            btnWhereBuy.setText(R.string.app_en_donde_comprar);
            btnLearnPisco.setText(R.string.app_en_aprende_sobre_pisco);
            btnPlayAgain.setText(R.string.app_en_jugar_denuevo);
        }
        RelativeLayout rlWhereBuy = view.findViewById(R.id.rlDondeComprar);
        if(Query.readValueInt("count", getContext())==0){
            rlWhereBuy.setVisibility(View.GONE);
        }else{
            rlWhereBuy.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);

    }

}