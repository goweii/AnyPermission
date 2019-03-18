package per.goweii.anypermission;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.option.Option;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/3/16
 */
public class RuntimeRequester implements Requester<RuntimeRequester> {

    private final Option mOption;
    private final Context mContext;
    private final int mRequestCode;

    private String[] mAllNeededPermissions = null;
    private Queue<String> mUnGrantedPermissions = null;

    private RequestListener mListener = null;

    private OnPermissionProcess<String> mOnBeforeRequest = null;
    private OnPermissionProcess<String> mOnBeenDenied = null;
    private OnPermissionProcess<String> mOnGoSetting = null;

    RuntimeRequester(Option option, Context context, int requestCodeWhenGoSetting) {
        this.mOption = option;
        this.mContext = context;
        this.mRequestCode = requestCodeWhenGoSetting;
    }

    public RuntimeRequester permissions(String... permissions) {
        this.mAllNeededPermissions = permissions;
        return this;
    }

    public RuntimeRequester onBeforeRequest(OnPermissionProcess<String> onBeforeRequest) {
        mOnBeforeRequest = onBeforeRequest;
        return this;
    }

    public RuntimeRequester onBeenDenied(OnPermissionProcess<String> onBeenDenied) {
        mOnBeenDenied = onBeenDenied;
        return this;
    }

    public RuntimeRequester onGoSetting(OnPermissionProcess<String> onGoSetting) {
        mOnGoSetting = onGoSetting;
        return this;
    }

    @Override
    public RuntimeRequester start(@NonNull RequestListener listener) {
        mListener = listener;
        findUnGrantedPermissions();
        if (mUnGrantedPermissions.peek() == null) {
            onSuccess();
            return this;
        }
        onBeforeRequest();
        return this;
    }

    public void onActivityResult(int requestCode) {
        if (requestCode == mRequestCode) {
            if (mUnGrantedPermissions.peek() == null) {
                findUnGrantedPermissions();
            }
            if (mUnGrantedPermissions.peek() == null) {
                onSuccess();
                return;
            }
            if (AndPermission.hasPermissions(mContext, mUnGrantedPermissions.peek())) {
                mUnGrantedPermissions.poll();
                findUnGrantedPermissions();
                if (mUnGrantedPermissions.peek() == null) {
                    onSuccess();
                    return;
                }
                onBeforeRequest();
            } else {
                if (AndPermission.hasAlwaysDeniedPermission(mContext, mUnGrantedPermissions.peek())) {
                    onGoSetting();
                } else {
                    onBeenDenied();
                }
            }
        }
    }

    private void findUnGrantedPermissions() {
        if (mUnGrantedPermissions == null) {
            mUnGrantedPermissions = new LinkedList<>();
        }
        for (String permission : mAllNeededPermissions) {
            if (!AndPermission.hasPermissions(mContext, permission)) {
                if (!mUnGrantedPermissions.contains(permission)) {
                    mUnGrantedPermissions.offer(permission);
                }
            } else {
                mUnGrantedPermissions.remove(permission);
            }
        }
    }

    private void onSuccess() {
        if (mListener != null) {
            mListener.onSuccess();
        }
    }

    private void onFailed() {
        if (mListener != null) {
            mListener.onFailed();
        }
    }

    private void onBeforeRequest() {
        if (mOnBeforeRequest == null) {
            request();
            return;
        }
        mOnBeforeRequest.process(mUnGrantedPermissions.peek(), new OnPermissionProcess.Processor() {
            @Override
            public void next() {
                request();
            }

            @Override
            public void cancel() {
                onFailed();
            }
        });
    }

    private void onBeenDenied() {
        if (mOnBeenDenied == null) {
            request();
            return;
        }
        mOnBeenDenied.process(mUnGrantedPermissions.peek(), new OnPermissionProcess.Processor() {
            @Override
            public void next() {
                request();
            }

            @Override
            public void cancel() {
                onFailed();
            }
        });
    }

    private void onGoSetting() {
        if (mOnGoSetting == null) {
            setting();
            return;
        }
        mOnGoSetting.process(mUnGrantedPermissions.peek(), new OnPermissionProcess.Processor() {
            @Override
            public void next() {
                setting();
            }

            @Override
            public void cancel() {
                onFailed();
            }
        });
    }

    private void setting() {
        mOption.runtime().setting().start(mRequestCode);
    }

    private void request() {
        mOption.runtime()
                .permission(mUnGrantedPermissions.peek())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        mUnGrantedPermissions.poll();
                        next();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(final List<String> data) {
                        next();
                    }
                })
                .start();
    }

    private void next() {
        if (mUnGrantedPermissions.peek() == null) {
            onSuccess();
            return;
        }
        if (AndPermission.hasAlwaysDeniedPermission(mContext, mUnGrantedPermissions.peek())) {
            onGoSetting();
        } else {
            onBeenDenied();
        }
    }
}