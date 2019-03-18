package per.goweii.android.anypermission;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anypermission.AnyPermission;
import per.goweii.anypermission.OnPermissionProcess;
import per.goweii.anypermission.RequestListener;
import per.goweii.anypermission.RuntimeRequester;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private RuntimeRequester mRuntimeRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.btn_runtime).setOnClickListener(this);
        findViewById(R.id.btn_install).setOnClickListener(this);
        findViewById(R.id.btn_overlay).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);
        findViewById(R.id.btn_notification_show).setOnClickListener(this);
        findViewById(R.id.btn_notification_access).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_runtime:
                requestRuntime();
                break;
            case R.id.btn_install:
                requestInstall();
                break;
            case R.id.btn_overlay:
                requestOverlay();
                break;
            case R.id.btn_setting:
                requestSetting();
                break;
            case R.id.btn_notification_show:
                requestNotificationShow();
                break;
            case R.id.btn_notification_access:
                requestNotificationAccess();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mRuntimeRequester != null) {
            mRuntimeRequester.onActivityResult(requestCode);
        }
    }

    private void requestRuntime() {
        mRuntimeRequester = AnyPermission.with(this).runtime(1)
                .permissions(Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.SEND_SMS)
                .onBeforeRequest(new OnPermissionProcess<String>() {
                    @Override
                    public void process(@NonNull final String permission, @NonNull final Processor processor) {
                        AnyLayer.with(TestActivity.this)
                                .contentView(R.layout.dialog_runtime_before_request)
                                .backgroundColorRes(R.color.dialog_bg)
                                .cancelableOnTouchOutside(false)
                                .cancelableOnClickKeyBack(false)
                                .bindData(new AnyLayer.IDataBinder() {
                                    @Override
                                    public void bind(AnyLayer anyLayer) {
                                        TextView tvTitle = anyLayer.getView(R.id.tv_dialog_permission_title);
                                        TextView tvDescription = anyLayer.getView(R.id.tv_dialog_permission_description);
                                        TextView tvNext = anyLayer.getView(R.id.tv_dialog_permission_next);

                                        tvNext.setText("去授权");
                                        tvTitle.setText(AnyPermission.with(TestActivity.this).name(permission));
                                        tvDescription.setText("我们将开始请求\"" + AnyPermission.with(TestActivity.this).name(permission) + "\"权限");
                                    }
                                })
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.next();
                                    }
                                }, R.id.tv_dialog_permission_next)
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.cancel();
                                    }
                                }, R.id.tv_dialog_permission_close)
                                .show();
                    }
                })
                .onBeenDenied(new OnPermissionProcess<String>() {
                    @Override
                    public void process(@NonNull final String permission, @NonNull final Processor processor) {
                        AnyLayer.with(TestActivity.this)
                                .contentView(R.layout.dialog_runtime_before_request)
                                .backgroundColorRes(R.color.dialog_bg)
                                .cancelableOnTouchOutside(false)
                                .cancelableOnClickKeyBack(false)
                                .bindData(new AnyLayer.IDataBinder() {
                                    @Override
                                    public void bind(AnyLayer anyLayer) {
                                        TextView tvTitle = anyLayer.getView(R.id.tv_dialog_permission_title);
                                        TextView tvDescription = anyLayer.getView(R.id.tv_dialog_permission_description);
                                        TextView tvNext = anyLayer.getView(R.id.tv_dialog_permission_next);

                                        tvNext.setText("重新授权");
                                        tvTitle.setText(AnyPermission.with(TestActivity.this).name(permission));
                                        tvDescription.setText("啊哦，\"" + AnyPermission.with(TestActivity.this).name(permission) + "\"权限被拒了");
                                    }
                                })
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.next();
                                    }
                                }, R.id.tv_dialog_permission_next)
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.cancel();
                                    }
                                }, R.id.tv_dialog_permission_close)
                                .show();
                    }
                })
                .onGoSetting(new OnPermissionProcess<String>() {
                    @Override
                    public void process(@NonNull final String permission, @NonNull final Processor processor) {
                        AnyLayer.with(TestActivity.this)
                                .contentView(R.layout.dialog_runtime_before_request)
                                .backgroundColorRes(R.color.dialog_bg)
                                .cancelableOnTouchOutside(false)
                                .cancelableOnClickKeyBack(false)
                                .bindData(new AnyLayer.IDataBinder() {
                                    @Override
                                    public void bind(AnyLayer anyLayer) {
                                        TextView tvTitle = anyLayer.getView(R.id.tv_dialog_permission_title);
                                        TextView tvDescription = anyLayer.getView(R.id.tv_dialog_permission_description);
                                        TextView tvNext = anyLayer.getView(R.id.tv_dialog_permission_next);

                                        tvNext.setText("去设置");
                                        tvTitle.setText(AnyPermission.with(TestActivity.this).name(permission));
                                        tvDescription.setText("不能禁止\"" + AnyPermission.with(TestActivity.this).name(permission) + "\"权限");
                                    }
                                })
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.next();
                                    }
                                }, R.id.tv_dialog_permission_next)
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.cancel();
                                    }
                                }, R.id.tv_dialog_permission_close)
                                .show();
                    }
                })
                .start(new RequestListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(TestActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(TestActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestInstall() {
        AnyPermission.with(this).install(new File(TestActivity.this.getCacheDir(), "1.apk"))
                .onWithoutPermission(new OnPermissionProcess<File>() {
                    @Override
                    public void process(@NonNull final File data, @NonNull final Processor processor) {
                        AnyLayer.with(TestActivity.this)
                                .contentView(R.layout.dialog_runtime_before_request)
                                .backgroundColorRes(R.color.dialog_bg)
                                .cancelableOnTouchOutside(false)
                                .cancelableOnClickKeyBack(false)
                                .bindData(new AnyLayer.IDataBinder() {
                                    @Override
                                    public void bind(AnyLayer anyLayer) {
                                        TextView tvTitle = anyLayer.getView(R.id.tv_dialog_permission_title);
                                        TextView tvDescription = anyLayer.getView(R.id.tv_dialog_permission_description);
                                        TextView tvNext = anyLayer.getView(R.id.tv_dialog_permission_next);

                                        tvNext.setText("去打开");
                                        tvTitle.setText("安装应用");
                                        tvDescription.setText("我们将开始请求安装应用权限");
                                    }
                                })
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.next();
                                    }
                                }, R.id.tv_dialog_permission_next)
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.cancel();
                                    }
                                }, R.id.tv_dialog_permission_close)
                                .show();
                    }
                })
                .start(new RequestListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(TestActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(TestActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestOverlay() {
        AnyPermission.with(this).overlay()
                .onWithoutPermission(new OnPermissionProcess<Void>() {
                    @Override
                    public void process(@NonNull final Void data, @NonNull final Processor processor) {
                        AnyLayer.with(TestActivity.this)
                                .contentView(R.layout.dialog_runtime_before_request)
                                .backgroundColorRes(R.color.dialog_bg)
                                .cancelableOnTouchOutside(false)
                                .cancelableOnClickKeyBack(false)
                                .bindData(new AnyLayer.IDataBinder() {
                                    @Override
                                    public void bind(AnyLayer anyLayer) {
                                        TextView tvTitle = anyLayer.getView(R.id.tv_dialog_permission_title);
                                        TextView tvDescription = anyLayer.getView(R.id.tv_dialog_permission_description);
                                        TextView tvNext = anyLayer.getView(R.id.tv_dialog_permission_next);

                                        tvNext.setText("去打开");
                                        tvTitle.setText("悬浮窗");
                                        tvDescription.setText("我们将开始请求悬浮窗权限");
                                    }
                                })
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.next();
                                    }
                                }, R.id.tv_dialog_permission_next)
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.cancel();
                                    }
                                }, R.id.tv_dialog_permission_close)
                                .show();
                    }
                })
                .start(new RequestListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(TestActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(TestActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestSetting() {
        AnyPermission.with(this).setting()
                .onWithoutPermission(new OnPermissionProcess<Void>() {
                    @Override
                    public void process(@NonNull final Void data, @NonNull final Processor processor) {
                        AnyLayer.with(TestActivity.this)
                                .contentView(R.layout.dialog_runtime_before_request)
                                .backgroundColorRes(R.color.dialog_bg)
                                .cancelableOnTouchOutside(false)
                                .cancelableOnClickKeyBack(false)
                                .bindData(new AnyLayer.IDataBinder() {
                                    @Override
                                    public void bind(AnyLayer anyLayer) {
                                        TextView tvTitle = anyLayer.getView(R.id.tv_dialog_permission_title);
                                        TextView tvDescription = anyLayer.getView(R.id.tv_dialog_permission_description);
                                        TextView tvNext = anyLayer.getView(R.id.tv_dialog_permission_next);

                                        tvNext.setText("去打开");
                                        tvTitle.setText("修改设置");
                                        tvDescription.setText("我们将开始请求修改设置权限");
                                    }
                                })
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.next();
                                    }
                                }, R.id.tv_dialog_permission_next)
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.cancel();
                                    }
                                }, R.id.tv_dialog_permission_close)
                                .show();
                    }
                })
                .start(new RequestListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(TestActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(TestActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestNotificationShow() {
        AnyPermission.with(this).notificationShow()
                .onWithoutPermission(new OnPermissionProcess<Void>() {
                    @Override
                    public void process(@NonNull final Void data, @NonNull final Processor processor) {
                        AnyLayer.with(TestActivity.this)
                                .contentView(R.layout.dialog_runtime_before_request)
                                .backgroundColorRes(R.color.dialog_bg)
                                .cancelableOnTouchOutside(false)
                                .cancelableOnClickKeyBack(false)
                                .bindData(new AnyLayer.IDataBinder() {
                                    @Override
                                    public void bind(AnyLayer anyLayer) {
                                        TextView tvTitle = anyLayer.getView(R.id.tv_dialog_permission_title);
                                        TextView tvDescription = anyLayer.getView(R.id.tv_dialog_permission_description);
                                        TextView tvNext = anyLayer.getView(R.id.tv_dialog_permission_next);

                                        tvNext.setText("去打开");
                                        tvTitle.setText("显示通知");
                                        tvDescription.setText("我们将开始请求显示通知权限");
                                    }
                                })
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.next();
                                    }
                                }, R.id.tv_dialog_permission_next)
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.cancel();
                                    }
                                }, R.id.tv_dialog_permission_close)
                                .show();
                    }
                })
                .start(new RequestListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(TestActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(TestActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestNotificationAccess() {
        AnyPermission.with(this).notificationAccess()
                .onWithoutPermission(new OnPermissionProcess<Void>() {
                    @Override
                    public void process(@NonNull final Void data, @NonNull final Processor processor) {
                        AnyLayer.with(TestActivity.this)
                                .contentView(R.layout.dialog_runtime_before_request)
                                .backgroundColorRes(R.color.dialog_bg)
                                .cancelableOnTouchOutside(false)
                                .cancelableOnClickKeyBack(false)
                                .bindData(new AnyLayer.IDataBinder() {
                                    @Override
                                    public void bind(AnyLayer anyLayer) {
                                        TextView tvTitle = anyLayer.getView(R.id.tv_dialog_permission_title);
                                        TextView tvDescription = anyLayer.getView(R.id.tv_dialog_permission_description);
                                        TextView tvNext = anyLayer.getView(R.id.tv_dialog_permission_next);

                                        tvNext.setText("去打开");
                                        tvTitle.setText("访问通知");
                                        tvDescription.setText("我们将开始请求访问通知权限");
                                    }
                                })
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.next();
                                    }
                                }, R.id.tv_dialog_permission_next)
                                .onClickToDismiss(new AnyLayer.OnLayerClickListener() {
                                    @Override
                                    public void onClick(AnyLayer anyLayer, View v) {
                                        processor.cancel();
                                    }
                                }, R.id.tv_dialog_permission_close)
                                .show();
                    }
                })
                .start(new RequestListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(TestActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(TestActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
