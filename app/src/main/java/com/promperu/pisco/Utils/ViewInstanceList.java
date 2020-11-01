package com.promperu.pisco.Utils;

import android.view.View;

import java.util.Dictionary;
import java.util.Hashtable;

public class ViewInstanceList {

    public static Dictionary<String,View> dictionaryViews=new Hashtable<>();

    public ViewInstanceList(Dictionary<String, View> dictionaryViews) {
        ViewInstanceList.dictionaryViews = dictionaryViews;
    }

    public static View getDictionaryViews(String name) {
        return dictionaryViews.get(name);
    }

    public static void setViewInstances(String name,View view){
        if ((ViewInstanceList.dictionaryViews.get(name) != null)) {
            ViewInstanceList.dictionaryViews.remove(name);
        }
        ViewInstanceList.dictionaryViews.put(name,view);
    }

}