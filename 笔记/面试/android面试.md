
## 1. 四大组件(生命周期，特性)
        注意：四大组件都是运行在主线程
        1. Activity:
           1. 生命周期
              onCreate->onStart->onResume->onPause->onStop->onDestory
              …->onStop->onRestart->onStart->…
        2. Service:
          前台服务，后台服务，一般使用的时后台服务，在系统内存不足的情况下，可能被杀死，需要使用前台服务时，
	  使用startForeground(int id, Notification notification)
          id,通知的id
           在后台运行，没有ui界面，不需要与用户交互，且需要长期运行，不要在Service中做
           耗时操作，因为他也运行在主线程
           1.生命周期:
	   2.区别:
	   3.运行在主线程：
	   4.在service里做耗时操作，需要创建子线程：
	   5.bindService与启动组件通信：
           1.startService：
            使用该方式启动服务之后，服务于调用者脱离关系，调用者退出，不影响服务运行
             onCreate->onStartCommond->onDestory
           2.bindService：
             使用该方式启动服务，service的生命周期与调用者绑定，调用者一旦退出，服务也
             退出
             onCreate->onBind->onUnbind->onDestory
        3. Broadcast Receiver:
           前台广播，后台广播，两种注册方式的区别
           1.动态注册：用于注册的组件一旦销毁，广播也就失效了
           2.静态注册:AndroidManifest文件注册，一直存在，直到进程消失
        4. Content Provider:
           应用程序间共享数据，如通讯录，图片媒体文件等都提供了相应的Content Provider
           供其他程序调用。
## 2. 存储
	1. SharePreferences(apply,commit):
	   1.apply,commit区别
	   2.apply之后，调用get能否获取值
	2. Sqlite(创建): 
	   创建过程:     
	   1.继承自SQLiteOpenHelper      
	   2.实现onCreate,onUpgrade方法，onCreate用于创建一些表，做一些初始化，onUpgrade方法用于数据库的更新，
	     另外实现构造方法，需要传入3个参数，database_name,database_version,CursorFactory，CursorFactory一般不传，使用默认Factory
	3.在生命周期的什么方法里做一个本地持久化的操作
	   onPause:Activity销毁一定会执行onPause方法，而在一些内存不足的情况下，Activity销毁，不一定执行onStop,onDestory方法
        

## 3. Looper,Thread,Handler,MessageQueue联系
     1.一个线程有几个Looper，通过什么保存
        一个线程有且只有一个，通过ThreadLocal保存在线程局部变量中
     2. Looper.prepare:通过ThreadLocal获取当前Looper，如果有，抛出异常，
        没有,则创建Looper，保存在当前线程的ThreadLocal里,并创建MessageQueue
        Looper.loop:无限for(;;)循环，从MessageQueue中取Message,并调用
        msg.target.dispatchMessage,而这个target就是Handler
     3.Handler:创建Handler,重写handleMessage方法，调用Handler.myLooper，从
       Threadlocal里获取当前线程的Looper，进而获取Looper的MessageQueue,post message时，
       将message放入MessageQueue中，并将Message.target赋值为当前handler，这样
       Looper中msg.target.dispatchMessage既是调用的当前Handler的方法，而该方法调用了重写的handleMessage方法
       
    延伸：View.post/View.postpostDelayed原理：
    1.View.post(Runnable runnable)，在dispatchAttachedToWindow执行之前，会将这个runnable封装成message，
    缓存下来,等dispatcchAttachedToWindow执行之时，会通过main线程的Handler,post到主线程的的Handler去处理，
    而在dispatchAttachedToWindow执行之后，在调用View.post(),会被直接post到主线程的Handler去处理。
    1.在onCreate里用View.post(Runnable runnable)，在Runnable的run方法里可以获取到控件宽高，原因是，View.post，会将这个消息
      缓存下来，等到dispatchAttachedToWindow调用时，会调用main线程的handler来执行这些缓存的消息，而这时，
      控件已经绘制完成，所以可以正确获取宽高。
    

