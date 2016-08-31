package com.urgoo.base;


import android.content.Intent;
import android.support.annotation.IdRes;
import android.view.View;

import com.urgoo.client.R;


public class HomeFragment extends BaseFragment {

    protected View view;

    protected void startActivityAnim() {
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.stay_remain);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        startActivityAnim();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        startActivityAnim();
    }


    protected <T extends View> T $(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }


}
