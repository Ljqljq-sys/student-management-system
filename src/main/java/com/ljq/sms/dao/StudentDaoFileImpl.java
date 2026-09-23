package com.ljq.sms.dao;

import com.ljq.sms.entity.Student;
import java.util.*;
import java.io.*;//* 表示"这个包下所有的类都导进来"，省得一个个写。
/*io等于把内存中的东西写入硬盘，代表input和output
往文件里写东西 = 数据流出去 = Output（输出）
从文件里读东西 = 数据流进来 = Input（输入）

io包装着所有和读写数据有关的类
File	            代表一个文件或目录（注意：它不代表文件内容，只代表"路径"）
FileInputStream	    字节输入流 —— 从文件读原始字节
FileOutputStream	字节输出流 —— 往文件写原始字节
InputStreamReader	字节流 → 字符流 的转换器
OutputStreamWriter	字符流 → 字节流 的转换器
BufferedReader	    带缓冲的字符输入流 —— 一行行读文本
BufferedWriter	    带缓冲的字符输出流 —— 一行行写文本
IOException	         IO 异常 —— 读写出错时抛出的异常
 */
import java.nio.charset.StandardCharsets;
/*
java.nio.charset.StandardCharsets —— 字符编码常量
读写文本必须指定编码，否则中文会乱码，这个包的作用就是转成utf-8编码
 */
public class StudentDaoFileImpl implements StudentDao {
    private final Map<String, Student> studentMap = new LinkedHashMap<>();
    //创建一个私有的、不能被重新赋值的映射表，名字叫 studentMap， 键是学号字符串，值是学生对象， 用保持插入顺序的 LinkedHashMap 来实现。
    private final String filePath = "data/students.txt";

    // 构造方法：创建对象时就从文件加载数据
    public StudentDaoFileImpl() {
        loadFromFile();
    }
    // ========== 下面是内部工具方法 ==========

    // 从文件读取数据到 studentMap
    private void loadFromFile() {
        //loadFromFile()	文件 → 内存	Input
        File file = new File(filePath);//创建一个文件对象（不代表文件真的存在）
       // File 对象就像"一张写着地址的纸条"，纸条本身不是房子。
        //这行代码做的事只是：记下"我要操作 data/students.txt 这个路径"。
        if (!file.exists()) {
            return;// 文件不存在（第一次运行），直接返回
            //防御性编程" —— 不要假设文件一定存在
        }

        try (BufferedReader reader = new BufferedReader(
                // try(...) 是"try-with-resources"：括号里声明的对象，
                // 出了 try 块会自动调用 close() 关闭
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
        /*
             new FileInputStream(file)：打开 data/students.txt 这个文件，准备好从里面读字节

             new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8：
             把字节流包起来，告诉它：这些字节是按 UTF-8 编码的
             这样它就能把字节翻译成字符
             如果这里不写 UTF_8，中文就会乱码，系统默认GBK编码

             new BufferedReader(...)
             把字符流再包一层，提供两个好处：
             1. 带缓冲区 —— 一次从硬盘读一大块，不是读一个字符就访问一次硬盘（快很多）
             2. 提供 readLine() 方法 —— 一次读一整行（超方便）
        */
            String line;
            while ((line = reader.readLine()) != null) {
                //读文件的一整行，返回一个 String。读到文件末尾时返回 null。
                /*(line = reader.readLine())
                这是一个"赋值表达式"：把读到的内容赋给 line，同时表达式的值就是 line 的值。
                 */
                // 读到文件末尾时 readLine() 返回 null
                // 所以 "!= null" 的意思是"还没读完，继续循环"
                // ⚠️ 外层括号 (line = ...) 不能少！因为 != 优先级比 = 高


                // 跳过空行
                if (line.trim().isEmpty()) {
                    continue;
                }/*
                    line.trim()	去掉字符串首尾的空白（空格、制表符、换行）
                    .isEmpty()	判断是不是空字符串
                     continue	跳过本次循环，进入下一轮
                */



                // 按逗号拆分成 5 个字段：id,name,age,major,score
                String[] parts = line.split(",");//split = 按指定分隔符，把字符串切成数组。

                if (parts.length != 5) {//检查字段数量
                    continue;   // 格式不对就跳过，别让程序崩
                }

                String id = parts[0].trim();//trim的原因是文件里的数据可能带空格，存进去会对不上
                String name = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());//Integer.parseInt：把字符串转换为数字
                String major = parts[3].trim();
                double score = Double.parseDouble(parts[4].trim());//Double.parseDouble：把字符串转换成数字

                Student student = new Student(id, name, age, major, score);
                studentMap.put(id, student);
            }

        } catch (IOException e) {
            //IOException 是"输入输出异常"。
            //catch = "捕获异常"，也就是"接住"程序抛出来的错误，然后决定怎么处理。
            //catch通常和try成对出现，try是可能出错的代码，catch是出错了怎么办
            System.out.println("读取文件失败：" + e.getMessage());
        }
    }

