package com.example.clicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class Back_DialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder_ = new AlertDialog.Builder(getActivity());
        builder_.setTitle(R.string.back_dialog_title_exit);

        builder_.setPositiveButton(R.string.back_dialog_exit_activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((GameFieldActivity) getActivity()).backDialogYes("123123");

            }
        });

        builder_.setNeutralButton(R.string.back_dialog_stay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((GameFieldActivity) getActivity()).backDialogNo();
            }
        });


        AlertDialog dialog = builder_.create();
        return dialog;

    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {// вызывается при выходе из диалога
        // не вызывается положительной кнопкой
        ((GameFieldActivity) getActivity()).backDialogNo();
        super.onDismiss(dialog);


    }
}