## 4.ANR(Application Not Responding)
    anr发生的情况
    1.程序在5秒内未响应用户的输入事件(屏幕触摸事件，键盘输入事件)
    2.Broadcast Recceiver 的onReceive事件，前台广播在10s内未执行完毕，后台广播在60s
      内未执行完毕，就会触发anr,一般我们发的时后台广播，发送前台广播，需要在Intent中，
      添加一个Flag,Intent.FLAG_RECEIVER_FOREGROUND
    3.Service:运行在主线程，前台服务20s,后台服务200s没有执行完毕，就会ANR
    4.ContentProvider:ContentProvider的publish在10s内没进行完。

## 5. Activity,Fragment生命周期
     Activity:onCreate->onStart->onResume->onPause->onStop->onDestory
              …->onStop->onRestart->onStart->…
    Fragment:onAttach->onCreate->onCreateView->onActivityCreated->onStart->onResume->onPause->onStop->onDestoryView->onDestory->onDetach
    …->onDestoryView ->onCreateView->…
    1.A启动B:
    	A:onPause->B:onCreate->B:onStart->B:onResume->A:onSaveInstanceState->A:onStop
    2.Activity在屏幕旋转时的生命周期:
	    onCreate -> onStart -> onResume -> onPause -> onSaveInstanceState -> onStop >onDestory  
	    -> onCreate -> onStart - > onRestoreInstanceState -> onResume
	    1、会走整个生命周期方法，而且会调用onSaveInstance和onRestoreInstancceState方法
	    2、sdk版本 > 13,后，要想activity横竖屏切换，不重新创建，需要在Manifest的注册的Activity里添加
	       android:configChanges="keyboardHidden|orientation|screenSize"
	       而sdk < 13,android:configChanges="keyboardHidden|orientation"
	    3、配置了configChanges，生命周期(从Activity创建开始)：
	      onCreate -> onStart -> onResume -> onConfigurationChanged
    3.onRestart调用时机
       1.从其他页面返回onRestart->onStart->onResume
       2.按下Home键
       3.跳转到其他应用，再从其他应用返回
    4.onSaveInstanceState调用时机
		1.非用户主动明确结束(主动结束：按back键，定义click方法，finish)都会调用onSaveInsatanceState
			1.按home键
			2.屏幕旋转
			3.内存不足
			4.启动另一个Activity
		2.这个方法的调用时机是在onStop之前，但是与onPause没有既定的时序关系	
## 6.Activity启动模式：
	1. standard:每一次启动，都会生成一个新的实例，放入栈顶中
	2. singleTop:通过singelTop启动Activity时，如果发现有需要启动的实例正在栈顶，责直接重用，否则生成新的实例
	3. singleTask:通过singleTask启动Activity时，如果发现有需要启动的实例正在栈中，责直接移除它上边的实例并 重用该实例，否则生成新的实例
	4. singleInstance:通过singleTask启动Activity时，会启用一个新的栈结构，并将新生成的实例放入栈中。

## 7.IntentService，HandlerThread
    1. IntentService 是继承自 Service,内部通过HandlerThread启动一个新线程处理耗时操作么，
    可以看做是Service和HandlerThread的结合体，在完成了使命之后会自动停止，适合需要在工作线程处理UI无关任务的场景
    2. 如果启动 IntentService 多次，那么每一个耗时操作会以工作队列的方式在 IntentService 的 onHandleIntent 回调方法中执行，
    依次去执行，使用串行的方式，执行完自动结束。
    
    源码角度：
    HandlerThread，是一个thread，内部run方法，初始化Looper，
    调用Looper.prepare:创建Looper，MessageQueue,将Looper保存在当前线程的ThreadLocal中， Looper.loop方法，启动消息循环。
    IntentService：onCreate时，创建了一个HandlerThread，并用这个HandlerThread的Looper创建了一个ServiceHandler,
    让这个Handler负责这个Thread的消息循环，onStart方法，启动service，将启动的Intent作为Message的obj，
    传给当前ServiceHandler处理，进而调onHandleIntent处理消息

