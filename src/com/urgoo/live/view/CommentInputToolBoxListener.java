package com.urgoo.live.view;

/**
 * 回复帖子富媒体输入工具条事件监听器
 * 
 *
 */
public interface CommentInputToolBoxListener {
	
	/**
	 * 发送
	 * 
	 * @param inputMsg 输入框中的文本信息
	 */
	void onSend(CharSequence inputMsg);
}