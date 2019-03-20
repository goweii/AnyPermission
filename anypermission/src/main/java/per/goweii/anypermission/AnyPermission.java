package per.goweii.anypermission;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.util.List;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/3/16
 */
public class AnyPermission {

    private final ContextHolder mContextHolder;

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
        mContextHolder = new ContextHolder(context);
    }

    private AnyPermission(final Activity activity){
        mContextHolder = new ContextHolder(activity);
    }

    private AnyPermission(final Fragment fragment){
        mContextHolder = new ContextHolder(fragment);
    }

    private AnyPermission(final android.app.Fragment fragment){
        mContextHolder = new ContextHolder(fragment);
    }

    public String name(String permission) {
        return Permission.transformText(mContextHolder.getContext(), permission).get(0);
    }

    public Uri fileUri(File file) {
        return AndPermission.getFileUri(mContextHolder.getContext(), file);
    }

    public RuntimeRequester runtime(int requestCodeWhenGoSetting) {
        return new RuntimeRequester(mContextHolder.getOption(), mContextHolder.getContext(), requestCodeWhenGoSetting);
    }

    public InstallRequester install(File apkFile) {
        return new InstallRequester(mContextHolder.getOption(), apkFile);
    }

    public OverlayRequester overlay() {
        return new OverlayRequester(mContextHolder.getOption());
    }

    @Deprecated
    public SettingRequester setting() {
        return new SettingRequester(mContextHolder.getOption());
    }

    public NotificationShowRequester notificationShow() {
        return new NotificationShowRequester(mContextHolder.getOption());
    }

    public NotificationAccessRequester notificationAccess() {
        return new NotificationAccessRequester(mContextHolder.getOption());
    }

}
