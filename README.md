# AnyPermission

[GitHub主页](https://github.com/goweii/AnyPermission)

[Demo下载](https://github.com/goweii/AnyPermission/raw/master/app/release/app-release.apk)





# 概述

是对[AndPermission](https://github.com/yanzhenjie/AndPermission)的封装，用于快速申请Android权限，链式调用，适配绝大多数国产系统。

- 运行时权限
- 未知应用安装权限
- 悬浮窗权限
- 显示通知权限
- 访问通知权限

在申请多个权限时，本框架采用排队方式申请，即先申请第一个权限，第一个申请成功后再进行下一个的申请流程，第一个失败则为本次申请失败。流程图如下。

![流程图](https://raw.githubusercontent.com/goweii/AnyPermission/master/picture/flowchart.png)



# 快速集成

## 一、添加仓库地址

在Project的**build.gradle**添加仓库地址

```java
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```

## 二、添加框架依赖

在Model:app的**build.gradle**添加框架依赖

**最新版本可查看[Releases](https://github.com/goweii/AnyPermission/releases)**

```java
dependencies {
	implementation 'com.github.goweii:AnyPermission:1.0.0'
}
```



# 使用说明

## 运行时权限

```java
private void requestRuntime() {
    mRuntimeRequester = AnyPermission.with(this).runtime(1)
            .permissions(Manifest.permission.CAMERA,
                    Manifest.permission.CALL_PHONE)
            .onBeforeRequest(new RequestInterceptor<String>() {
                @Override
                public void interceptor(@NonNull final String permission, @NonNull final Executor executor) {
                    // TODO 在每个权限申请之前调用，多次回调。可弹窗向用户说明下面将进行某个权限的申请。
                    // processor有两个方法，必须调用其一，否则申请流程终止。
                }
            })
            .onBeenDenied(new RequestInterceptor<String>() {
                @Override
                public void interceptor(@NonNull final String permission, @NonNull final Executor executor) {
                    // TODO 在每个权限被拒后调用，多次回调。可弹窗向用户说明为什么需要该权限，否则用户可能在下次申请勾选不再提示。
                    // processor有两个方法，必须调用其一，否则申请流程终止。
                }
            })
            .onGoSetting(new RequestInterceptor<String>() {
                @Override
                public void interceptor(@NonNull final String permission, @NonNull final Executor executor) {
                    // TODO 在每个权限永久被拒后调用（即用户勾选不再提示），多次回调。可弹窗引导用户前往设置打开权限，调用executor.execute()会自动跳转设置。
                    // processor有两个方法，必须调用其一，否则申请流程将终止。
                }
            })
            .request(new RequestListener() {
                @Override
                public void onSuccess() {
                    // TODO 授权成功
                }

                @Override
                public void onFailed() {
                    // TODO 授权失败
                }
            });
}

@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (mRuntimeRequester != null) {
        mRuntimeRequester.onActivityResult(requestCode);
    }
}
```

## 未知应用安装权限

```java
AnyPermission.with(this).install(apkFile)
        .onWithoutPermission(new RequestInterceptor<File>() {
            @Override
            public void interceptor(@NonNull final File data, @NonNull final Executor executor) {
                // TODO 在安装应用之前如果没有授予未知应用安装权限，则会回调该方法，可在此项用户弹窗提示。
                // processor有两个方法，必须调用其一，否则申请流程将终止。
            }
        })
        .request(new RequestListener() {
            @Override
            public void onSuccess() {
                // TODO 授权成功
            }

            @Override
            public void onFailed() {
                // TODO 授权失败
            }
        });
```

## 悬浮窗权限

```java
AnyPermission.with(this).overlay()
        .onWithoutPermission(new RequestInterceptor<Void>() {
            @Override
            public void interceptor(@NonNull final Void data, @NonNull final Executor executor) {
                // TODO 在申请悬浮窗权限之前调用，可在此项用户弹窗提示。
                // processor有两个方法，必须调用其一，否则申请流程将终止。
            }
        })
        .request(new RequestListener() {
            @Override
            public void onSuccess() {
                // TODO 授权成功
            }

            @Override
            public void onFailed() {
                // TODO 授权失败
            }
        });
```

## 显示通知权限

```java
AnyPermission.with(this).notificationShow()
        .onWithoutPermission(new RequestInterceptor<Void>() {
            @Override
            public void interceptor(@NonNull final Void data, @NonNull final Executor executor) {
                // TODO 在申请显示通知权限之前调用，可在此项用户弹窗提示。
                // processor有两个方法，必须调用其一，否则申请流程将终止。
            }
        })
        .request(new RequestListener() {
            @Override
            public void onSuccess() {
                // TODO 授权成功
            }

            @Override
            public void onFailed() {
                // TODO 授权失败
            }
        });
```

## 访问通知权限

```java
AnyPermission.with(this).notificationAccess()
        .onWithoutPermission(new RequestInterceptor<Void>() {
            @Override
            public void interceptor(@NonNull final Void data, @NonNull final Executor executor) {
                // TODO 在申请访问通知权限之前调用，可在此项用户弹窗提示。
                // processor有两个方法，必须调用其一，否则申请流程将终止。
            }
        })
        .request(new RequestListener() {
            @Override
            public void onSuccess() {
                // TODO 授权成功
            }

            @Override
            public void onFailed() {
                // TODO 授权失败
            }
        });
```



# API

## RequestInterceptor<T>

权限申请流程中回调，用于弹窗提醒用户。

```java
void interceptor(@NonNull final T data, @NonNull final Executor executor)
```

## Executor

控制流程的执行或取消。

```java
/**
 * 继续流程
 **/
void execute()
    
/**
 * 取消流程，即回调到申请失败
 **/
void cancel()
```

## AnyPermission

5种权限的申请都由此方法发起

```java
/**
 * 构建AnyPermission实例
 **/
public static AnyPermission with(@NonNull final Context context)
public static AnyPermission with(@NonNull final Activity activity)
public static AnyPermission with(@NonNull final Fragment fragment)
public static AnyPermission with(@NonNull final android.app.Fragment fragment)
    
/**
 * 获取某一权限的名称
 **/
public String name(String permission)
    
/**
 * 获取文件对应的Uri
 **/
public Uri fileUri(File file)
    
/**
 * 获取运行时权限申请的实现类实例
 **/
public RuntimeRequester runtime(int requestCodeWhenGoSetting)
    
/**
 * 获取未知应用安装权限申请的实现类实例
 **/
public InstallRequester install(File apkFile)
    
/**
 * 获取悬浮窗权限申请的实现类实例
 **/
public OverlayRequester overlay()
    
/**
 * 获取显示通知权限申请的实现类实例
 **/
public NotificationShowRequester notificationShow()
    
/**
 * 获取访问通知权限申请的实现类实例
 **/
public NotificationAccessRequester notificationAccess()
```

## RuntimeRequester

运行时权限申请的实现类

```java
/**
 * 所需申请的权限
 **/
public RuntimeRequester permissions(String... permissions)

/**
 * 每个权限申请之前的回调
 **/
public RuntimeRequester onBeforeRequest(RequestInterceptor<String> onBeforeRequest)

/**
 * 每个权限被拒后的回调
 **/
public RuntimeRequester onBeenDenied(RequestInterceptor<String> onBeenDenied)

/**
 * 每个权限被永久拒绝后的回调
 **/
public RuntimeRequester onGoSetting(RequestInterceptor<String> onGoSetting)

/**
 * 开始申请
 **/
public RuntimeRequester request(@NonNull RequestListener listener)

/**
 * 从设置页返回的时候，需要在再Activity的onActivityResult()中调用
 **/
public void onActivityResult(int requestCode)
```

## InstallRequester

未知应用安装权限申请的实现类

```java
/**
 * 未授予权限时，在跳转设置页面之前调用
 **/
public InstallRequester onWithoutPermission(RequestInterceptor<File> onWithoutPermission)
    
/**
 * 开始申请
 **/
public void request(final RequestListener listener)
```

## OverlayRequester

悬浮窗权限申请的实现类

```java
/**
 * 未授予权限时，在跳转设置页面之前调用
 **/
public OverlayRequester onWithoutPermission(RequestInterceptor<Void> onWithoutPermission)
    
/**
 * 开始申请
 **/
public void request(final RequestListener listener)
```

## NotificationShowRequester

显示通知权限申请的实现类

```java
/**
 * 未授予权限时，在跳转设置页面之前调用
 **/
public NotificationShowRequester onWithoutPermission(RequestInterceptor<Void> onWithoutPermission)
    
/**
 * 开始申请
 **/
public void request(final RequestListener listener)
```

## NotificationAccessRequester

访问通知安装权限申请的实现类

```java
/**
 * 未授予权限时，在跳转设置页面之前调用
 **/
public NotificationAccessRequester onWithoutPermission(RequestInterceptor<Void> onWithoutPermission)
    
/**
 * 开始申请
 **/
public void request(final RequestListener listener)
```



# 常见问题

## 暂无