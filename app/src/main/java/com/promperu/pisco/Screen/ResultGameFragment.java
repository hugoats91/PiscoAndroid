package com.promperu.pisco.Screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.PiscoApplication;
import com.promperu.pisco.R;
import com.promperu.pisco.Utils.AppConstantList;
import com.promperu.pisco.Utils.DownloadImageTask;
import com.promperu.pisco.Utils.UtilAnalytics;
import com.promperu.pisco.Utils.UtilSound;
import com.promperu.pisco.Utils.ViewInstanceList;
import com.promperu.pisco.ViewModel.HomeViewModel;
import com.promperu.pisco.ViewModel.LiveData.Question;
import com.promperu.pisco.Screen.Dialogs.TrophyDialogFragment;
import com.promperu.pisco.Screen.Dialogs.MenuDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class ResultGameFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    private TextView tvTitle = null;
    private Button btnQuestion1 = null;
    private Button btnQuestion2 = null;
    private String question = null;
    private JsonArray jsonArray;
    private int drinkId;
    private String resultDrinkImage;
    private String drinkTitle;
    private String drinkImagePath;
    private TextView tvRecipe;

    public ResultGameFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_resultado_juego, container, false);
        ViewInstanceList.setViewInstances("in-resultado-juego-fragment", view);
        return view;
    }

    public void answerAction(View v, String text){
        int answerId;
        int answerScore;
        int recipeId;
        HomeViewModel homeViewModel = new HomeViewModel();
        String answer;
        String answerMessage;
        for(int j = 0; j < jsonArray.size(); j++) {
            if (j == jsonArray.size()) break;
            answerId = jsonArray.get(j).getAsJsonObject().get("RespId").getAsInt();
            answerScore = jsonArray.get(j).getAsJsonObject().get("RespPuntaje").getAsInt();
            Question question =new Question(answerId,"",0);
            answer = jsonArray.get(j).getAsJsonObject().get("RespRespuesta").getAsString();
            answerMessage = jsonArray.get(j).getAsJsonObject().get("RespMensaje").getAsString();
            recipeId = jsonArray.get(j).getAsJsonObject().get("ReceId").getAsInt();
            if(answer.equals(text)){
                Bundle argsAnswer = new Bundle();
                argsAnswer.putString("BebiTitulo", drinkTitle);
                argsAnswer.putString("imagenRutaBebidaUrl", drinkImagePath);
                argsAnswer.putString("ReceId", String.valueOf(recipeId));
                if (answerScore == 0) {
                    UtilSound.startSound(getContext(), R.raw.lose);
                    argsAnswer.putString("RespMensaje", answerMessage);
                    Navigation.findNavController(v).navigate(R.id.action_in_resultado_juego_to_in_resultado_juego_again,argsAnswer);
                } else {
                    UtilSound.startSound(getContext(), R.raw.win);
                    homeViewModel.answerQuestionUser(question, new Callback<JsonElement>() {

                        @EverythingIsNonNull
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            Navigation.findNavController(v).navigate(R.id.action_in_resultado_juego_to_in_resultado_ask, argsAnswer);
                        }

                        @EverythingIsNonNull
                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {}

                    });
                }
                break;
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UtilAnalytics.sendEventScreen(PiscoApplication.getInstance(requireContext()), "Resultado del juego");
        HomeViewModel homeViewModel =new HomeViewModel();
        ImageView ivBack = view.findViewById(R.id.IDBackInResultadoJuego);
        ImageView ivHeader = view.findViewById(R.id.IDInResultadoJuegoCabeceraImageView);
        Button btnPlayAgain = view.findViewById(R.id.IDButtonResultadoJuegoJugarDenuevo);
        ImageView ivMenu = view.findViewById(R.id.IDMenuModal);
        tvRecipe = view.findViewById(R.id.IdTextViewReceta);
        tvTitle = view.findViewById(R.id.IDResultadoJuegoTextView);
        btnQuestion1 = view.findViewById(R.id.IDResultadoJuegoButtonFirst);
        btnQuestion2 = view.findViewById(R.id.IDResultadoJuegoButtonSecond);
        TextView tvBody = view.findViewById(R.id.marg);
        CardView cardView = view.findViewById(R.id.CardViewINImagenRecetaCabecera);
        cardView.setBackgroundResource(R.drawable.border_image);
        if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 1) {
            tvBody.setText(R.string.app_en_resultado_juego_titulo_cuerpo);
            btnPlayAgain.setText(R.string.app_en_jugar_denuevo);
        } else {
            tvBody.setText(R.string.app_es_resultado_juego_titulo_cuerpo);
            btnPlayAgain.setText(R.string.app_es_jugar_denuevo);
        }
        btnQuestion1.setOnTouchListener((v, event) -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Resultado del Juego", "Respuesta", "Respuesta");
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btnQuestion1.setTextColor(Color.WHITE);
            }else{
                btnQuestion1.setTextColor(Color.parseColor("#041c54"));
            }
            return false;
        });
        btnQuestion2.setOnTouchListener((v, event) -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Resultado del Juego", "Respuesta", "Respuesta");
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                btnQuestion2.setTextColor(Color.WHITE);
            } else {
                btnQuestion2.setTextColor(Color.parseColor("#041c54"));

            }
            return false;
        });
        ivBack.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Resultado del Juego", "Clic Boton", "Volver");
            requireActivity().onBackPressed();
        });
        homeViewModel.questionList(new Callback<ArrayList<JsonObject>>() {

            @SuppressLint("SetTextI18n")
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                ArrayList<JsonObject> arrayList = response.body();
                if (arrayList == null || arrayList.isEmpty()) {
                    return;
                }
                try {
                    JsonObject jsonObject = arrayList.get(0).getAsJsonObject();
                    question = jsonObject.get("PregPregunta").getAsString();
                    drinkId = jsonObject.get("BebiId").getAsInt();
                    resultDrinkImage = jsonObject.get("BebiImagenResultado").getAsString();
                    drinkTitle = jsonObject.get("BebiTitulo").getAsString();
                    drinkImagePath = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath() + AppConstantList.RUTA_BEBIDA + drinkId +"/" + resultDrinkImage;
                    new DownloadImageTask(ivHeader).execute(drinkImagePath);
                    tvTitle.setText(question);
                    String yourPerfectRecipe;
                    if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 1) {
                        yourPerfectRecipe = getString(R.string.app_en_resultado_juego_titulo);
                    } else {
                        yourPerfectRecipe = getString(R.string.app_es_resultado_juego_titulo);
                    }
                    tvRecipe.setText(yourPerfectRecipe + " " + drinkTitle + "!");
                    jsonArray =jsonObject.getAsJsonObject().get("listaRespuesta").getAsJsonArray();
                    for(int j = 0; j < 2; j++) {
                        String answer = jsonArray.get(j).getAsJsonObject().get("RespRespuesta").getAsString();
                        if (j == 0) {
                            btnQuestion1.setText(answer);
                        } else {
                            btnQuestion2.setText(answer);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {}

        });
        btnQuestion1.setOnClickListener(v -> answerAction(v, btnQuestion1.getText().toString()));
        btnQuestion2.setOnClickListener(v -> answerAction(v, btnQuestion2.getText().toString()));
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
                    int partPuntajeAcumulado=jsonObject.get("PartPuntajeAcumulado").getAsInt();
                    if (partPuntajeAcumulado==AppDatabase.INSTANCE.userDao().getEntityUser().getNumberRoulette()) {
                        DialogFragment newFragment = new TrophyDialogFragment();
                        newFragment.show(getChildFragmentManager(), "missiles");
                    } else {
                        requireActivity().onBackPressed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {}

        }));
        ivMenu.setOnClickListener(v -> {
            DialogFragment newFragment = new MenuDialogFragment();
            newFragment.show(getChildFragmentManager(), "missiles");
        });
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