    // 把 studentMap 写回文件
    private void saveToFile() {
        //saveToFile()	内存 → 文件	Output
        // 1. 确保 data 目录存在
        File dir = new File("data");
        //这里 File 代表的不是文件，是目录
        //File 类既可以代表文件，也可以代表目录。
        if (!dir.exists()) {
            dir.mkdirs();
            //mkdirs() = make directories，创建目录。在这里mkdirs有复数也就是s,这是创建多级目录
            //mkdir()	创建一级目录。如果父目录不存在，失败
            //mkdirs()	创建多级目录。父目录不存在会一起创建
            //mkdirs() 会返回 boolean（成功 true / 失败 false）
        }

        // 2. 把内存里的所有学生写进文件（覆盖写入）
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            //结构和读文件时一模一样，只是"流"换成了输出方向。
            /*
            读（Input）	        写（Output）	            作用
            FileInputStream	    FileOutputStream	    字节进出文件
            InputStreamReader	OutputStreamWriter	    字节 ↔ 字符转换
            BufferedReader	    BufferedWriter	        缓冲 + 按行操作
            记住这个对称关系，以后写 IO 就是套模板。
            new FileOutputStream(filePath) 默认是覆盖模式 —— 文件原有内容会被清空
            如果要追加，用 new FileOutputStream(filePath, true)
             */

            for (Student s : studentMap.values()) {
                String line = s.getId() + ","
                        + s.getName() + ","
                        + s.getAge() + ","
                        + s.getMajor() + ","
                        + s.getScore();
                writer.write(line);//把这一行字符串写进文件（其实先写进缓冲区）。
                writer.newLine();   // 换行,newLine() 会自动适配当前操作系统的换行符。
                //如果不换行，所有学生会被写在同一行，分割逗号会出现错误，得到超出5个字段，数据全部丢失
            }
            /*s.getAge() 返回 int，s.getScore() 返回 double。
            Java 会自动把它们转成字符串（这个规则叫"字符串拼接时自动 toString"）
            最终输出line:"2024001,ljq,20,计算机,85.5"
             这个格式必须和 loadFromFile() 里 split(",") 对得上！
             这就是"序列化"和"反序列化" —— 两个方法必须配对。
            */

        } catch (IOException e) {
            System.out.println("写入文件失败：" + e.getMessage());
            // 和 loadFromFile() 一样：出错只打印提示，不让程序崩溃
        }
    }

    @Override
    public void add(Student student) {
        studentMap.put(student.getId(), student);
        saveToFile();
    }

    @Override
    public Student findById(String id) {
        return  studentMap.get(id);
    }

    @Override
    public List<Student> findAll() {
        return  new ArrayList<>(studentMap.values());
    }

    @Override
    public void update(Student student) {
        studentMap.put(student.getId(), student);
        saveToFile();
    }

    @Override
    public void delete(String id) {
        studentMap.remove(id);
        saveToFile();
    }

    @Override
    public boolean existsById(String id) {
        return studentMap.containsKey(id);
    }
}
