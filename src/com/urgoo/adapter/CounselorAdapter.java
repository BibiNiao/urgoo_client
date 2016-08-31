package com.urgoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.domain.CounselorInfo;
import com.urgoo.view.CircleImageView;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.urgoo.webviewmanage.BaseWebViewFragment;
import com.zw.express.tool.image.ImageLoaderUtil;

import java.util.ArrayList;

/**
 * Created by lijie on 2016/5/27.
 */
public class CounselorAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<CounselorInfo> mList;

    private imageLoadBusiness business = new imageLoadBusiness();

    public CounselorAdapter(Context context, ArrayList<CounselorInfo> list) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (mList != null && mList.size() > 0) ? mList
                .get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.counselot_item, parent, false);
            viewHolder.experience = (TextView) convertView.findViewById(R.id.workExperience);
            viewHolder.nationality = (TextView) convertView.findViewById(R.id.nationality);
            viewHolder.organization1 = (TextView) convertView.findViewById(R.id.organization1);
            viewHolder.organization2 = (TextView) convertView.findViewById(R.id.organization2);
            viewHolder.organization3 = (TextView) convertView.findViewById(R.id.organization3);
            viewHolder.circleImageView = (CircleImageView) convertView.findViewById(R.id.img_avatar);
            viewHolder.enName = (TextView) convertView.findViewById(R.id.counselorName);
            viewHolder.slogan = (TextView) convertView.findViewById(R.id.slogan);
            viewHolder.label1 = (TextView) convertView.findViewById(R.id.labelt1);
            viewHolder.label2 = (TextView) convertView.findViewById(R.id.labelt2);
            viewHolder.label3 = (TextView) convertView.findViewById(R.id.labelt3);
            viewHolder.p1 = (ImageView) convertView.findViewById(R.id.labeli1);
            viewHolder.p2 = (ImageView) convertView.findViewById(R.id.labeli2);
            viewHolder.p3 = (ImageView) convertView.findViewById(R.id.labeli3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BaseWebViewActivity.class);
                if (mList.get(position).getCounselorId() != null) {
                    intent.putExtra(BaseWebViewFragment.EXTRA_URL,
                            ZWConfig.URGOOURL_BASE + "001/001/client/counselorInfoContract?counselorId="
                                    + mList.get(position).getCounselorId());
                    v.getContext().startActivity(intent);
                } else Toast.makeText(mContext, "请检查网络后重试！", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.experience.setText("从业年数:" + mList.get(position).getWorkYear());
        viewHolder.nationality.setText(mList.get(position).getCountyName());
        Log.d("uuu", "getView: " + mList.get(position).getOrganization());
        if (mList.get(position).getOrganization() != null) {
            String[] split = mList.get(position).getOrganization().get(0).split("(,|，)");
            switch (split.length) {
                case 3:
                    viewHolder.organization3.setVisibility(View.VISIBLE);
                    viewHolder.organization3.setText(split[2]);
                case 2:
                    viewHolder.organization2.setVisibility(View.VISIBLE);
                    viewHolder.organization2.setText(split[1]);
                case 1:
                    viewHolder.organization1.setVisibility(View.VISIBLE);
                    viewHolder.organization1.setText(split[0]);
                    break;
                default:
                    viewHolder.organization3.setVisibility(View.GONE);
                    viewHolder.organization2.setVisibility(View.GONE);
                    viewHolder.organization1.setVisibility(View.GONE);
            }
        } else {
            viewHolder.organization3.setVisibility(View.GONE);
            viewHolder.organization2.setVisibility(View.GONE);
            viewHolder.organization1.setVisibility(View.GONE);
        }
        if (mList.get(position).getUserIcon() != null)
            business.imageLoadByURL(mList.get(position).getUserIcon(), viewHolder.circleImageView);
        viewHolder.enName.setText(mList.get(position).getEnName());
        viewHolder.slogan.setText(mList.get(position).getSlogan());
        if (mList.get(position).getLableList() != null) {
            viewHolder.p3.setVisibility(View.GONE);
            viewHolder.label3.setVisibility(View.GONE);
            viewHolder.p2.setVisibility(View.GONE);
            viewHolder.label2.setVisibility(View.GONE);
            viewHolder.p1.setVisibility(View.GONE);
            viewHolder.label1.setVisibility(View.GONE);

            switch (mList.get(position).getLableList().size()) {
                case 3:
                    viewHolder.p3.setVisibility(View.VISIBLE);
                    viewHolder.label3.setVisibility(View.VISIBLE);
                    viewHolder.label3.setText(mList.get(position).getLableList().get(2).getLableCnNAme());
                case 2:
                    viewHolder.p2.setVisibility(View.VISIBLE);
                    viewHolder.label2.setVisibility(View.VISIBLE);
                    viewHolder.label2.setText(mList.get(position).getLableList().get(1).getLableCnNAme());
                case 1:
                    viewHolder.p1.setVisibility(View.VISIBLE);
                    viewHolder.label1.setVisibility(View.VISIBLE);
                    viewHolder.label1.setText(mList.get(position).getLableList().get(0).getLableCnNAme());
                    break;
                default:
                    break;
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView experience;
        private TextView nationality;
        private TextView organization1;
        private TextView organization2;
        private TextView organization3;
        private CircleImageView circleImageView;

        private TextView enName;
        private TextView slogan;
        private ImageView p1;
        private TextView label1;
        private ImageView p2;
        private TextView label2;
        private ImageView p3;
        private TextView label3;
    }
}
