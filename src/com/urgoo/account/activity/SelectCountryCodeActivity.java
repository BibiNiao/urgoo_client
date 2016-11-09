package com.urgoo.account.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.urgoo.account.adapter.SelectCountryCodeAdapter;
import com.urgoo.account.model.RegionCode;
import com.urgoo.account.model.RegionHelper;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.view.ErasableEditText;
import com.urgoo.view.PinnedSectionListView;
import com.urgoo.view.SideBar;
import com.zw.express.tool.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择国家页面
 */
public class SelectCountryCodeActivity extends NavToolBarActivity {

    private ErasableEditText editInput;
    private PinnedSectionListView listview;
    private SideBar sidebar; // 右边栏字母索引
    private SelectCountryCodeAdapter countryCodeAdapter;
    private TextView tvEmpty;
    private List<RegionCode> data = RegionHelper.getRegions();

    private List<RegionCode> filterData;
    private boolean isFilter;

    private RegionCode regionCode;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_country_code,
                null);
        editInput = (ErasableEditText) view.findViewById(R.id.edit_input);
        listview = (PinnedSectionListView) view.findViewById(R.id.listview);
        sidebar = (SideBar) view.findViewById(R.id.sidebar);
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty);
        editInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String country = editInput.getText().toString();
                    if (!TextUtils.isEmpty(country)) {
                        if (filterData.size() == 0) {
                            listview.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editInput.getWindowToken(), 0);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        initViews();
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavTitleText("选择国家");
        editInput.setHint("输入国家中文或拼音名称");
        initView();
    }

    private void initViews() {
        countryCodeAdapter = new SelectCountryCodeAdapter(this, data);
        listview.setAdapter(countryCodeAdapter);
        countryCodeAdapter.initAdapter();
        editInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        editInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 监听点击输入框右边清理按钮
        editInput.setClearTextListener(new ErasableEditText.OnClearTextListener() {

            @Override
            public void onClear() {
                Util.closeSoftKeyBoard(SelectCountryCodeActivity.this);
            }
        });
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (isFilter) {
                    regionCode = filterData.get(position);
                } else {
                    if (listview.getHeaderViewsCount() > 0) { // 如果有header
                        regionCode = countryCodeAdapter.getItem(position - 1);
                    } else {
                        regionCode = countryCodeAdapter.getItem(position);
                    }

                }
                if (regionCode.type == RegionCode.ITEM) {
                    Bundle bundle = new Bundle();
                    bundle.putString("code", regionCode.getCode());
                    bundle.putString("name", regionCode.getName());
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    Util.closeSoftKeyBoard(SelectCountryCodeActivity.this);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 字母首次出现的位置
                int position = countryCodeAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    if (listview.getHeaderViewsCount() > 0) // 如果有header
                        listview.setSelection(position + 1);
                    else
                        listview.setSelection(position);
                }

            }
        });
        listview.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Util.closeSoftKeyBoard(SelectCountryCodeActivity.this);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == Constants.REQUEST_CODE_SELECT_CITY) {
//                Intent intent = new Intent();
//                intent.putExtras(data.getExtras());
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        }
//    }

    /**
     * 根据输入框中关键字来过滤数据并更新ListView
     *
     * @param filterStr 搜索关键字
     */
    private void filterData(String filterStr) {
        filterData = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterData = data;
            isFilter = false;
        } else {
            filterData.clear();
            for (RegionCode info : data) {
                String name = info.getName();
                String firstLetterPY = info.getFirstLetterPY();
                String py = info.getCountryNamePY();
                if (!TextUtils.isEmpty(name)) {
                    if (name.contains(filterStr.toString())
                            || firstLetterPY
                            .contains(filterStr.toString()
                                    .toLowerCase())
                            || py.contains(filterStr.toString()
                            .toLowerCase())) {
                        filterData.add(info);
                    }
                }
            }
            isFilter = true;
        }
        if (filterData.size() > 0){
            listview.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
        countryCodeAdapter.updateData(filterData);
    }

    @Override
    protected void onNavLeftClick(View v) {
        Util.closeSoftKeyBoard(SelectCountryCodeActivity.this);
        super.onNavLeftClick(v);
    }

}
