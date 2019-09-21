package per.goweii.anypermission;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.checker.StandardChecker;
import com.yanzhenjie.permission.option.Option;

import java.lang.reflect.Field;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/3/19
 */
public class ContextHolder {

    private final Context mContext;
    private final Activity mActivity;
    private final Fragment mSupportFragment;
    private final android.app.Fragment mFragment;

    ContextHolder(@NonNull final Context context) {
        mContext = context;
        mActivity = null;
        mSupportFragment = null;
        mFragment = null;
    }

    ContextHolder(@NonNull final Activity activity) {
        mContext = null;
        mActivity = activity;
        mSupportFragment = null;
        mFragment = null;
    }

    ContextHolder(@NonNull final Fragment fragment) {
        mContext = null;
        mActivity = null;
        mSupportFragment = fragment;
        mFragment = null;
    }

    ContextHolder(@NonNull final android.app.Fragment fragment) {
        mContext = null;
        mActivity = null;
        mSupportFragment = null;
        mFragment = fragment;
    }

    public Option getOption() {
        hookAndPermission();
        if (mContext != null) {
            return AndPermission.with(mContext);
        } else if (mActivity != null) {
            return AndPermission.with(mActivity);
        } else if (mSupportFragment != null) {
            return AndPermission.with(mSupportFragment);
        } else if (mFragment != null) {
            return AndPermission.with(mFragment);
        }
        return null;
    }

    public Context getContext() {
        if (mContext != null) {
            return mContext;
        } else if (mActivity != null) {
            return mActivity;
        } else if (mSupportFragment != null) {
            return mSupportFragment.getContext();
        } else if (mFragment != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return mFragment.getContext();
            } else {
                return mFragment.getActivity();
            }
        }
        return null;
    }

    private void hookAndPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            hookAndPermissionDoubleChecker();
        }
    }

    private void hookAndPermissionDoubleChecker() {
        try {
            Class<?> andPermission = Class.forName("com.yanzhenjie.permission.checker.DoubleChecker");
            Field strictChecker = andPermission.getDeclaredField("STRICT_CHECKER");
            strictChecker.setAccessible(true);
            Object fieldObj = strictChecker.get(null);
            strictChecker.set(fieldObj, new StandardChecker());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
