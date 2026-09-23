package com.ljq.sms.dao;

import com.ljq.sms.entity.Student;
import java.util.*;



public class StudentDaoImpl implements StudentDao {//implements相当于合同功能，将Student类中的6个接口全部完成

    private final Map<String, Student> studentMap = new LinkedHashMap<>();
    //<String,Student>是泛型，规定了key和value分别是什么类型
    //Map:键值对容器：通过key就能直接拿到value，这里的意思是通过学号能直接拿到Student的值
   //为什么不用Arraylist存储，因为学生多了循环次数多，用Map只要找一次
    @Override
    public void add(Student student) {
        studentMap.put(student.getId(), student);//添加学生
    }

    @Override
    public Student findById(String id) {
        return studentMap.get(id);//按学号查
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(studentMap.values());
        //studentMap.values() → 取出所有的学生对象（一个集合）
        //new ArrayList<>(...) → 把它包装成 ArrayList
    }

    @Override
    public void update(Student student) {
        studentMap.put(student.getId(), student);
        //跟add一样，Map的put规则是：key存在就覆盖，不存在就新增
        //添加和修改在Map层面是一类操作
    }

    @Override
    public void delete(String id) {
        studentMap.remove(id);
        //把学生删除
    }

    @Override
    public boolean existsById(String id) {
        return studentMap.containsKey(id);
        //Map 里有没有这个 key？有 → true，没有 → false。
    }
}
