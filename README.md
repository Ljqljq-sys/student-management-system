# 学生管理系统（Student Management System）

基于 Java SE 开发的控制台学生信息管理系统，采用三层架构设计，
支持学生信息的增删改查、数据持久化、模糊搜索、成绩排序与统计。

## 功能特性

- ✅ 学生信息增删改查
- ✅ 数据持久化（保存到文件，重启不丢失）
- ✅ 按姓名模糊搜索
- ✅ 按成绩排序（升序/降序）
- ✅ 成绩统计（平均分、最高分、最低分、及格率）
- ✅ 完善的输入校验（非空、数字格式、成绩范围）
- ✅ 业务规则校验（学号唯一、学生存在性）
- ✅ 异常处理（自定义异常，程序不会崩溃）

## 技术栈

- Java 17
- 面向对象编程（封装、继承、多态、接口）
- 集合框架（LinkedHashMap、ArrayList）
- 文件 IO（BufferedReader / BufferedWriter，UTF-8 编码）
- 异常处理（自定义 RuntimeException）
- Java 8 特性（Lambda、方法引用、Comparator、Stream）

## 项目架构

采用经典三层架构，各层职责分明：
┌─────────────────────────────────────┐ │ UI 层（StudentUI） │ 菜单交互、输入校验、异常展示 ├─────────────────────────────────────┤ │ Service 层（StudentServiceImpl） │ 业务规则：学号唯一、成绩范围 ├─────────────────────────────────────┤ │ DAO 层（StudentDaoFileImpl） │ 数据存取：读写文件 ├─────────────────────────────────────┤ │ 数据层（data/students.txt） │ 持久化存储 └─────────────────────────────────────┘
**设计要点：** DAO 层定义了 `StudentDao` 接口，
调用方只依赖接口不依赖实现，因此更换存储方式（如换成数据库）只需新增实现类，
上层代码无需改动。

## 项目结构

src/com/ljq/sms/ ├── Main.java # 程序入口 ├── entity/ │ └── Student.java # 学生实体 ├── dao/ │ ├── StudentDao.java # 数据访问接口 │ ├── StudentDaoImpl.java # 内存实现 │ └── StudentDaoFileImpl.java # 文件实现 ├── service/ │ ├── StudentService.java # 业务逻辑接口 │ └── StudentServiceImpl.java # 业务逻辑实现 ├── exception/ │ ├── DuplicateIdException.java # 学号重复异常 │ └── StudentNotFoundException.java # 学生不存在异常 └── ui/ └── StudentUI.java # 控制台界面

## 如何运行

1. 安装 JDK 17 或更高版本
2. 克隆项目
3. 用 IntelliJ IDEA 打开，运行 `Main.java`
4. 按菜单提示操作

## 数据存储格式

`data/students.txt`，每行一个学生，逗号分隔：
## 遇到的问题与解决

### 1. 替换存储方式时如何避免修改上层代码

**问题：** 最初只有内存版 DAO，数据无法持久化。
**解决：** 先定义 `StudentDao` 接口，编写 `StudentDaoFileImpl` 实现类。
上层只依赖接口，更换实现时只需修改 `new` 后面的类名一处。

### 2. 中文写入文件乱码

**问题：** 使用默认编码读写文件，中文出现乱码。
**解决：** 在 `InputStreamReader` / `OutputStreamWriter` 中显式指定
`StandardCharsets.UTF_8`。

### 3. 查询不存在的学生导致空指针

**问题：** `findById` 查不到时返回 null，调用方直接使用会抛
`NullPointerException`。
**解决：** Service 层统一处理，`getStudent` 在查不到时抛出
`StudentNotFoundException`，强制调用方处理。

### 4. 修改学生时误将新学生插入

**问题：** `Map.put` 在 key 不存在时会新增，导致"修改"变成"添加"。
**解决：** Service 层在 `updateStudent` 中先校验学生是否存在。

## 后续计划

- [ ] 增加数据校验（学号格式、年龄范围）
- [ ] 支持分页显示
- [ ] 导出 CSV / Excel
- [ ] 改造为 Web 版本（Spring Boot + MySQL）
- [ ] 添加登录与权限控制