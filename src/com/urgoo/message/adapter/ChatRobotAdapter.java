package com.urgoo.message.adapter;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.urgoo.Interface.OnItemClickListener;
import com.urgoo.client.R;
import com.urgoo.data.SPManager;
import com.urgoo.message.activities.ChatRobotActivity;
import com.urgoo.message.biz.Robot;
import com.urgoo.view.MyListView;

import java.util.List;

/**
 * Created by bb on 2016/10/29.
 */
public class ChatRobotAdapter extends BaseAdapter {
    private static final String MESSAGE_TYPE_DATE = "1";
    private static final String MESSAGE_TYPE_TIME = "2";
    private static final String MESSAGE_TYPE_VERTICAL = "3";
    private static final String MESSAGE_TYPE_HORIZONTAL = "4";

    private static final int VIEW_TYPE_LEFT_CHAT_ITEM = 0;
    private static final int VIEW_TYPE_RIGHT_CHAT_ITEM = 1;

    private ChatRobotActivity chatPage;
    private String counselorId;
    private List<Robot> robots;
    protected LayoutInflater inflater;

    public ChatRobotAdapter(ChatRobotActivity activity, String counselorId, List<Robot> robots) {
        this.chatPage = activity;
        this.counselorId = counselorId;
        this.robots = robots;
        inflater = activity.getLayoutInflater();
    }

    public void addRobot(Robot robot) {
        int position = robots.indexOf(robot);
        if (position < 0) {
            robots.add(robot);
            notifyDataSetChanged();
        }
    }

    public int getViewTypeCount() {
        return 4;
    }

    public int getItemViewType(int position) {
        Robot robot = getItem(position);
        String type = robot.getStyle();
        if (type.equals(MESSAGE_TYPE_DATE)) {
            return robot.isMyself() ? VIEW_TYPE_RIGHT_CHAT_ITEM : VIEW_TYPE_LEFT_CHAT_ITEM;
        } else if (type.equals(MESSAGE_TYPE_TIME)) {
            return robot.isMyself() ? VIEW_TYPE_RIGHT_CHAT_ITEM : VIEW_TYPE_LEFT_CHAT_ITEM;
        } else if (type.equals(MESSAGE_TYPE_VERTICAL)) {
            return robot.isMyself() ? VIEW_TYPE_RIGHT_CHAT_ITEM : VIEW_TYPE_LEFT_CHAT_ITEM;
        } else if (type.equals(MESSAGE_TYPE_HORIZONTAL)) {
            return robot.isMyself() ? VIEW_TYPE_RIGHT_CHAT_ITEM : VIEW_TYPE_LEFT_CHAT_ITEM;
        } else {
            return -1;// invalid
        }
    }

    @Override
    public int getCount() {
        return robots.size();
    }

