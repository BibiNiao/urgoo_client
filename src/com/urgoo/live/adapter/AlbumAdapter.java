package com.urgoo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.client.R;
import com.urgoo.live.model.Album;

import java.util.List;

/**
 * Created by bb on 2016/9/20.
 */
public class AlbumAdapter extends UltimateViewAdapter<AlbumAdapter.ViewHolder> {
    private Context context;
    private List<Album> albums;
    private OnItemClickListener onItemClickListener;

    public AlbumAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    public Album getItem(int position) {
        return albums.get(position);
    }

    public void addData(List<Album> albums) {
        for (Album album : albums) {
            insert(this.albums, album, getAdapterItemCount());
        }
    }

    public void clear() {
        clear(albums);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return albums.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < getAdapterItemCount()) {
            Album album = getItem(position);
            holder.sdvAlbum.setImageURI(Uri.parse(album.getCover()));
            holder.tvTitle.setText(album.getTitle());
            holder.tvDes.setText(album.getDes());
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
        public SimpleDraweeView sdvAlbum;
        public TextView tvTitle;
        public TextView tvDes;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                sdvAlbum = (SimpleDraweeView) itemView.findViewById(R.id.sdv_album);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvDes = (TextView) itemView.findViewById(R.id.tv_des);
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
