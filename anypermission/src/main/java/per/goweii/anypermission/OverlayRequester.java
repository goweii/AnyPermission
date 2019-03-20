package per.goweii.anypermission;

import android.content.Context;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.option.Option;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/3/16
 */
public class OverlayRequester implements Requester<Void> {

    private final Option mOption;

    private RequestInterceptor<Void> mOnWithoutPermission = null;

    OverlayRequester(Option option) {
        this.mOption = option;
    }

    public OverlayRequester onWithoutPermission(RequestInterceptor<Void> onWithoutPermission) {
        mOnWithoutPermission = onWithoutPermission;
        return this;
    }

    @Override
    public Void start(final RequestListener listener) {
        mOption.overlay()
                .rationale(new Rationale<Void>() {
                    @Override
                    public void showRationale(Context c, Void d, final RequestExecutor e) {
                        if (mOnWithoutPermission == null) {
                            e.execute();
                        } else {
                            mOnWithoutPermission.intercept(d, new RequestInterceptor.Executor() {
                                @Override
                                public void execute() {
                                    e.execute();
                                }

                                @Override
                                public void cancel() {
                                    e.cancel();
                                }
                            });
                        }
                    }
                })
                .onGranted(new Action<Void>() {
                    @Override
                    public void onAction(Void data) {
                        listener.onSuccess();
                    }
                })
                .onDenied(new Action<Void>() {
                    @Override
                    public void onAction(Void data) {
                        listener.onFailed();
                    }
                })
                .start();
        return null;
    }
}
