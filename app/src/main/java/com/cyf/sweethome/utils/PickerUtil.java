package com.cyf.sweethome.utils;

import android.content.Context;
import android.graphics.Color;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cyf.sweethome.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/9/009.
 */
public class PickerUtil {
//    private static List<ProvInfo> options1Items = new ArrayList<>();
//    private static ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
//    private static ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
//    private static List<ChooseType> singleData = new ArrayList<>();

//    public static void initAddress(ArrayList<ProvInfo> provInfoList) {
//        options1Items.clear();
//        options2Items.clear();
//        options3Items.clear();
//        for (ProvInfo provInfo : provInfoList) {
//            options1Items.add(provInfo);
//            ArrayList cityInfoList = new ArrayList<String>();
//            ArrayList<ArrayList<String>> areaInfoListList = new ArrayList<>();
//            for (CityInfo cityInfo : provInfo.getLists()) {
//                cityInfoList.add(cityInfo.getTitle());
//                ArrayList<String> areaInfoList = new ArrayList<>();
//                if (cityInfo.getLists() != null) {
//                    for (AreaInfo areaInfo : cityInfo.getLists()) {
//                        areaInfoList.add(areaInfo.getTitle());
//                    }
//                }
//                areaInfoListList.add(areaInfoList);
//            }
//            options2Items.add(cityInfoList);
//            options3Items.add(areaInfoListList);
//        }
//    }
//
//    public static void showAddress(Context context, OnOptionsSelectListener onOptionsSelectListener) {
//        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, onOptionsSelectListener)
//                .setTitleText("城市选择")
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK)
//                .setContentTextSize(20)
//                .build();
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
//        pvOptions.show();
//    }
//
//    public static void initChooseType(ArrayList<ChooseType> data) {
//        singleData.clear();
//        singleData.addAll(data);
//    }
//
//    public static void showChooseType(Context context, String title, OnOptionsSelectListener onOptionsSelectListener) {
//        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, onOptionsSelectListener)
//                .setTitleText(title)
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK)
//                .setContentTextSize(20)
//                .build();
//        pvOptions.setPicker(singleData);
//        pvOptions.show();
//    }

    public static void showUserInfoPicker(final Context context, ArrayList<UserInfo> userInfo, OnOptionsSelectListener listener) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < userInfo.size(); i++) {
            data.add(userInfo.get(i).getTitle());
        }
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(context, listener)
                .setTitleText("选择身份")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(data);
        pvOptions.show();
    }

    public static void showSexPicker(final Context context, OnOptionsSelectListener listener) {
        List<String> sexData = new ArrayList<>();
        sexData.add("男");
        sexData.add("女");
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(context, listener)
                .setTitleText("选择性别")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(sexData);
        pvOptions.show();
    }

    public static void showTimerPicker(final Context context, OnTimeSelectListener listener) {
        TimePickerView pvTime = new TimePickerBuilder(context, listener)
                .setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .setTitleText("选择预约时间")
                .setSubmitText("确定")
                .setCancelText("取消")
                .build();
        pvTime.show();
    }

    public static void showTimerPickerYM(final Context context, OnTimeSelectListener listener) {
        TimePickerView pvTime = new TimePickerBuilder(context, listener)
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setSubmitText("确定")
                .setCancelText("取消")
                .setTitleText("选择预约时间")
                .build();
        pvTime.show();
    }
}
