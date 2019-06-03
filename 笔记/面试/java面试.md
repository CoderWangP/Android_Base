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
      
