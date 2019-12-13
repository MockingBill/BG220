package com.illidan.dengqian.bg220.tool_bean;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.illidan.dengqian.bg220.MainActivity;
import com.illidan.dengqian.bg220.R;
import com.illidan.dengqian.bg220.UI.DrawErrView;
import com.illidan.dengqian.bg220.UI.DrawHookView;
import com.illidan.dengqian.bg220.UI.DrawRadiusView;

import java.util.Map;

public class checkListAdapteer extends BaseAdapter {


    public String check_key[];






    /**
     * 控制ListView的Item的数量
     * @return
     */

    @Override
    public int getCount() {


        return checkBean.check_title.size();
    }

    /**
     * 控制ListView的某些方法返回的Object数据.
     * 例如ListView的getItemAtPosition()方法. 通过位置索引得到数据对象, 即该方法返回的Object对象
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * 每次点击item都会回调该方法. 同样是为了ListView的getItemIdAtPosition()方法能够得到item的id
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 控制ListView的Item的视图显示
     *
     * @param position 当前显示的视图位置
     * @param convertView 缓存的视图. 用于复用item
     * @param parent 父容器布局
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(MainActivity.context, R.layout.check_list_item, null);

        // 根据传入的数据进行修改
        TextView  check_label= view.findViewById(R.id.check_label);
        TextView  check_value=view.findViewById(R.id.check_value);
        check_label.setText(checkBean.check_title.get(position).get(checkBean.lable_tag));
        check_value.setText(checkBean.check_title.get(position).get(checkBean.value_tag));
        return view;
    }
}
