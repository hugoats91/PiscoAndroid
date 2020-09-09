package com.pisco.app.Screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.PiscoApplication;
import com.pisco.app.R;
import com.pisco.app.Utils.DownloadImageTask;
import com.pisco.app.Utils.UtilAnalytics;

public class PromotionPiscoFragment extends Fragment {

    public static final String ARG_OBJECT = "object";
    public static String PromTitulo = "PromTitulo";
    public static String PromPromocion = "PromPromocion";
    public static String PromImagen = "PromImagen";
    public static String PromSubTitulo = "PromSubTitulo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_promocion_pisco, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        ImageView imageView = view.findViewById(R.id.IDPromImagen);
        TextView tvTitle = view.findViewById(R.id.IDPromTitulo);
        TextView tvDescription = view.findViewById(R.id.IDPromPromocion);
        TextView tvSubtitle = view.findViewById(R.id.IDPromTag);
        new DownloadImageTask(imageView).execute(args.getString(PromImagen));
        tvTitle.setText(args.getString(PromTitulo));
        tvDescription.setText(args.getString(PromPromocion));
        tvSubtitle.setText(args.getString(PromSubTitulo));
        view.findViewById(R.id.IdPromocionPiscoCard).setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Inicio", "Clic", "Inicio - Promociones de Pisco");
            String url= AppDatabase.INSTANCE.userDao().getEntityUser().getLearnPisco();
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            view.getContext().startActivity(intent);
        });
    }

}