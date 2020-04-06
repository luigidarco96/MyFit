package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.managers.NetworkManager;
import com.example.luigidarco.myfit.managers.PermissionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class QRCodeScanner extends Fragment {

    private final String TAG = "MYFITAPP";

    CodeScanner codeScanner;
    CodeScannerView codeScannerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.qr_code_scanner, container, false);

        codeScannerView = view.findViewById(R.id.scanner_view);

        codeScanner = new CodeScanner(getActivity(), codeScannerView);
        codeScanner.setDecodeCallback(decodeCallback);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PermissionManager.checkCamera(getActivity())) {
            PermissionManager.requestCamera(getActivity());
        } else {
            codeScanner.startPreview();
        }
    }

    private DecodeCallback decodeCallback = result -> {
        String code = result.getText();
        String url = getResources().getString(R.string.url_server) + "/qrcode/verify";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);
            NetworkManager.makePostJsonObjRequest(
                    getActivity(),
                    url,
                    jsonObject,
                    new NetworkCallback() {
                        @Override
                        public void onSuccess(JSONObject object) {
                            NavHostFragment.findNavController(getParentFragment()).navigateUp();
                        }

                        @Override
                        public void onError(String error) {
                            Log.e(TAG, "Error: " + error);
                            new MaterialAlertDialogBuilder(getActivity())
                                    .setTitle("QR Scanner")
                                    .setMessage("Something was wrong. Try again")
                                    .setPositiveButton("Try again", (dialogInterface, i) -> {
                                        codeScanner.startPreview();
                                        dialogInterface.dismiss();
                                    })
                                    .setNegativeButton("Exit", (dialogInterface, i) -> {
                                        NavHostFragment.findNavController(getParentFragment()).navigateUp();
                                    })
                                    .show();
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };


}
