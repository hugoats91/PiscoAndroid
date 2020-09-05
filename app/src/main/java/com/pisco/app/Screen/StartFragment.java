package com.pisco.app.Screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pisco.app.Adapter.PromotionPiscoViewPagerAdapter;
import com.pisco.app.Entity.PromotionPiscoItem;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.R;
import com.pisco.app.Screen.Dialogs.MenuDialogFragment;
import com.pisco.app.Screen.Dialogs.OnboardDialogFragment;
import com.pisco.app.Screen.Dialogs.TrophyDialogFragment;
import com.pisco.app.Utils.AppConstantList;
import com.pisco.app.Utils.Query;
import com.pisco.app.Utils.UtilSound;
import com.pisco.app.Utils.ViewInstanceList;
import com.pisco.app.Utils.ViewModelInstanceList;
import com.pisco.app.Utils.ZoomOutPageTransformer;
import com.pisco.app.ViewModel.HomeViewModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class StartFragment extends Fragment {

    private ImageView ivWheel;

    private Random random;
    private int degree = 0, degreeOld = 0;
    private ViewPager viewPager;
    private PromotionPiscoViewPagerAdapter promotionPiscoViewPagerAdapter;
    private List<PromotionPiscoItem> promotionPiscoItemList;
    private View view;
    private OnFragmentInteractionListener listener;

    public StartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inicio, container, false);
        ViewInstanceList.setViewInstances("in-inicio-fragment", view);
        HomeViewModel homeViewModel = new HomeViewModel();
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        ImageView ivMenu = view.findViewById(R.id.IDMenuModal);
        ivMenu.setOnClickListener(v -> {
            DialogFragment newFragment = new MenuDialogFragment();
            newFragment.show(getChildFragmentManager(), "missiles");
        });
        Button btnUser = view.findViewById(R.id.IdInicioButtonUsuario);
        btnUser.setText(AppDatabase.INSTANCE.userDao().getEntityUser().getUserName());
        Button btnStart = view.findViewById(R.id.IdInicioButtonStart);

        homeViewModel.currentScore(new Callback<JsonElement>() {

            @EverythingIsNonNull
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement jsonElement = response.body();
                if (jsonElement == null) {
                    return;
                }
                try {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    int currentScore = jsonObject.get("PartPuntajeAcumulado").getAsInt();
                    btnStart.setText(currentScore + " pts.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }

        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_inicioFragment_self);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        homeViewModel.promotionListPisco(new Callback<List<JsonElement>>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<JsonElement>> call, Response<List<JsonElement>> response) {
                List<JsonElement> data = response.body();
                if (data == null) {
                    return;
                }
                try {
                    promotionPiscoItemList = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        String imagePath = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath() + AppConstantList.RUTA_PROMOCION + data.get(i).getAsJsonObject().get("PromId").getAsInt() + "/" + data.get(i).getAsJsonObject().get("PromImagen").getAsString();
                        promotionPiscoItemList.add(new PromotionPiscoItem(imagePath,
                                data.get(i).getAsJsonObject().get("PromTitulo").getAsString(),
                                data.get(i).getAsJsonObject().get("PromPromocion").getAsString(),
                                data.get(i).getAsJsonObject().get("PromSubTitulo").getAsString()));
                    }
                    viewPager = view.findViewById(R.id.promociones_pisco_view_pager);
                    promotionPiscoViewPagerAdapter = new PromotionPiscoViewPagerAdapter(view.getContext(), promotionPiscoItemList, getChildFragmentManager());
                    viewPager.setAdapter(promotionPiscoViewPagerAdapter);
                    viewPager.setClipToPadding(false);
                    viewPager.setPadding(40, 0, 40, 0);
                    viewPager.setPageMargin(50);
                    viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                    viewPager.setOffscreenPageLimit(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<JsonElement>> call, Throwable t) {}

        });
        ImageButton ivPoint = view.findViewById(R.id.imPointer);
        ivWheel = view.findViewById(R.id.imRoulette);
        String imageRoulettePath = AppDatabase.INSTANCE.userDao().getEntityUser().getImagePath() + AppConstantList.RUTA_RULETA + AppDatabase.INSTANCE.userDao().getEntityUser().getImageRoulette();
        Picasso.get().load(imageRoulettePath).into(ivWheel);
        random = new Random();
        ivPoint.setOnClickListener(v -> rouletteOnWeel());
        view.findViewById(R.id.imRoulette).setOnClickListener(v -> rouletteOnWeel());
        Button aprendePisco = view.findViewById(R.id.IdButtonInicioAprendePisco);
        aprendePisco.setOnClickListener(v -> {
            String url = AppDatabase.INSTANCE.userDao().getEntityUser().getLearnPisco();
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        Button btnWhereBuy = view.findViewById(R.id.IdButtonInicioDondeComprar);
        RelativeLayout rlWhereBuy = view.findViewById(R.id.rlDondeComprar);
        if(Query.readValueInt("count", getContext())==0){
            rlWhereBuy.setVisibility(View.GONE);
        }else{
            rlWhereBuy.setVisibility(View.VISIBLE);
        }
        btnWhereBuy.setOnClickListener(v -> {
            if(AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 3.1").getSesiState()==1 &&AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 3.2").getSesiState()== 1){
                Navigation.findNavController(view).navigate(StartFragmentDirections.actionInicioFragmentToOnBoardFragment("onBoard-donde"));
                /*Bundle bundle = new Bundle();
                bundle.putString("onboard", "onboard-donde");
                OnboardDialogFragment onboardDialogFragment = new OnboardDialogFragment(response -> {
                    Navigation.findNavController(view).navigate(R.id.action_inicioFragment_to_in_donde_comprar);
                });
                onboardDialogFragment.setArguments(bundle);
                onboardDialogFragment.show(getChildFragmentManager(), "missiles");*/
            }else{
                Navigation.findNavController(view).navigate(R.id.action_inicioFragment_to_in_donde_comprar);
            }
        });

        Button btnRecipeHome = view.findViewById(R.id.IdButtonInicioRecetas);
        btnRecipeHome.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_inicioFragment_to_in_receta));
        return view;
    }

    public void rouletteOnWeel() {
        HomeViewModel homeViewModel = new HomeViewModel();
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
                        degreeOld = degree % 360;
                        degree = random.nextInt(360) + 720;
                        RotateAnimation rotate = new RotateAnimation(degreeOld, degree, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(3600);
                        rotate.setFillAfter(true);
                        rotate.setInterpolator(new DecelerateInterpolator());
                        rotate.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                int number = currentNumber(360 - (degree % 360));
                                UtilSound.startSound(getContext(), R.raw.alert);
                                homeViewModel.roulette(new Callback<List<JsonElement>>() {

                                    @Override
                                    public void onResponse(Call<List<JsonElement>> call, Response<List<JsonElement>> response) {
                                        List<JsonElement> data = response.body();
                                        if (data == null) {
                                            return;
                                        }
                                        try {
                                            for (int i = 0; i < data.size(); i++) {
                                                if (number == data.get(i).getAsJsonObject().get("BebiOrden").getAsInt()) {
                                                    int bebiId = data.get(i).getAsJsonObject().get("BebiId").getAsInt();
                                                    AppDatabase.INSTANCE.userDao().setUpdateBebiId(bebiId);
                                                    if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 4.1").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 4.2").getSesiState() == 1) {
                                                        Navigation.findNavController(view).navigate(StartFragmentDirections.actionInicioFragmentToOnBoardFragment("onBoard-resultado"));
                                                        /*Bundle bundle = new Bundle();
                                                        bundle.putString("onboard", "onboard-resultado");
                                                        onboardDialogFragment.setArguments(bundle);
                                                        onboardDialogFragment.show(getChildFragmentManager(), "missiles");*/
                                                    }else{
                                                        Navigation.findNavController(view).navigate(StartFragmentDirections.actionInicioFragmentToInResultadoJuego());
                                                    }
                                                    break;
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<JsonElement>> call, Throwable t) {}

                                });
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {}

                        });
                        UtilSound.startSound(getContext(), R.raw.roulette);
                        ivWheel.startAnimation(rotate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }

        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvSpinRoulette = view.findViewById(R.id.IDapp_es_inicio_ruleta);
        Button tvRecipes = view.findViewById(R.id.IdButtonInicioRecetas);
        Button btnWhereBuy = view.findViewById(R.id.IdButtonInicioDondeComprar);
        Button btnLearnPisco = view.findViewById(R.id.IdButtonInicioAprendePisco);
        TextView tvPromotions = view.findViewById(R.id.IDapp_es_inicio_promociones);
        if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 1) {
            tvSpinRoulette.setText(R.string.app_en_inicio_ruleta);
            tvRecipes.setText(R.string.app_en_recetas);
            btnWhereBuy.setText(R.string.app_en_donde_comprar);
            btnLearnPisco.setText(R.string.app_en_aprende_sobre_pisco);
            tvPromotions.setText(R.string.app_en_inicio_promociones);
        } else {
            tvSpinRoulette.setText(R.string.app_es_inicio_ruleta);
            tvRecipes.setText(R.string.app_es_recetas);
            btnWhereBuy.setText(R.string.app_es_donde_comprar);
            btnLearnPisco.setText(R.string.app_es_aprende_sobre_pisco);
            tvPromotions.setText(R.string.app_es_inicio_promociones);
        }
    }

    private int currentNumber(int degrees) {
        int number = 0;
        if (AppDatabase.INSTANCE.userDao().getEntityUser().getNumberRoulette() == 4) {
            if (degrees >= 45 && degrees < 135) {
                number = 1;
            } else if (degrees >= 135 && degrees < 225) {
                number = 2;
            } else if (degrees >= 225 && degrees < 315) {
                number = 3;
            } else if (degrees >= 315 && degrees < 360 || degrees >= 0 && degrees < 45) {
                number = 4;
            }
        } else if (AppDatabase.INSTANCE.userDao().getEntityUser().getNumberRoulette() == 6) {
            if (degrees >= 30 && degrees < 90) {
                number = 1;
            } else if (degrees >= 90 && degrees < 150) {
                number = 2;
            } else if (degrees >= 150 && degrees < 210) {
                number = 3;
            } else if (degrees >= 210 && degrees < 270) {
                number = 4;
            } else if (degrees >= 270 && degrees < 330) {
                number = 5;
            } else if (degrees >= 330 && degrees < 360 || degrees >= 0 && degrees < 30) {
                number = 6;
            }
        } else if (AppDatabase.INSTANCE.userDao().getEntityUser().getNumberRoulette() == 8) {
            double RESTA = 22.5;
            if (degrees >= 45 - RESTA && degrees < 90 - RESTA) {
                number = 1;
            } else if (degrees >= 90 - RESTA && degrees < 135 - RESTA) {
                number = 2;
            } else if (degrees >= 135 - RESTA && degrees < 175 - RESTA) {
                number = 3;
            } else if (degrees >= 175 - RESTA && degrees < 220 - RESTA) {
                number = 4;
            } else if (degrees >= 220 - RESTA && degrees < 265 - RESTA) {
                number = 5;
            } else if (degrees >= 265 - RESTA && degrees < 310 - RESTA) {
                number = 6;
            } else if (degrees >= 310 - RESTA && degrees < 355 - RESTA) {
                number = 7;
            } else if (degrees >= 355 - RESTA && degrees < 360 || degrees >= 0 && degrees < 45 - RESTA) {
                number = 8;
            }
        } else if (AppDatabase.INSTANCE.userDao().getEntityUser().getNumberRoulette() == 10) {
            if (degrees >= 18 && degrees < 54) {
                number = 1;
            } else if (degrees >= 54 && degrees < 90) {
                number = 2;
            } else if (degrees >= 90 && degrees < 126) {
                number = 3;
            } else if (degrees >= 126 && degrees < 162) {
                number = 4;
            } else if (degrees >= 162 && degrees < 198) {
                number = 5;
            } else if (degrees >= 198 && degrees < 234) {
                number = 6;
            } else if (degrees >= 234 && degrees < 270) {
                number = 7;
            } else if (degrees >= 270 && degrees < 306) {
                number = 8;
            } else if (degrees >= 306 && degrees < 342) {
                number = 9;
            } else if (degrees >= 342 && degrees < 360 || degrees >= 0 && degrees < 18) {
                number = 10;
            }
        } else if (AppDatabase.INSTANCE.userDao().getEntityUser().getNumberRoulette() == 12) {
            if (degrees >= 15 && degrees < 45) {
                number = 1;
            } else if (degrees >= 45 && degrees < 75) {
                number = 2;
            } else if (degrees >= 75 && degrees < 105) {
                number = 3;
            } else if (degrees >= 105 && degrees < 135) {
                number = 4;
            } else if (degrees >= 135 && degrees < 165) {
                number = 5;
            } else if (degrees >= 165 && degrees < 195) {
                number = 6;
            } else if (degrees >= 195 && degrees < 225) {
                number = 7;
            } else if (degrees >= 225 && degrees < 255) {
                number = 8;
            } else if (degrees >= 255 && degrees < 285) {
                number = 9;
            } else if (degrees >= 285 && degrees < 315) {
                number = 10;
            } else if (degrees >= 315 && degrees < 345) {
                number = 11;
            } else if (degrees >= 345 && degrees < 360 || degrees >= 0 && degrees < 15) {
                number = 12;
            }
        }
        return number;
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