## 8. LocalBroadcast本地广播:
	1. LocalBroadcast是APP内部维护的一套广播机制，有很高的安全性和高效性。所以如果有APP内部发送、接收广播的需要应该使用LocalBroadcast。
	2. Receiver只允许动态注册，不允许在Manifest中注册。
	3. LocalBroadcastManager所发送的广播action，只能与注册到LocalBroadcastManager中BroadcastReceiver产生互动。

	代码使用：
	1. LocalBroadcastManager.getInstance(context)获取实例
	2. LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter)，注册监听。
	3. LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
	4. LocalBroadcastManager.getInstance(context).sendBroadcast(intent);  
## 9. asset目录与res目录区别
	1. res目录下的文件会在R文件中生成对应的id，asset不会(Context.getAssets().open("fileName")，AsserManager.open("fileName"))
	2. res目录下的文件除了res/raw外，其余都会被编译成二进制文件，而asset不会被编译
## 10. Android WebView与js交互
      1.Android调用js:
	   1.Webview.loadUrl
	   2.Webview.evaluateJavascript（"javascript", ValueCallback)执行js代码
      2.js调用Android:
           1.2.WebviewSettings需要打开应用javascript的开关，WebViewSettings.setJavaScriptEnabled(true)
	   2.WebView.addJavascriptInterface(new JavascriptInterface(), "obj"),向js注入java对象，
	     对象为JavascriptInterface,名称为obj，js那边就可以直接用名称obj.方法，方法为JavascriptInterface里的方法
      注意：
          JavascriptInterface里的方法需要加注解@JavascriptInterface
## 11. Android动画
	1.Tween动画（补间动画）：对View做一些 Alpha Scale Translate Rotate等图形变换来完成动画
	2.Frame动画（帧动画）：AnimationDrawable控制，播放一些图片等操作
	3.属性动画：PropertyAnimation 属性动画。
## 12. 自定义View
	1.invalidate,postInvalidate区别，invalidate不能再异步线程里直接调用，postInvalidate可以，
	2.requestLayout会调用onMeasure,onLayout,不一定调用onDraw。
	3.保存自定义View的状态数据（原生也行，可以用一个SwitchView，横竖屏切换时，自动保存状态）
	 	1.View必须有一个唯一的id
		2.setSaveEnabled(true)
## 13. 事件分发
## 14. 图片加载框架，Glide缓存原理，Lru cache(Least Recently Used :近期最少使用的)算法
	LruCache:近期最少使用
	构造方法：public LruCache(int maxSize) {
			if (maxSize <= 0) {
			    throw new IllegalArgumentException("maxSize <= 0");
			}
			this.maxSize = maxSize;
			this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
	        }
	原理：内部维持了一个LinkedHashMap,而且创建(new LinkedHashMap<K, V>(0, 0.75f, true)，accessOrder为true)
	的是一个按照访问顺序存储 的LinkedHashMap，这样每次访问的时候，比如get方法，就会把get到的元素从原来的链表中删除，
	然后重新插入到链表的尾部(尾部表示最新加入的)，put元素的时候，会调用trimToSize,如果发现加入当前元素之后，超过了设置的最大size，
	就会从链表的头部开始删除，直到小于最大size,这样就保持了LinkedHashMap的大小保持在最大size以下，达到缓存的效果，防止oom。
## 15. EventBus
       1.处理消息的4种线程模式：posting,main,background,asyc
        	posting：默认的线程模式，与发消息是在同一个线程中
	 	main：主线程
	 	background:发送线程在子线程，就在该线程处理，发送线程在主线程，开启子线程执行
	 	asyc：不管发送线程在主线程，还是子线程，都会创建新的子线程来处理
       2.原理：
## 16. orm Sqlite创建，第三方框架应用，GreenDao,Realm应用，原理
   	1.原生Sqlite创建
	2.GreenDao更新操作
## 17. 线程及线程池应用，volatile关键字
	1.线程的两种创建方式，区别
	2.线程池
