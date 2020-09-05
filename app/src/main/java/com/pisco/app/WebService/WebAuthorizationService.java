package com.pisco.app.WebService;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pisco.app.Entity.RecipeItem;
import com.pisco.app.ViewModel.LiveData.Drink;
import com.pisco.app.ViewModel.LiveData.CityData;
import com.pisco.app.ViewModel.LiveData.CitySaleData;
import com.pisco.app.ViewModel.LiveData.StateOnboardingData;
import com.pisco.app.ViewModel.LiveData.LoginRegisterData;
import com.pisco.app.ViewModel.LiveData.Question;
import com.pisco.app.ViewModel.LiveData.RecipeData;
import com.pisco.app.Remote.UpdateAvatarRemote;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WebAuthorizationService {

    @Headers("Content-Type: application/json")
    @POST("Front/PostActualizarBienvenidoFront")
    Call<JsonElement> postUpdateWelcomeFront();

    @Headers("Content-Type: application/json")
    @POST("Front/PostActualizarOnnboardingFront")
    Call<JsonElement> postUpdateOnboardingFront(@Body StateOnboardingData estadoOnnboardingData);

    @Headers("Content-Type: application/json")
    @POST("Front/PostActualizarOnnboardingSaltarFront")
    Call<JsonElement> postUpdateOnboardingSkipFront(@Body StateOnboardingData estadoOnnboardingData);

    @Headers("Content-Type: application/json")
    @POST("Front/PostActualizarOnnboardingListoFront")
    Call<JsonElement> postUpdateOnboardingReadyFront(@Body StateOnboardingData estadoOnnboardingData);

    @Headers("Content-Type: application/json")
    @POST("Front/PostListarBebidasFront")
    Call<List<JsonElement>> postDrinkListFront();

    @Headers("Content-Type: application/json")
    @POST("Front/PostListarPromocionesFront")
    Call<List<JsonElement>> postPromotionListFront();

    @Headers("Content-Type: application/json")
    @POST("Front/PostListarPreguntasFront")
    Call<ArrayList<JsonObject>> postQuestionListFront(@Body Drink bebida);

    @Headers("Content-Type: application/json")
    @POST("Front/PostInsertarRespuestaUsuarioFront")
    Call<JsonElement> postInsertUserAnswerFront(@Body Question pregunta);

    @Headers("Content-Type: application/json")
    @POST("Front/PostListarPuntosUsuarioFront")
    Call<JsonElement> postUserPointListFront();

    @Headers("Content-Type: application/json")
    @POST("Front/PostListarCiudadesFront")
    Call<ArrayList<JsonObject>> postCityListFront();

    @Headers("Content-Type: application/json")
    @POST("Front/PostListarPuntosVentasCiudadFront")
    Call<ArrayList<JsonObject>> postPointListCitySalesFront(@Body CityData ciudadData);

    @Headers("Content-Type: application/json")
    @POST("Front/PostListarTiendasOnlineFront")
    Call<ArrayList<JsonObject>> postStoreListOnlineFront(@Body CitySaleData ciudadVentaData);

    @Headers("Content-Type: application/json")
    @POST("Front/PostDetallePuntoVentaFront")
    Call<ArrayList<JsonObject>> postSalePointDetailFront(@Body CitySaleData ciudadVentaData);

    @Headers("Content-Type: application/json")
    @POST("Front/PostListarRecetaFront")
    Call<ArrayList<JsonObject>> postRecipeListFront(@Body RecipeData receta);

    @Headers("Content-Type: application/json")
    @POST("Front/PostListarRecetasActivasFront")
    Call<ArrayList<JsonObject>> postActiveRecipeListFront();

    @Headers("Content-Type: application/json")
    @POST("Front/PostListarRecetasBloqueadasFront")
    Call<ArrayList<JsonObject>> postBlockedRecipeListFront();

    @Headers("Content-Type: application/json")
    @POST("Front/PostActualizarMeGustaFront")
    Call<JsonElement> postUpdateLikeFront(@Body RecipeItem recipeItem);

    @Headers("Content-Type: application/json")
    @POST("Front/PostsDatosUsuarioFront")
    Call<JsonElement> postUserDataFront();

    @Headers("Content-Type: application/json")
    @POST("Front/PostsActualizarUsuarioFront")
    Call<JsonElement> postUpdateUserFront(@Body LoginRegisterData loginRegisterData);

    @Headers("Content-Type: application/json")
    @POST("Front/PostsActualizarImagenUsuarioFront")
    Call<JsonElement> postUpdateAvatar(@Body UpdateAvatarRemote updateAvatarRemote);

    @Headers("Content-Type: application/json")
    @POST("Front/PostSeleccionarLogueoCorreoFront")
    Call<JsonElement> postLogInSelectionEmailFront(@Body LoginRegisterData loginRegisterData);

}