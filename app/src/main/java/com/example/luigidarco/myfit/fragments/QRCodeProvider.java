package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.managers.NetworkManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class QRCodeProvider extends Fragment {

    ImageView qrCodeView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.qr_code_provider, container, false);

        qrCodeView = view.findViewById(R.id.qr_code_view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String url = getResources().getString(R.string.url_server) + "/qrcode/generate";
        NetworkManager.getImage(getActivity(), url, qrCodeView);
    }
}
