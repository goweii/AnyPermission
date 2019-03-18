package per.goweii.anypermission;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.option.Option;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/3/16
 */
public class AnyPermission {

    private final Context mContext;
    private final Option mOption;

    public static AnyPermission with(@NonNull final Context context) {
        return new AnyPermission(context);
    }

    public static AnyPermission with(@NonNull final Activity activity) {
        return new AnyPermission(activity);
    }

    public static AnyPermission with(@NonNull final Fragment fragment) {
        return new AnyPermission(fragment);
    }

    public static AnyPermission with(@NonNull final android.app.Fragment fragment) {
        return new AnyPermission(fragment);
    }

    private AnyPermission(final Context context){
        mContext = context;
        mOption = AndPermission.with(context);
    }

    private AnyPermission(final Activity activity){
        mContext = activity;
        mOption = AndPermission.with(activity);
    }

    private AnyPermission(final Fragment fragment){
        mContext = fragment.getContext();
        mOption = AndPermission.with(fragment);
    }

    private AnyPermission(final android.app.Fragment fragment){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mContext = fragment.getContext();
        } else {
            mContext = fragment.getActivity();
        }
        mOption = AndPermission.with(fragment);
    }

    public String name(String permission) {
        return Permission.transformText(mContext, permission).get(0);
    }

    public Uri fileUri(File file) {
        return AndPermission.getFileUri(mContext, file);
    }

    public RuntimeRequester runtime(int requestCodeWhenGoSetting) {
        return new RuntimeRequester(mOption, mContext, requestCodeWhenGoSetting);
    }

    public InstallRequester install(File apkFile) {
        return new InstallRequester(mOption, apkFile);
    }

    public OverlayRequester overlay() {
        return new OverlayRequester(mOption);
    }

    @Deprecated
    public SettingRequester setting() {
        return new SettingRequester(mOption);
    }

    public NotificationShowRequester notificationShow() {
        return new NotificationShowRequester(mOption);
    }

    public NotificationAccessRequester notificationAccess() {
        return new NotificationAccessRequester(mOption);
    }

}
