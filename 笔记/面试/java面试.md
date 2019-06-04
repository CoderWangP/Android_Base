## 一、网络
  ### 1.TCP与UDP的区别:
    1、基于连接与无连接; 
    2、对系统资源的要求(T CP较多，UDP少); 
    3、UDP程序结构较简单; 
    4、流模式与数据报模式 ; 
    5、TCP保证数据正确性，UDP可能丢包; 6、TCP保证数据顺序，UDP不保证。
    
  ### 2.Http与Https的区别:
    1、https协议需要到ca申请证书，一般免费证书较少，因而需要一定费用。 
    2、http是超文本传输协议，信息是明文传输，https则是具有安全性的ssl加密传输协议。 
    3、http和https使用的是完全不同的连接方式，用的端口也不一样，前者是80，后者是 443。 
    4、http的连接很简单，是无状态的;HTTPS协议是由SSL+HTTP协议构建的可进行加 密传输、身份认证的网络协议，比http协议安全。  
  ### 3.Https的工作流程
  ### 4.Http请求方法
## 二 、java
  ### 1.线程与进程
  ### 2.死锁是什么？死锁产生的条件
      死锁是指两个或两个以上的进程在执行过程中，由于竞争资源或者由于彼此通信而造成的一种阻塞的现象，相互等待，
      若无外力作用，它们都将无法推进下去。
      在java中，举个例子，多线程下，线程1拥有锁a，去获取锁b，才能执行，线程2拥有锁b,去获取锁a，才能执行，
      这样线程1，线程2都持有对方需要的锁，才能执行，而线程1，线程2都不释放自己的锁，线程相互等待，无法执行下去
      解决：1.改变持有锁的顺序，都是先锁a，再锁b
           2.使用定时锁，trylock()超时
      
  ### 3.静态代码块，构造方法块，构造方法
    1.静态代码块：用static修饰，jvm加载类时执行，仅执行一次
    2.构造代码块：类中直接用{}包裹，每一次创建对象时调用
    3.构造方法：每一次创建对象时会调用
    执行顺序：静态代码块 > main()方法 > 构造代码块 > 构造方法
  ### 4. 反序列化对单例的影响
     在反序列化时，ObjectInputStream 因为利用反射机制调用了readObject,进而调用invokeReadResolve()，
     该方法利用反射机制创建了新对象，破坏了单例的唯一性。（序列化会通过反射调用无参数的构造方法创建一个新的对象。）
     解决方法：
     实现Serializable接口，重写readResolve方法，在readResolve方法里直接返回该单例对象
     public class Singleton implement Serializable {
        public static class Builder {
          public static Singleton instance = new Singleton();
        }

        public static Singleton getInstance(){
          return Builder.instance;
        }

        private Object readResolve(){
          retutn Builder.instance;
        }
      }
  ### 5. 深拷贝与浅拷贝
      1.在 Java 中，使用 『 = 』号做赋值操作的时候。对于基本数据类型，实际上是拷贝的它的值，但是对于对象而言，其实赋值的只是这个对象的引用，将原对象的引用传递过去，他们实际上还是指向的同一个对象。
     2.而浅拷贝和深拷贝就是在这个基础之上做的区分，如果在拷贝这个对象的时候，只对基本数据类型进行了拷贝，而对引用数据类型只是进行了引用的传递，而没有真实的创建一个新的对象，则认为是浅拷贝。反之，在对引用数据类型进行拷贝的时候，创建了一个新的对象，并且复制其内的成员变量，则认为是深拷贝。
     
     3.总结：Object 上的 clone() 方法，声明为protected
      浅拷贝：会创建一个新的对象，这个对象有着原始对象属性值的一份精确拷贝，
             如果原始对象的属性是基本数据类型，拷贝的就是基本数据类型的值，
             如果原始对象的属性是引用类型(内存地址)，那么拷贝的就是内存地址

      深拷贝：深拷贝会拷贝所有的属性,并拷贝属性指向的动态分配的内存。
            当对象和它所引用的对象一起拷贝时即发生深拷贝。
            深拷贝相比于浅拷贝速度较慢并且花销较大。
  ### 6.switch中能否使用string做参数
      jdk1.6，只能用int，
      jdk1.7之后，可以用String
  ### 7.线程
      1.sleep，wait区别
        sleep:thread里的方法，线程阻塞，不让出锁
        wait：object里的方法，线程不阻塞，让出锁，让其他线程对象执行，需要其他线程对象唤醒继续执行
      2.join,yield区别
        join:ThreadA.join，在线程B，调用该方法，当前线程B暂停执行，等待线程A执行完毕后，再执行线程B
        yield:让出cpu资源，线程由running状态变成runable状态，不阻塞，
              此时，cpu选择其他相同或者优先级更高的线程执行，如果没有，则该线程继续执行
 ### 8.线程池
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
     }
     1.参数解释
       corePoolSize：核心线程数量，默认情况下，即使核心线程没有任务执行，核心线程也是存活的。
       maximumPoolSize：线程池中最大线程数量，最大线程数量=核心线程数量+非核心线程数量，当任务数量超过最大线程数量时，
                       任务就可能会被阻塞，非核心线程只有在核心线程用完，且不超过最大线程数量时，才会被创建，非核心
                       线程在任务执行完，且非核心线程等待任务时间超过keepAliveTime时就会被销毁。
       keepAliveTime：超时时间，默认情况下，非核心线程等待任务时间超过这个时间，就会被销毁，当allowCoreThreadTimeOut设置为
                      true时，此属性也同样适用于核心线程。
       timeUnit:超时时间的的单位。
       workQueue：阻塞队列，我们提交的任务runnable会被存储在这个对象上
       threadFactory:线程工厂，创建新的线程任务
       RejectedExecutionHandler：拒绝策略
     2.任务分配策略（一个新任务提交到线程池）
       1.当核心线程有空闲时，启动一个核心线程去执行任务。
       2.当核心线程用完时，将任务放入BlockingQueue任务队列中，等待执行。
       3.当核心线程用完，且任务队列已满时，如果当前池内的线程数小于最大线程数，则创建一个新线程执行任务，
         如果当前池内的线程数已经大于最大线程数，那么线程池会拒绝执行该任务，具体的拒绝策略由RejectedExecutionHandler来执行。
     3.任务拒绝策略（RejectedExecutionHandler）
       ThreadPoolExecutor.AbortPolicy：丢弃任务，并抛出RejectedExecutionException异常。
       ThreadPoolExecutor.DiscardPolicy：丢弃任务，但是不抛出异常。
       ThreadPoolExecutor.DiscardOldestPolicy：丢弃任务队列最前面的任务，然后重新尝试执行最新的任务（重复此过程）
       ThreadPoolExecutor.CallerRunsPolicy：由调用该任务的线程去执行（添加该任务的线程）
     4.BlockingQueue（任务队列，阻塞队列）  
       ArrayBlockingQueue：基于数组的先进先出队列，有界，创建时，需指定大小。
       LinkedBlockingDeque：基于链表的先进先出队列，创建时，如果不指定大小，默认为Integer.MAX_VALUE。
       SynchronousQueue：同步队列，不会保存提交的任务，而是直接将提交的任务交给线程来执行。
     5.创建线程池
       java不提倡直接使用ThreadPoolExecutor，而是由Executors类去创建线程池，常用的如下：
       1.Executors.newFixedThreadPool(5)：固定容量大小的线程池
             public static ExecutorService newFixedThreadPool(int nThreads) {
                  return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
             }
          核心线程数量与最大线程数量是一样的，超时时间为0，因为全是核心线程，不会被回收，
          采用的任务队列是未指定大小的LinkedBlockingQueue（默认大小为Integer.MAX_VALUE）。
       2.Executors.newSingleThreadExecutor()：容量为1的线程池
             public static ExecutorService newSingleThreadExecutor() {
                  return new FinalizableDelegatedExecutorService
                      (new ThreadPoolExecutor(1, 1,
                                              0L, TimeUnit.MILLISECONDS,
                                              new LinkedBlockingQueue<Runnable>()));
             }
          核心线程数量与最大线程数量都为1的线程池，超时时间为0，全是核心线程，
          采用的任务队列是未指定大小的LinkedBlockingQueue（默认大小为Integer.MAX_VALUE）。
       3.Executors.newCachedThreadPool()：容量为Integer.MAX_VALUE的线程池
            public static ExecutorService newCachedThreadPool() {
                return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                              60L, TimeUnit.SECONDS,
                                              new SynchronousQueue<Runnable>());
            }
         核心线程数为0，最大线程数为Integer.MAX_VALUE，即没有核心线程，超时时间为60s，
         采用的任务队列是同步队列SynchronousQueue，收到提交任务，会直接查看有无空闲线程，
         有则交给其处理，没有则直接创建线程去处理。空闲线程在执行完任务等待接收任务的时间超过60s，就会被回收。
       4.Executors.newScheduledThreadPool(5);定时或者周期性的执行的线程池
            public ScheduledThreadPoolExecutor(int corePoolSize) {
                super(corePoolSize, Integer.MAX_VALUE,
                      DEFAULT_KEEPALIVE_MILLIS, MILLISECONDS,
                      new DelayedWorkQueue());
            }
            最大线程数量为Integer.MAX_VALUE,按超时时间升序排序的队列，定时或者周期执行
     6.线程池的其他方法
       1.shutDown()  关闭线程池，不影响已经提交的任务
       2.shutDownNow() 关闭线程池，并尝试去终止正在执行的线程
       3.allowCoreThreadTimeOut(boolean value) 允许核心线程闲置超时时被回收
       4.submit 一般情况下我们使用execute来提交任务，但是有时候可能也会用到submit，使用submit的好处是submit有返回值。
       5.beforeExecute() - 任务执行前执行的方法
       6.afterExecute() -任务执行结束后执行的方法
       7.terminated() -线程池关闭后执行的方法
 ### 9. try catch finally
      1. 只有当try代码块发生异常的时候，才会执行到catch代码块
      2. 不管try中是否发生异常，finally都会执行。
            以下两种情况例外：
               一：try中不发生异常时，try块中有System.exit(0);
               二：try中发生异常时，catch中有System.exit(0);
            说明：System.exit(0) 代码的作用是退出虚拟机;

      3. 若finally块内有return语句，则以finally块内的return为准
          说明：如果try 或者 catch内也有return 其实是先执行了try 或者 catch代码块中的return语句的，
               但是由于finally的机制，执行完try或者catch内的代码以后并不会立刻结束函数，还会执行finally块代码，
               若finally也有return语句，则会覆盖try块或者catch块中的return语句
               
      4.若finally代码块中有return语句，则屏蔽cath代码块中抛出的异常
          例如：
              public static int getResult1() {
                  int i = 0;
                  try {
                      int c = 2 / i;
                  } catch (Exception e) {
                      i = 10;
                      throw e;
                  } finally {
                      i = 20;
                      return i;
                  }
              }

          说明：调用该函数不会发生异常，因为catch代码块内的throw e 语句抛出的异常被 finally代码块的return语句屏蔽了      
### 10. java内存模型
      堆（heap）：线程共享，所有的对象实例以及数组都要在堆上分配。回收器主要管理的对象。
      方法区（MEATHOD AREA）：线程共享，存储类信息、常量、静态变量、即时编译器编译后的代码。
      方法栈（JVM Stack）：线程私有、存储局部变量表、操作栈、动态链接、方法出口，对象指针。
      本地方法栈（NATIVE METHOD STACK）：线程私有。为虚拟机使用到的Native 方法服务。如Java使用c或者c++编写的接口服务时，代码在此区运行。
      PC寄存器（PC Register）：线程私有。指向下一条要执行的指令。
      
      1.方法中的基本数据类型（int char...）存储在栈内存中，引用类型，引用放在栈内存中，对象放在堆内存中
      2.成员变量，不管是基本数据类型，还是包装类型（Integer,Double）都会放在堆内存中
