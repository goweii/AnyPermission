package per.goweii.anypermission;

import android.support.annotation.NonNull;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/3/16
 */
public interface OnPermissionProcess<T> {
    void process(@NonNull final T data, @NonNull final Processor processor);

    interface Processor {
        void next();
        void cancel();
    }

}