## 18. 图片压缩二次采样，加载合适的图片
     /**
      * 从资源文件中decode位图，按照需要的宽高
      */
     public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
        int reqWidth, int reqHeight) {

	    // 首先只解析图片资源的边界
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    //计算缩放值（图片的像素压缩）
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // 用计算出来的缩放值解析图片
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 计算缩放的比例
     */	
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            /**
             * 计算最大的inSampleSize值，压缩的比例，这个值必须是2的幂的值，如果不是，解码器最终会向下
             * 舍入到最接近2的幂的值
             *
             * 计算时，保证缩放后的height，width必须大于需要的宽，高
             */
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
## 19.  Retrofit+RxJava处理生命周期
       1.rxlifecycle
         1.原理2.Rx的关键方法takeUntil
## 20. 引用类型（reference）
       在Java中，一切都被视为对象，引用则是用来操纵对象的途径。
       1.Strong reference（强引用）：
       2.SoftReference（软引用）:
       3.WeakReference （弱引用）:
       4.PhantomReference（虚引用）:
       一个只被虚引用持有的对象可能会在任何时候被GC回收。虚引用对对象的生存周期完全没有影响，
       也无法通过虚引用来获取对象实例，仅仅能在对象被回收时，
       得到一个系统通知（只能通过是否被加入到ReferenceQueue来判断是否被GC，这也是唯一判断对象是否被GC的途径）
       
       虚引用必须和引用队列(ReferenceQueue)联合使用。当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，
       就会在回收对象的内存之前，把这个虚引用加入到与之关联的引用队列中。
            String str = new String("abc");
	    ReferenceQueue queue = new ReferenceQueue();
	    // 创建虚引用，要求必须与一个引用队列关联
	    PhantomReference pr = new PhantomReference(str, queue);
## 21. 注解vs枚举
## 22. 常用布局
	LinearLayout,RelativeLayout,FrameLayout,AbsoluteLayout,ConstraintLayout
## 23. 方法数超过64k,65535怎么处理
   ### 1. 如果你的minSdkVersion的设置>=21(android 5.0)，只需要在build.gradle中设置multiDexEnabled为true
   	android {
	    defaultConfig {
		...
		minSdkVersion 21 
		targetSdkVersion 26
                //配置multiDexEnabled
		multiDexEnabled true
	    }
	    ...
	}
   ### 2. 如果你的minSdkVersion的设置<21:
       1. 在AndroidMainfest.xml中添加自定义的application。
          	<?xml version="1.0" encoding="utf-8"?>
		<manifest xmlns:android="http://schemas.android.com/apk/res/android"
		   package="com.example.myapp">
		   <application
			   android:name="MyApplication" >
		       ...
		   </application>
		</manifest>

        2.在自定义Application的attachBaseContext()方法中调用Multidex.install(this)。
		public class MyApplication extends SomeOtherApplication {
			  @Override
			  protected void attachBaseContext(Context base) {
			     super.attachBaseContext(context);
			     Multidex.install(this);
			  }
		}
        3.同样要在build.gradle中添加multiDexEnabled，而且还要添加com.android.support:multidex依赖包
		android {
		    defaultConfig {
			...
			minSdkVersion 15 
			targetSdkVersion 26
			multiDexEnabled true
		    }
		    dexOptions {
			incremental true
			javaMaxHeapSize "4g"
		    }
		    ...
		}

		dependencies {
		  compile 'com.android.support:multidex:1.0.1'
		}

## 24 .onSaveInstanceState 什么时候调用
	一,非用户主动明确结束（按back键，自定义click方法调用finish）时都会调用onSaveInstanceState：
		1.屏幕旋转
		2.按HOME键
		3.内存不足，被回收
		4.从一个activity启动另一个activity
	二,这个方法的调用时机是在onStop前，但是它和onPause没有既定的时序关系
## 25 .View的生命周期
	1.构造方法(两个参数)-> onFinishInflate->onVisibilityChanged->onAttachedToWindow
	->onMeasure->onSizeChanged->onLayout->onDraw->onWindowFocusChanged		

   
