package com.mop.ui.utils;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mop.ui.fragment.HomeDrawerFragment;

import java.util.List;


public class FragmentUtil {

    public static void showFragment(FragmentManager fm, @IdRes int containerViewId,
                                    @NonNull Class fragmentClass,
                                    @Nullable Bundle args) {
        Fragment fragment = fm.findFragmentByTag(fragmentClass.getName());
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(fm, ft, fragmentClass.getName());
        if (fragment == null) {
            ft.add(containerViewId, fragmentClass, args, fragmentClass.getName());
        } else {
            fragment.setArguments(args);
            if (!fragment.isAdded()) {
                ft.add(containerViewId, fragment, fragmentClass.getName());
            }
            if (!fragment.isVisible()) {
                ft.show(fragment);
            }
        }
        ft.commitNow();
    }

    public static void preLoad(FragmentManager fm, @IdRes int containerViewId,
                               @NonNull Class fragmentClass,
                               @Nullable Bundle args) {
        Fragment fragment = fm.findFragmentByTag(fragmentClass.getName());
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(fm, ft, fragmentClass.getName());
        if (fragment == null) {
            ft.add(containerViewId, fragmentClass, args, fragmentClass.getName());
            ft.hide(fragment);
        }
    }

    private static void hideFragment(FragmentManager fm, FragmentTransaction ft, String tag) {
        List<Fragment> fragments = fm.getFragments();
        if (fragments == null) {
            return;
        }
        for (Fragment fragment : fragments) {
            if (fragment instanceof HomeDrawerFragment) {
                continue;
            }
            if (tag != null && tag.equals(fragment.getTag())) {
                continue;
            }
            if (fragment.isHidden()) {
                continue;
            }
            ft.hide(fragment);
        }
    }
}
