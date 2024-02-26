package com.tencent.qcloud.ugckit.component.dialogfragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tencent.qcloud.ugckit.R;
import com.tencent.qcloud.ugckit.utils.ScreenUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ValidationDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ValidationDialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public static ValidationDialogFragment newInstance(String param1, String param2) {
        ValidationDialogFragment fragment = new ValidationDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
}