package com.example.luigidarco.myfit.miband;

public interface ActionCallback {

    void onSuccess(Object data);

    void onFailure(int errorCode, String msg);
}
