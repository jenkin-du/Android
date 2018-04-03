package com.android.djs.useimooc_asyncloading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


/**
 * 自定义新闻列表适配器
 */
public class newsAdapter extends BaseAdapter implements AbsListView.OnScrollListener{


    private List<NewsBean> newsList;

    private LayoutInflater inflater;

    private ImageLoader imageLoader;

    private  boolean isFirstIn;

    private  int start,end;        //定义listview可见的第一项和最后一项的位置

    public static String[] urls;     //定义一个url数组，用于保存所有的图片url地址，用于listView优化处理

    public newsAdapter(List<NewsBean> newsList,Context context,ListView listView) {
        this.newsList = newsList;
        inflater= LayoutInflater.from(context);

        this.imageLoader=new ImageLoader(listView);

        //将newsList中的url赋给url数组
        urls=new String[newsList.size()];
        for (int i=0;i<newsList.size();i++){
            urls[i]=newsList.get(i).getImageURL();
        }

        listView.setOnScrollListener(this);

        isFirstIn=true;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * 将xml布局文件绘制到视图上，并将数据集合里对应项的数据显示在视图上
     * @param position        数据集合中的位置属性
     * @param convertView       需要被绘制的视图
     * @param parent         装载单个视图的List父容器
     * @return         返回每一数据向的视图
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        //文艺式的加载布局
        //通过创建viewHolder类，判断列表的布局是否绘制，如果没有绘制，
        //就新建一个ViewHolder类装载布局文件中实例化的对象
        //然后绘制布局，最后将已实例化的布局元素保存起来，下次绘制时直接拿来用，以减小系统开销
        if (convertView==null){
             holder=new ViewHolder();

            //将布局文件绘制到列表元素的视图上
            convertView=inflater.inflate(R.layout.item,null);

            //通过需要转换的视图找到布局文件中相应的布局元素实例
            holder.icon= (ImageView) convertView.findViewById(R.id.image);
            holder.tvTitle= (TextView) convertView.findViewById(R.id.tv_content);
            holder.tvContent= (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);    //将已经实例化的布局元素保存起来
        }else {
                holder= (ViewHolder) convertView.getTag();    //获得已经实例化的布局元素实例
        }

              //将对应列表中的数据加载到布局文件中,显示默认图片
        holder.icon.setImageResource(R.mipmap.ic_launcher);

        //在多线程中将对应图标加载上
        imageLoader.showImageByThread(holder.icon,newsList.get(position).getImageURL());

        //将imageView和对应url绑定起来，可以解决listView缓存在下一次加载图片时出现错乱的现象
        holder.icon.setTag(newsList.get(position).getImageURL());


        holder.tvTitle.setText(newsList.get(position).getTitle());
        holder.tvContent.setText(newsList.get(position).getContent());

        return convertView;
    }


    /**
     * 此方法在listview状态改变时调用
     * @param view   视图
     * @param scrollState    滚动状态
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        //如果列表停止滚动，就加载可见项，否则就停止加载
        if (scrollState==SCROLL_STATE_IDLE){

            imageLoader.loadImages(start,end);
        }else {
            imageLoader.cancellAllTask();
        }
    }


    /**
     *此方法在listview滚动过程中一直调用
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        start=firstVisibleItem;
        end=firstVisibleItem+visibleItemCount;

        //第一次显示调用
        if (isFirstIn&&visibleItemCount>0){
            imageLoader.loadImages(start,end);
            isFirstIn=false;
        }
    }

    class ViewHolder{
        public TextView tvTitle,tvContent;
        public ImageView icon;
    }
}