    @Override
    public Robot getItem(int position) {
        if (position < 0 || position >= robots.size()) {
            return null;
        } else {
            return robots.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取item类型
     */
    protected View createViewByMessage(Robot robot) {
        String type = robot.getStyle();
        if (type.equals(MESSAGE_TYPE_DATE)) {
            return robot.isMyself() ? inflater.inflate(R.layout.chatting_robot_item_right, null) : inflater
                    .inflate(R.layout.chatting_robot_date_item_left, null);
        } else if (type.equals(MESSAGE_TYPE_TIME)) {
            return robot.isMyself() ? inflater.inflate(R.layout.chatting_robot_item_right, null) : inflater
                    .inflate(R.layout.chatting_robot_time_item_left, null);
        } else if (type.equals(MESSAGE_TYPE_VERTICAL)) {
            return robot.isMyself() ? inflater.inflate(R.layout.chatting_robot_item_right, null) : inflater
                    .inflate(R.layout.chatting_robot_vertical_item_left, null);
        } else {
            return robot.isMyself() ? inflater.inflate(R.layout.chatting_robot_item_right, null) : inflater
                    .inflate(R.layout.chatting_robot_horizontal_item_left, null);
        }
    }

    /**
     * 点击后设置数据
     *
     * @param fposition
     * @param position
     */
    private void setDate(int fposition, int position) {
        robots.get(fposition).setShow(false);
        chatPage.getRobotAll(counselorId, robots.get(fposition).getListOption().get(position).getLevel(),
                robots.get(fposition).getListOption().get(position).getTarget(), robots.get(fposition).getListOption().get(position).getText(), robots.get(fposition).getListOption().get(position).getType());
    }


    @Override
    public View getView(final int fposition, View convertView, ViewGroup parent) {
        Robot robot = getItem(fposition);
        String type = robot.getStyle();
        final ChatMsgViewHolder holder;
        if (convertView == null) {
            holder = new ChatMsgViewHolder();
            convertView = createViewByMessage(robot);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (SimpleDraweeView) convertView.findViewById(R.id.sdv_avatar);
            System.out.println("======================" + fposition + " = " + robot.getStyle() + robot.getText());
            System.out.println("======================" + robot.getStyle());
            if (type.equals(MESSAGE_TYPE_DATE)) {
                holder.lvDate = (RecyclerView) convertView.findViewById(R.id.lv_date);
            } else if (type.equals(MESSAGE_TYPE_TIME)) {
                holder.lvTime = (RecyclerView) convertView.findViewById(R.id.lv_time);
            } else if (type.equals(MESSAGE_TYPE_VERTICAL)) {
                holder.lvVertical = (MyListView) convertView.findViewById(R.id.lv_vertical);
            } else {
                holder.lvHorizontal = (RecyclerView) convertView.findViewById(R.id.lv_horizontal);
            }
            convertView.setTag(holder);
        } else {
            System.out.println("//////////////////////" + fposition + " = " + robot.getStyle() + robot.getText());
            holder = (ChatMsgViewHolder) convertView.getTag();
        }
        if (type.equals(MESSAGE_TYPE_DATE)) {
            if (holder.lvDate != null) {
                DateAdpater dateAdpater = new DateAdpater(chatPage, robot.getListOption());
                holder.lvDate.setAdapter(dateAdpater);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(chatPage, LinearLayoutManager.HORIZONTAL, false);
                holder.lvDate.setLayoutManager(linearLayoutManager);
                dateAdpater.setOnItemClickLitener(new DateAdpater.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        if (robots.get(fposition).getShow()) {
                            for (int i = 0; i < robots.get(fposition).getListOption().size(); i++) {
                                robots.get(fposition).getListOption().get(i).setChecked(true);
                            }
                            chatPage.sendMessage(robots.get(fposition).getListOption().get(position).getText());
                            setDate(fposition, position);
                        }
                    }
                });
                dateAdpater.notifyDataSetChanged();
            }
        } else if (type.equals(MESSAGE_TYPE_TIME)) {
            if (holder.lvTime != null) {
                TimeAdpater timeAdpater = new TimeAdpater(chatPage, robot.getListOption());
                holder.lvTime.setAdapter(timeAdpater);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(chatPage, LinearLayoutManager.HORIZONTAL, false);
                holder.lvTime.setLayoutManager(linearLayoutManager);
                timeAdpater.setOnItemClickLitener(new TimeAdpater.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        if (robots.get(fposition).getShow()) {
                            for (int i = 0; i < robots.get(fposition).getListOption().size(); i++) {
                                robots.get(fposition).getListOption().get(i).setChecked(true);
                            }
                            chatPage.sendMessage(robots.get(fposition).getListOption().get(position).getText());
                            setDate(fposition, position);
                        }
                    }
                });
                timeAdpater.notifyDataSetChanged();
            }
        } else if (type.equals(MESSAGE_TYPE_VERTICAL)) {
            if (holder.lvVertical != null) {
                VerticalAdapter adapter = new VerticalAdapter(chatPage, robot.getListOption());
                holder.lvVertical.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        if (robots.get(fposition).getShow()) {
                            for (int i = 0; i < robots.get(fposition).getListOption().size(); i++) {
                                robots.get(fposition).getListOption().get(i).setChecked(true);
                            }
                            chatPage.sendMessage(robots.get(fposition).getListOption().get(position).getText());
                            setDate(fposition, position);
                        }
                    }
                });
                adapter.notifyDataSetChanged();
            }
        } else {
            if (holder.lvHorizontal != null) {
                HorizontalAdpater horizontalAdpater = new HorizontalAdpater(chatPage, robot.getListOption());
                holder.lvHorizontal.setAdapter(horizontalAdpater);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(chatPage, LinearLayoutManager.HORIZONTAL, false);
                holder.lvHorizontal.setLayoutManager(linearLayoutManager);
                horizontalAdpater.setOnItemClickLitener(new HorizontalAdpater.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        if (robots.get(fposition).getShow()) {
                            for (int i = 0; i < robots.get(fposition).getListOption().size(); i++) {
                                robots.get(fposition).getListOption().get(i).setChecked(true);
                            }
                            chatPage.sendMessage(robots.get(fposition).getListOption().get(position).getText());
                            setDate(fposition, position);
                        }
                    }
                });
                horizontalAdpater.notifyDataSetChanged();
            }
        }
        holder.text.setText(robot.getText());
        if (robot.isMyself()) {
            holder.icon.setImageURI(Uri.parse(SPManager.getInstance(chatPage).getUserIcon()));
        } else {
            holder.icon.setImageURI(Uri.parse(robot.getStaffIcon()));
        }
        return convertView;
    }

    private class ChatMsgViewHolder {
        public TextView text;
        public SimpleDraweeView icon;
        public MyListView lvVertical;
        public RecyclerView lvDate;
        public RecyclerView lvTime;
        public RecyclerView lvHorizontal;
    }
}
