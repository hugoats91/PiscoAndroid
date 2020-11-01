package com.promperu.pisco.Screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.promperu.pisco.Utils.DownloadImageTask;
import com.promperu.pisco.Utils.UtilAnalytics;
import com.promperu.pisco.Utils.ViewInstanceList;
import com.promperu.pisco.ViewModel.HomeViewModel;
import com.promperu.pisco.Screen.Dialogs.TrophyDialogFragment;
import com.promperu.pisco.Screen.Dialogs.MenuDialogFragment;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class ResultPlayAgainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ResultPlayAgainFragment() {}

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_resultado_juego_again, container, false);
        ViewInstanceList.setViewInstances("in-resultado-juego-fragment",view);
        Context context = getContext();
        UtilAnalytics.sendEventScreen(PiscoApplication.getInstance(requireContext()), "Resultado del juego");
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_in_resultado_juego_again_to_inicioFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        ImageView ivBack = view.findViewById(R.id.IDBackInResultadoAgain);
        ivBack.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Resultados del Juego", "Clic Boton", "Respuesta Incorrecta_Flecha Volver");
            requireActivity().onBackPressed();
        });
        HomeViewModel homeViewModel =new HomeViewModel();
        Button btnPlayAgain = view.findViewById(R.id.IDButtonResultadoJuegoAgainJugarDenuevo);
        btnPlayAgain.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Resultados del Juego", "Boton", "Jugar de Nuevo - Respuesta incorrecta");
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
                            DialogFragment newFragment = new TrophyDialogFragment();
                            newFragment.show(getChildFragmentManager(), "missiles");
                        } else {
                            Navigation.findNavController(view).navigate(R.id.action_in_resultado_juego_again_to_inicioFragment);
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
        TextView tvResultIncorrect = view.findViewById(R.id.IDTextResultadoAgainIncorrecto);
        ImageView ivMenu = view.findViewById(R.id.IDMenuModal);
        ivMenu.setOnClickListener(v -> {
            DialogFragment newFragment = new MenuDialogFragment();
            newFragment.show(getChildFragmentManager(), "missiles");
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            String drinkTitle = bundle.getString("BebiTitulo");
            String drinkImagePath = bundle.getString("imagenRutaBebidaUrl");
            tvResultIncorrect.setText(bundle.getString("RespMensaje"));
            TextView tvRecipe = view.findViewById(R.id.IdTextViewReceta);
            String yourPerfectRecipe;
            if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 1) {
                yourPerfectRecipe = getString(R.string.app_en_resultado_juego_titulo);
            } else {
                yourPerfectRecipe = getString(R.string.app_es_resultado_juego_titulo);
            }
            tvRecipe.setText(yourPerfectRecipe + " " + drinkTitle + "!");
            ImageView ivHeader = view.findViewById(R.id.IDResultadoAskImageViewCabecera);
            new DownloadImageTask(ivHeader).execute(drinkImagePath);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CardView cardView = view.findViewById(R.id.CardViewINRecetaReceImagenRecetaCabecera);
        cardView.setBackgroundResource(R.drawable.border_image);
        TextView tvBody = view.findViewById(R.id.marg);
        TextView tvIncorrect = view.findViewById(R.id.IDapp_es_incorrecto);
        TextView tvMoreOptions = view.findViewById(R.id.IDTextViewAgainMasOpciones);
        Button btnPlayAgain = view.findViewById(R.id.IDButtonResultadoJuegoAgainJugarDenuevo);
        if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 1) {
            tvBody.setText(R.string.app_en_resultado_juego_titulo_cuerpo);
            tvIncorrect.setText(R.string.app_en_incorrecto);
            tvMoreOptions.setText(R.string.app_en_resultado_jugar_mas_opciones);
            btnPlayAgain.setText(R.string.app_en_jugar_denuevo);
        } else {
            tvBody.setText(R.string.app_es_resultado_juego_titulo_cuerpo);
            tvIncorrect.setText(R.string.app_es_incorrecto);
            tvMoreOptions.setText(R.string.app_es_resultado_jugar_mas_opciones);
            btnPlayAgain.setText(R.string.app_es_jugar_denuevo);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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