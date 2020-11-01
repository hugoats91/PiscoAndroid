package com.promperu.pisco.Screen;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.promperu.pisco.Entity.ScreenItem;
import com.promperu.pisco.R;
import com.promperu.pisco.Utils.ViewInstanceList;

public class ItemOnBoardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_on_board, container, false);
        ViewInstanceList.setViewInstances("on-item-board-fragment", view);
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        TextView tvMaxIndex = view.findViewById(R.id.tvMaxIndex);
        TextView tvCurrentIndex = view.findViewById(R.id.tvCurrentIndex);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        TextView tvSeparator = view.findViewById(R.id.tvSeperator);
        ImageView ivBoard = view.findViewById(R.id.ivBoard);

        ScreenItem item  = args.getParcelable("item");
        if(item!=null){
            ivBoard.setImageResource(item.getScreenImg());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvMessage.setText(Html.fromHtml(item.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            }else{
                tvMessage.setText(Html.fromHtml(item.getDescription()));
            }
            tvCurrentIndex.setText(item.getPosition()+"");
            tvMaxIndex.setText("2");
            tvMessage.setTextColor(Color.parseColor(item.getColor()));
            tvMaxIndex.setTextColor(Color.parseColor(item.getColor()));
            tvCurrentIndex.setTextColor(Color.parseColor(item.getColor()));
            tvSeparator.setTextColor(Color.parseColor(item.getColor()));
        }
    }

}