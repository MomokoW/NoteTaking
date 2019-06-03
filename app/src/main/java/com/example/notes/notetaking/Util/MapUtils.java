package com.example.notes.notetaking.Util;

import com.example.notes.notetaking.R;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static final Map<String, Integer> imageMap = new HashMap<String, Integer>() {
        {
            put("个人", R.mipmap.ic_geren);
            put("工作", R.mipmap.ic_gongzuo);
            put("旅游", R.mipmap.ic_lvyou);
            put("生活", R.mipmap.ic_shenghuo);
            put("未标签", R.mipmap.ic_none);
        }
    };
}
