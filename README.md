# Android_Base
android整个项目的基础仓库

## 1. 四大组件(生命周期，特性)
        注意：四大组件都是运行在主线程
        1. Activity:
           1. 生命周期
              onCreate->onStart->onResume->onPause->onStop->onDestory
              …->onStop->onRestart->onStart->…
        2. Service:
          前台服务，后台服务，一般使用的时后台服务，在系统内存不足的情况下，可能被杀死，需要使用前台服务时，使用startForeground(int id, Notification notification)
          id,通知的id
           在后台运行，没有ui界面，不需要与用户交互，且需要长期运行，不要在Service中做
           耗时操作，因为他也运行在主线程
           1.生命周期，2，区别，3.运行在主线程，4.在service里做耗时操作，需要创建子线程，
           1.startService：
            使用该方式启动服务之后，服务于调用者脱离关系，调用者退出，不影响服务运行
             onCreate->onStartCommond->onDestory
           2.bindService：
             使用该方式启动服务，service的生命周期与调用者绑定，调用者一旦退出，服务也
             退出
             onCreate->onBind->onUnbind->onDestory
        3. Broadcast Receiver:
           前台广播，后台广播
           1.动态注册：用于注册的组件一旦销毁，广播也就失效了
           2.静态注册:AndroidManifest文件注册，一直存在，直到进程消失
        4. Content Provider:
           应用程序间共享数据，如通讯录，图片媒体文件等都提供了相应的Content Provider
           供其他程序调用
