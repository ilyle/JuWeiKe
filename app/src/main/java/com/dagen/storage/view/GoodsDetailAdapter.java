package com.dagen.storage.view;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dagen.storage.R;
import com.dagen.storage.activity.WareHousingEntryInsertActivity;

import java.lang.reflect.Field;
import java.util.List;

public class GoodsDetailAdapter extends AbstractPanelListAdapter {

    private ContentAdapter mContentAdapter;

    public GoodsDetailAdapter(Context context, PanelListLayout pl_root, ListView lv_content,
                              ContentAdapter contentAdapter) {
        super(context, pl_root, lv_content);
        this.mContentAdapter = contentAdapter;
    }

    @Override
    protected BaseAdapter getContentAdapter() {
        return mContentAdapter;
    }

    static class ContentAdapter extends ArrayAdapter {

        private Context mContext;
        private List<WareHousingEntryInsertActivity.AdapterBean> mData;
        private List<String> mRawData;
        private int mCurrLine = -1;
        private EditText mCurrEdt;
        private WareHousingEntryInsertActivity.AdapterListBean mCurrAdapterListBean;

        ContentAdapter(@NonNull Context context) {
            super(context, R.layout.item_goods);
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView,
                            @NonNull final ViewGroup parent) {
            final View view;
            final ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_goods, parent, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
                for (int i = 0; i < mRawData.size() - 1; i++) {
                    viewHolder.layouts[i].setVisibility(View.VISIBLE);
                }
                for (int i = mRawData.size() - 1; i < 8; i++) {
                    viewHolder.layouts[i].setVisibility(View.GONE);
                }
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            WareHousingEntryInsertActivity.AdapterBean data = mData.get(position);
            for (final WareHousingEntryInsertActivity.AdapterListBean adapterListBean : data.adapterList) {
                int i = 0;
                for (; i < mRawData.size(); i++) {
                    String rawStr = mRawData.get(i);
                    if (rawStr.equals(adapterListBean.size)) {
                        break;
                    }
                }
                viewHolder.tvs[i].setText(String.valueOf(adapterListBean.sku));
                if (adapterListBean.custom >= 0) {
                    viewHolder.ets[i].setText(String.valueOf(adapterListBean.custom));
                } else {
                    viewHolder.ets[i].setText("");
                }
                final int finalI = i;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.ets[i].setShowSoftInputOnFocus(false);
                }
                viewHolder.ets[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            mCurrLine = position;
                            mCurrEdt = viewHolder.ets[finalI];
                            mCurrAdapterListBean = adapterListBean;
                        } else if (view == mCurrEdt) {
                            getCurr();
                        }
                    }
                });
                viewHolder.ets[i].setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event.getAction() == KeyEvent.ACTION_UP) {
                                int next = finalI + 1;
                                int visibleIndex = 0;
                                for (int i = viewHolder.ets.length - 1; i >= 0; i--) {
                                    if (viewHolder.ets[i].isShown()) {
                                        visibleIndex = i;
                                        break;
                                    }
                                }
                                if (next > visibleIndex) {
                                    int viewIndex = parent.indexOfChild(view) + 1;
                                    if (viewIndex < parent.getChildCount()) {
                                        View nextView = parent.getChildAt(viewIndex);
                                        if (nextView.getTag() instanceof ViewHolder) {
                                            ((ViewHolder) nextView.getTag()).ets[0].requestFocus();
                                        }
                                    }
                                } else {
                                    viewHolder.ets[next].requestFocus();
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                });
                viewHolder.ets[i].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                                  int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        boolean setValue = false;
                        int etTotalCount = 0;
                        for (TextView tv : viewHolder.ets) {
                            String str = tv.getText().toString();
                            if (TextUtils.isEmpty(str)) {
                                continue;
                            }
                            setValue = true;
                            etTotalCount +=
                                    Integer.valueOf(str);
                        }
                        if (setValue) {
                            viewHolder.etTotal.setText(String.valueOf(etTotalCount));
                        } else {
                            viewHolder.etTotal.setText("");
                        }
                    }
                });
            }
            boolean setValue = false;
            int tvTotalCount = 0;
            for (TextView tv : viewHolder.tvs) {
                String str = tv.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    continue;
                }
                setValue = true;
                tvTotalCount += Integer.valueOf(str);
            }
            if (setValue) {
                viewHolder.tvTotal.setText(String.valueOf(tvTotalCount));
            }
            return view;
        }

        public void setData(List<WareHousingEntryInsertActivity.AdapterBean> data, List<String> rawData) {
            this.mData = data;
            this.mRawData = rawData;
            notifyDataSetChanged();
        }

        public List<WareHousingEntryInsertActivity.AdapterBean> getData() {
            return mData;
        }

        public int getCurr() {
            if (mCurrEdt != null && mCurrAdapterListBean != null) {
                String str = mCurrEdt.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    mCurrAdapterListBean.custom = -1;
                } else {
                    mCurrAdapterListBean.custom = Integer.valueOf(str);
                }
                mCurrEdt = null;
                mCurrAdapterListBean = null;
            }
            return mCurrLine;
        }

        public void clear() {
            mCurrEdt = null;
            mCurrAdapterListBean = null;
            List<WareHousingEntryInsertActivity.AdapterBean> data = getData();
            for (WareHousingEntryInsertActivity.AdapterBean datum : data) {
                for (WareHousingEntryInsertActivity.AdapterListBean adapterListBean : datum.adapterList) {
                    adapterListBean.custom = -1;
                }
            }
            notifyDataSetChanged();
        }

        private void performNext() {
            InputConnection inputConnection = getInputConnection();
            if (inputConnection != null) {
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_NEXT);
            }
        }

        private InputConnection getInputConnection() {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            Field mServedInputConnectionWrapperField;
            try {
                mServedInputConnectionWrapperField = InputMethodManager.class
                        .getDeclaredField("mServedInputConnectionWrapper");
                mServedInputConnectionWrapperField.setAccessible(true);
                Object curinputMethod = mServedInputConnectionWrapperField.get(inputMethodManager);
                InputConnection inputConnection = (InputConnection) curinputMethod.getClass()
                        .getMethod("getInputConnection").invoke(curinputMethod);
                return inputConnection;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private class ViewHolder {
            private View[] layouts = new View[8];
            private EditText[] ets = new EditText[8];
            private TextView[] tvs = new TextView[8];
            private TextView etTotal;
            private TextView tvTotal;

            ViewHolder(View itemView) {
                layouts[0] = itemView.findViewById(R.id.layout_01);
                layouts[1] = itemView.findViewById(R.id.layout_02);
                layouts[2] = itemView.findViewById(R.id.layout_03);
                layouts[3] = itemView.findViewById(R.id.layout_04);
                layouts[4] = itemView.findViewById(R.id.layout_05);
                layouts[5] = itemView.findViewById(R.id.layout_06);
                layouts[6] = itemView.findViewById(R.id.layout_07);
                layouts[7] = itemView.findViewById(R.id.layout_08);
                ets[0] = itemView.findViewById(R.id.id_et_01);
                ets[1] = itemView.findViewById(R.id.id_et_02);
                ets[2] = itemView.findViewById(R.id.id_et_03);
                ets[3] = itemView.findViewById(R.id.id_et_04);
                ets[4] = itemView.findViewById(R.id.id_et_05);
                ets[5] = itemView.findViewById(R.id.id_et_06);
                ets[6] = itemView.findViewById(R.id.id_et_07);
                ets[7] = itemView.findViewById(R.id.id_et_08);
                tvs[0] = itemView.findViewById(R.id.id_tv_01);
                tvs[1] = itemView.findViewById(R.id.id_tv_02);
                tvs[2] = itemView.findViewById(R.id.id_tv_03);
                tvs[3] = itemView.findViewById(R.id.id_tv_04);
                tvs[4] = itemView.findViewById(R.id.id_tv_05);
                tvs[5] = itemView.findViewById(R.id.id_tv_06);
                tvs[6] = itemView.findViewById(R.id.id_tv_07);
                tvs[7] = itemView.findViewById(R.id.id_tv_08);
                etTotal = itemView.findViewById(R.id.id_et_total);
                tvTotal = itemView.findViewById(R.id.id_tv_total);
            }
        }
    }
}

