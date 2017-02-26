package com.tgs.tubik.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tgs.tubik.TubikApp;

public class BaseFragment extends Fragment {
    protected TubikApp mApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (TubikApp)getActivity().getApplicationContext();
    }

    protected void fail(Throwable t) {
        if (getActivity() != null)
            if (t.getMessage() != null)
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
