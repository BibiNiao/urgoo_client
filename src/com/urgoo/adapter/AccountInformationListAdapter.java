package com.urgoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.urgoo.client.R;
import com.urgoo.domain.OtherInformationEntity;
import com.zw.express.tool.image.ImageCacheSrcInfo;
import com.zw.express.tool.image.ImageWorker;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class AccountInformationListAdapter extends BaseAdapter {





    private Context mContext;
    private List<Object> mInfoList;
    private int mInfoType = 0;

    public AccountInformationListAdapter(Context context, List<Object> mInfoList,
                                      int mInfoType) {
        this.mContext = context;
        this.mInfoList = mInfoList;
        this.mInfoType = mInfoType;
    }

    @Override
    public int getCount() {
        return mInfoList != null ? mInfoList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (mInfoList != null && mInfoList.size() > 0) ? mInfoList
                .get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Object o = mInfoList.get(position);
        int num = getCount();
        switch (mInfoType) {
            case 0:
                final OtherInformationEntity qb = ( OtherInformationEntity) o;
                ViewHolderGNQB vhq = null;
                if (view == null) {
                    vhq = new ViewHolderGNQB();
                    view = LayoutInflater.from(mContext).inflate(
                            R.layout.account_information_item, null);
                   /* vhq.qbLayout = (LinearLayout) view
                            .findViewById(R.id.infoqb_lnlayout);*/

                    vhq.title= (TextView) view.findViewById(R.id.title);
                    vhq.content = (TextView) view.findViewById(R.id.content);
                    vhq.txtqbtime = (TextView) view.findViewById(R.id.time);
                    view.setTag(vhq);
                } else {
                    vhq = (ViewHolderGNQB) view.getTag();

                }

                vhq.title.setText(qb.getTitle());
                vhq.content.setText(qb.getContent());
                vhq.txtqbtime
                        .setText(qb.getInsertDatetime());

                  if(qb.getUnread().equals("2")){
                    vhq.title.setTextColor(0xff434343);
                    vhq.content.setTextColor(0xff434343);
                    vhq.txtqbtime.setTextColor(0xff434343);

                }else{
                      vhq.title.setTextColor(0xff9b9b9b);
                      vhq.content.setTextColor(0xff9b9b9b);
                      vhq.txtqbtime.setTextColor(0xff9b9b9b);
                  }


                break;
            case 1:
                break;
            default: {
                break;
            }
        }
        return view;
    }

    class ViewHolderGNQB {
        LinearLayout qbLayout;
        ImageView imgqb;
        TextView title;
        TextView content;
        TextView txtqbtime;
    }

    private void imageLoad(String url, final ImageView img) {
        ImageCacheSrcInfo icsi = new ImageCacheSrcInfo(url, 84, 84);
        ImageWorker.newInstance(mContext).loadBitmap(icsi, img);
    }
}
