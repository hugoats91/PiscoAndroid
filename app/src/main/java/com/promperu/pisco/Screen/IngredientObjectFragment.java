package com.promperu.pisco.Screen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.promperu.pisco.R;
import com.promperu.pisco.Utils.DownloadImageTask;

public class IngredientObjectFragment extends Fragment {
    
    public static final String ARG_OBJECT = "object";
    public static String IngrId ="IngrId";
    public static String IngrTitulo = "IngrTitulo";
    public static String IngrIcono = "IngrIcono";
    public static String IngrTipoDescripcion = "IngrTipoDescripcion";
    public static String IngrDescripcion = "IngrDescripcion";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_ingrediente, container, false);
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        ImageView imageView = view.findViewById(R.id.IDIngrIcono);
        TextView textView = view.findViewById(R.id.IDIngrTitulo);
        new DownloadImageTask(imageView).execute(args.getString(IngrIcono));
        textView.setText(args.getString(IngrTitulo));
    }

}