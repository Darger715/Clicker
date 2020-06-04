package com.example.clicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class GameOver_DialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.back_dialog_title_gameOver);

        builder.setPositiveButton(R.string.back_dialog_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((GameFieldActivity) getActivity()).gameOver_DialogYes("123123");

            }
        });

        builder.setNeutralButton(R.string.back_dialog_try_again, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((GameFieldActivity) getActivity()).gameOver_DialogNo();
            }
        });


        AlertDialog dialog = builder.create();
        return dialog;

    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {// вызывается при выходе из диалога
        // не вызывается положительной кнопкой
        ((GameFieldActivity) getActivity()).gameOver_DialogNo();

    }


}