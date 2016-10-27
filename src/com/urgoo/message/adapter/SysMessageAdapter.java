package com.urgoo.message.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.client.R;
import com.urgoo.message.model.MessageItem;
import com.urgoo.message.model.SysMessage;

import java.util.List;

/**
 * Created by bb on 2016/10/26.
 */
public class SysMessageAdapter extends UltimateViewAdapter<SysMessageAdapter.ViewHolder> {
    private Context context;
    private List<SysMessage> sysMessageList;
    private OnItemClickListener onItemClickListener;
    private int flag;

    public SysMessageAdapter(Context context, List<SysMessage> sysMessageList, int flag) {
        this.context = context;
        this.sysMessageList = sysMessageList;
        this.flag = flag;
    }

    public SysMessage getItem(int position) {
        return sysMessageList.get(position);
    }

    public void addData(List<SysMessage> sysMessageList) {
        for (SysMessage sysMessage : sysMessageList) {
            insert(this.sysMessageList, sysMessage, getAdapterItemCount());
        }
    }

    public void clear() {
        clear(sysMessageList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SysMessageAdapter.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public SysMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView;
        if (flag == 2) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultant_item, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sys_message, parent, false);
        }
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return sysMessageList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position < getAdapterItemCount()) {
            SysMessage sysMessage = getItem(position);
            holder.tvContent.setText(sysMessage.getContent111());
            holder.tvTitle.setText(sysMessage.getTitle111());
//            holder.sdvIcon.setImageURI(Uri.parse(sysMessage.getIcon111()));
            if (sysMessage.getUnread().equals("1")) {
                holder.ivUnread.setVisibility(View.GONE);
            } else {
                holder.ivUnread.setVisibility(View.VISIBLE);
            }

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(sysMessage.getIcon111()))
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setResizeOptions(new ResizeOptions(225, 225))
                    .build();

            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setImageRequest(request);
            controller.setOldController(holder.sdvIcon.getController());
            holder.sdvIcon.setController(controller.build());
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public class ViewHolder extends UltimateRecyclerviewViewHolder {
        public SimpleDraweeView sdvIcon;
        public TextView tvTitle;
        public TextView tvContent;
        public ImageView ivUnread;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvIcon = (SimpleDraweeView) itemView.findViewById(R.id.sdv_avatar);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_name);
                tvContent = (TextView) itemView.findViewById(R.id.tv_region);
                ivUnread = (ImageView) itemView.findViewById(R.id.iv_unread);
                if (onItemClickListener != null) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(v, getAdapterPosition());
                        }
                    });
                }
            }
        }
    }
}
