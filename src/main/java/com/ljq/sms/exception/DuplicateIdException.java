package com.ljq.sms.exception;

/**
 * 学号重复异常
 */
public class DuplicateIdException extends RuntimeException {//继承这个类，才是一个异常
            //RuntimeException是非受检异常
            //只有 Throwable 及其子类的对象，才能被 throw 抛出、被 catch 捕获。
            //Object->Throwable->Exception(程序级异常)->RuntimeException(非受检异常)
    public DuplicateIdException(String message) {
        super(message);
        //super是父类，super()是调用父类的构造方法
        //super(message)作用：把消息字符串交给父类保存起来。
    }
}