package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {


    public EarthquakeAdapter(@NonNull Context context, @NonNull List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }


        Earthquake currentItem = getItem(position);


        //设置mag格式，只显示一位小数
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        String magnitude = magnitudeFormat.format(currentItem.getmMagnitude());

        TextView magTextView = (TextView) listItemView.findViewById(R.id.mag);
        magTextView.setText(magnitude);


        // 为震级圆圈设置正确的背景颜色。
        // 从 TextView 获取背景，该背景是一个 GradientDrawable。
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // 根据当前的地震震级获取相应的背景颜色
        int magnitudeColor = getMagnitudeColor(currentItem.getmMagnitude());

        // 设置震级圆圈的颜色
        magnitudeCircle.setColor(magnitudeColor);


        //设置offset
        TextView offsetTextView = (TextView) listItemView.findViewById(R.id.offset);
        String offsetLocation = currentItem.getmLocation();
        int i = formatLocation(offsetLocation);

        if (i != -1) {
            i = i + 2;
            offsetTextView.setText(offsetLocation.substring(0, i));
        } else {
            offsetTextView.setText("Near the");
            i = 0;
        }

        //设置location
        TextView locationTextView = (TextView) listItemView.findViewById(R.id.location);
        locationTextView.setText(offsetLocation.substring(i));


        //时间格式的转换
        // 根据地震时间（以毫秒为单位）创建一个新的 Date 对象
        Date dateObject = new Date(currentItem.getmTimeInMilliseconds());

        // 找到视图 ID 为 date 的 TextView
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // 设置日期字符串的格式（即 "Mar 3, 1984"）
        String formattedDate = formatDate(dateObject);
        // 在该 TextView 中显示目前地震的日期
        dateView.setText(formattedDate);

        // 找到视图 ID 为 time 的 TextView
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // 设置时间字符串的格式（即 "4:30PM"）
        String formattedTime = formatTime(dateObject);
        // 在该 TextView 中显示目前地震的时间
        timeView.setText(formattedTime);

        return listItemView;
    }

    /**
     * 从 Date 对象返回格式化的日期字符串（即 "Mar 3, 1984"）。
     * 从 Date 对象返回格式化的时间字符串（即 "4:30 PM"）。
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * 将地点拆分。
     * 考虑到我们无法一次返回两条地址信息，但是写两个函数用来返回两条信息有点浪费。所以可以
     * 1.返回原字符串，但是在中间添加标志符号，如@，然后在getView中操作分割
     * 2.直接返回两个字符串分割的索引
     * 这里考虑方法2，但是又涉及一个问题，如format2需要添加near，但是这个不影响，因为不改变字符串，直接输出的时候加上near就可以了
     *
     * 同时考虑一下，这里直接传入的是字符串，实际上是传地址，会对原来的字符串更改
     *
     * 如果不利用函数来写，直接在getView中更改的话，采用stringsplit比较简单
     */

    /**
     * 考虑到我们无法一次返回两条地址信息
     * 这句话错了，我们可以同时返回两个字符串
     * String[] 不记得了么  SB LPG
     */

    /**
     * LOCATION_SEPARATOR = "of"
     * if (originalLocation.contains(LOCATION_SEPARATOR)) {
     * String[] parts = originalLocation.split(LOCATION_SEPARATOR);
     * locationOffset = parts[0] + LOCATION_SEPARATOR;
     * primaryLocation = parts[1];
     * } else {
     * locationOffset = getContext().getString(R.string.near_the);
     * primaryLocation = originalLocation;
     * }
     */

    private int formatLocation(String location) {

        //判断地点类型
        if (location.charAt(0) <= '9' && location.charAt(0) >= '0') {
            int i = location.indexOf("of");
            return i;
        } else {
            return -1;
        }
    }


    private int getMagnitudeColor(double mag) {

        //将mag从double强制转换为int，会直接截断小数点后的数据
        /*
        * 由于return的实际是资源值，这样显示的圆圈其实是没有颜色的，我们需要将id资源值转换为十六进制的颜色值
        switch ((int)mag){
            case 0:
            case 1: return R.color.magnitude1;
            case 2: return R.color.magnitude2;
            case 3: return R.color.magnitude3;
            case 4: return R.color.magnitude4;
            case 5: return R.color.magnitude5;
            case 6: return R.color.magnitude6;
            case 7: return R.color.magnitude7;
            case 8: return R.color.magnitude8;
            case 9: return R.color.magnitude9;
            default:return R.color.magnitude10plus;
        }
        */

        int magnitudeColorResourceId;
        switch ((int) mag) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}

