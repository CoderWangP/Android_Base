package com.wp.android_base.test.java.clone;

/**
 * Created by wp on 2019/5/31.
 * <p>
 * Description:
 */

public class Person implements Cloneable{
    public String name;
    public Course course;

    public Person(String name,String courseName) {
        this.name = name;
        this.course = new Course(courseName);
    }

    /**
     * 重写object类的clone方法，该对象并实现Cloneable接口
     * @return
     */
    @Override
    protected Object clone(){
/*        try {
            //浅拷贝，精确的拷贝对象的每个属性，如果属性是基本数据类型，就直接拷贝值，如果是引用类型，就拷贝地址
            return super.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return null;*/
        //深拷贝，拷贝所有属性，并拷贝引用类型的引用指向新的动态分配的内存，当对象，及它引用的对象一起拷贝时，发生深拷贝，
        //深拷贝相比以浅拷贝，速度较慢并且花销更大
        return new Person(name,course.name);
    }
}
