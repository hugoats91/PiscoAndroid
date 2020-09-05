package com.pisco.app.Utils;

import android.app.AlertDialog;

import androidx.fragment.app.Fragment;

import java.util.Dictionary;
import java.util.Hashtable;

public class FragmentInstanceList {

    public static Dictionary<String, Fragment> dictionaryFragments = new Hashtable<>();
    public static Dictionary<String, AlertDialog> dictionaryAlertDialogs = new Hashtable<>();

    public FragmentInstanceList(Dictionary<String, Fragment> dictionaryFragments) {
        FragmentInstanceList.dictionaryFragments = dictionaryFragments;
    }

    public static void setFragmentInstances(String name,Fragment fragment){
        if ((FragmentInstanceList.dictionaryFragments.get(name)!=null)){
            FragmentInstanceList.dictionaryFragments.remove(name);
        }
        FragmentInstanceList.dictionaryFragments.put(name,fragment);
    }

    public static AlertDialog getDictionaryAlertDialog(String name) {
        return dictionaryAlertDialogs.get(name);
    }

    public static void setDictionaryAlertDialogInstances(String name,AlertDialog alertDialogs){
        if((FragmentInstanceList.dictionaryAlertDialogs.get(name)!=null)){
            FragmentInstanceList.dictionaryAlertDialogs.remove(name);
        }
        FragmentInstanceList.dictionaryAlertDialogs.put(name,alertDialogs);
    }

}