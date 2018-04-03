package com.example.lenovo.ptjob_company.com.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.ptjob_company.R;


/**我的信息类
 * Created by Administrator on 2016/8/24.
 */
public class FriendMessage extends RelativeLayout {

//    private int mheadImgRes;
//    private int mChatBackgroundRes;

    public ImageView mHeadImg;
    private TextView mChatMessage;


    public FriendMessage(Context context) {
      this(context,null);
    }

    public FriendMessage(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.friend_message, this, true);
        //初始化视图
        initView(view);
        //获得自定义属性
        obtainAttributes(context,attrs);

    }



    /**
     * 初始化视图
     */
    private void initView(View view) {
        mHeadImg= (ImageView) view.findViewById(R.id.friend_head_img);
        mChatMessage = (TextView) view.findViewById(R.id.friend_message);
    }


    /**
     * 设置头像
     */
    public void setHeadImg(int resId){
      mHeadImg.setImageResource(resId);
    }

    /**
     * 设置聊天背景
     */
    public void setChatBackground(int resId){
        mChatMessage.setBackgroundResource(resId);
    }

    /**
     * 设置文字
     * @param message
     */
    public void setMessage(String message){
        mChatMessage.setText(message);
    }

    //获得并设置自定义属性
    private void obtainAttributes(Context context, AttributeSet attrs) {

        TypedArray arr=context.obtainStyledAttributes(attrs,R.styleable.FriendMessage);

        String message=arr.getString(R.styleable.FriendMessage_friendMessageText);
        float messageSize=arr.getDimension(R.styleable.FriendMessage_friendMessageSize,16);
        int messageColor=arr.getColor(R.styleable.FriendMessage_friendMessageColor, Color.BLACK);
        int chatBackgroundRes=arr.getResourceId(R.styleable.FriendMessage_friendChatBackground,R.drawable.chat_friend_bg);
        int userHeadImgRes=arr.getResourceId(R.styleable.FriendMessage_friendHeadImg,R.drawable.person);

        arr.recycle();

        mHeadImg.setImageResource(userHeadImgRes);

        mChatMessage.setText(message);
        mChatMessage.setTextSize(messageSize);
        mChatMessage.setTextColor(messageColor);
        mChatMessage.setBackgroundResource(chatBackgroundRes);

    }
}
