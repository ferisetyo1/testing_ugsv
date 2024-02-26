package com.tencent.qcloud.ugckit.component.dialogfragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.qcloud.ugckit.R;
import com.tencent.qcloud.ugckit.utils.ScreenUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ValidationDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ValidationDialogFragment extends DialogFragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MESSAGE = "message";
    private static final String BTN_POSITIVE = "btn_positive";
    private static final String BTN_NEGATIVE = "btn_negative";

    // TODO: Rename and change types of parameters
    private String txtMsg;
    private String txtBtnPositive;
    private String txtBtnNegative;
    private OnValidationListener listener;

    public ValidationDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ValidationDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ValidationDialogFragment newInstance(String param1, String param2, String param3) {
        ValidationDialogFragment fragment = new ValidationDialogFragment();
        Bundle args = new Bundle();
        args.putString(MESSAGE, param1);
        args.putString(BTN_POSITIVE, param2);
        args.putString(BTN_NEGATIVE, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null)
                window.setLayout((int) (ScreenUtils.getScreenWidth(dialog.getContext()) * 0.9),//设置宽度最小为 90%
                        WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * 去掉标题栏
     */
    private void setDialogStyle() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            txtMsg = getArguments().getString(MESSAGE);
            txtBtnPositive = getArguments().getString(BTN_POSITIVE);
            txtBtnNegative = getArguments().getString(BTN_NEGATIVE);
        }
    }

    private void dismissDialog() {
        try {
            dismiss();
        } catch (Exception e) {
            if (getFragmentManager() != null && isAdded()) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(this);
                transaction.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setDialogStyle();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_validation_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null){
            ((TextView) view.findViewById(R.id.tv_message)).setText(txtMsg);
            ((Button) view.findViewById(R.id.btn_positive)).setText(txtBtnPositive);
            ((Button) view.findViewById(R.id.btn_negative)).setText(txtBtnNegative);
        }
        ((Button) view.findViewById(R.id.btn_positive)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btn_negative)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_positive) {
            if (listener!=null){
                listener.onClickPositive();
            }
            dismissDialog();
        } else if (view.getId() == R.id.btn_negative) {
            dismissDialog();
        }
    }

    public void setListener(OnValidationListener listener) {
        this.listener = listener;
    }

    public void showDialog(){
        getDialog().show();
    }


    public interface OnValidationListener {
        void onClickPositive();
    }
}