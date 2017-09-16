package com.jmarkstar.s3.configchanges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import java.util.List;

/**
 * Created by jmarkstar on 9/15/17.
 */
public class StoreDataRetainedFragment extends Fragment {

    private List<MyDataObject> data;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //retener este fragment
        setRetainInstance(true);
    }

    public List<MyDataObject> getData() {
        return data;
    }

    public void setData(List<MyDataObject> data) {
        this.data = data;
    }
}
