package com.fwtai.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Lambda 表达式测试,Stream是元素的集合的操作
 * 总结:
 *     语法格式1:无参数无返回值
 *     语法格式2:有一个参数1且无返回值
 *     语法格式3:若只有一个参数1且无返回值,那小括号可以省略[左右遇1括号省]
 *     语法格式4:有两个及两个以上的参数,有返回值,且Lambda体中有多条语句,注意:如果Lambda体有多条执行语句的话,那必须要用大括号{}括起!!!
 *     语法格式5:有两个及两个以上的参数,有返回值,且Lambda体只有一条语句是,那return 和大括号{}都可以省略[左右遇1括号省]
 *     语法格式6:Lambda表达式参数列表的数据类型可以省略不写,因为JVM 编译器通过上下文推断出数据类型,即"类型推断"
 *  左右遇1括号省,即左边的只有一个参数时或右边的只有1条语句时,括号可以省略
 *  左侧推断类型省
 *  能省则省
 *
 *  Lambda 表达式需要"函数式接口"的支持
 *  什么是函数式接口呢??? 接口中有且只有一个抽象方法的接口就是函数式接口,可以使用注解@FunctionalInterface 来检查是否是函数式接口
 *
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019-06-07 23:37
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public class Lambda{

    public static void main(String[] args){
        funTest();
    }

    /*********************************************分隔线*********************************************/
    public static void comparison1A(){
        final Comparator<Integer> comparator = new Comparator<Integer>(){
            @Override
            public int compare(Integer o1,Integer o2){
                return Integer.compare(o1,o2);
            }
        };
        TreeSet<Integer> ts = new TreeSet<Integer>(comparator);
        System.out.println(ts);
    }

    public static void comparison1B(){
        final Comparator<Integer> comparator = (o1,o2) -> Integer.compare(o1,o2);
        final TreeSet<Integer> ts = new TreeSet<Integer>(comparator);
        System.out.println(ts);
    }

    /*********************************************分隔线*********************************************/

    public static void comparison2A(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                System.out.println("使用匿名内部类实现");
            }
        }).start();
    }

    public static void comparison2B(){
        new Thread(() -> System.out.println("使用JDK8的Lambda表达式实现")).start();//Lambda表达式可以理解为是一段可以传递的代码，代码像数据一样的传递
    }

    /*********************************************分隔线*********************************************/

    public static void comparison3A(){
        final List<String> languages = Arrays.asList("java","scala","python");
        //before java8
        for(String each:languages) {
            System.out.println(each);
        }
    }

    public static void comparison3B(){
        final List<String> languages = Arrays.asList("java","scala","python");
        //after java8,当lambda表达式的参数个数只有一个，可以省略小括号()
        languages.forEach(language -> System.out.println(language));//Lambda表达式可以理解为是一段可以传递的代码，代码像数据一样的传递
        //或
        languages.forEach(System.out::println);
    }

    /*********************************************分隔线*********************************************/
    //map函数可以说是函数式编程里最重要的一个方法了。map的作用是将一个对象变换为另外一个。在我们的例子中，就是通过map方法将cost增加了0,05倍的大小然后输出
    public static void lambdamap(){
        final List<Double> l = new ArrayList<Double>();
        final List<Double> list = Arrays.asList(10.0,20.0,30.0);
        list.stream().map(element -> element + element * 0.05).forEach(el -> System.out.println(el));
        //取出
        list.stream().map(element -> element + element * 0.05).forEach(el -> l.add(el));
        System.out.println(l);
    }

    /*********************************************分隔线*********************************************/

    //reduce与map一样，也是函数式编程里最重要的几个方法之一……,map的作用是将一个对象变为另外一个，而reduce实现的则是将所有值合并为一个
    public static void mapReduceA(){
        final List<Double> list = Arrays.asList(10.0,20.0,30.0);
        double sum = 0;
        for(double d:list) {
            d += d * 0.05;
            sum += d;
        }
        System.out.println(sum);
    }

    public static void mapReduceB(){
        final List<Double> list = Arrays.asList(10.0,20.0,30.0);
        final double allCost = list.stream().map(element -> element + element * 0.05).reduce((sum,el) -> sum + el).get();
        System.out.println(allCost);
    }

    /*********************************************分隔线*********************************************/
    //filter也是我们经常使用的一个操作。在操作集合的时候，经常需要从原始的集合中过滤掉一部分元素。
    public static void filter(){
        List<Double> list = Arrays.asList(10.0,20.0,30.0,40.0);
        List<Double> filteredCost = list.stream().filter(x -> x > 25.0).collect(Collectors.toList());
        filteredCost.forEach(x -> System.out.println(x));
    }

    /*********************************************分隔线*********************************************/

    public void lambdaThis(){
        //转全小写
        final List<String> list = Arrays.asList(new String[]{"Ni","Hao","Lambda"});
        List<String> execStrs = list.stream().map(str -> {
            System.out.println(this.getClass().getName());
            return str.toLowerCase();
        }).collect(Collectors.toList());
        execStrs.forEach(System.out::println);
    }
    /*********************************************分隔线*********************************************/

    //语法格式1:无参数无返回值
    private static void voidParams(){
        //匿名内部类实现
        final Runnable runnable = new Runnable(){
            @Override
            public void run(){
                System.out.println("无参数无返回值!");
            }
        };
        //调用
        runnable.run();
        /************************************/
        //lambda表达式实现,无参数无返回值
        final Runnable run = () -> System.out.println("lambda表达式实现");
        run.run();
    }

    /*********************************************分隔线*********************************************/

    //语法格式2:有一个参数1且无返回值,它是对抽象方法的实现
    private static void paramOne(){
        final Consumer<String> consumer = (param) -> System.out.println(param);
        //调用?
        consumer.accept("有一个参数1且无返回值,它是对抽象方法的实现!");
    }

    /*********************************************分隔线*********************************************/

    //语法格式3:若只有一个参数1且无返回值,那小括号可以省略
    private static void paramKuohao(){
        final Consumer<String> consumer = param -> System.out.println(param);
        //调用?
        consumer.accept("若只有一个参数1且无返回值,那小括号可以省略!!!");
    }

    /*********************************************分隔线*********************************************/

    //语法格式4:有两个及两个以上的参数,有返回值,且Lambda体中有多条语句,注意:如果Lambda体有多条执行语句的话,那必须要用大括号{}括起!!!
    private static void paramMulti(){
        final Comparator<Integer> comparator = (v1,v2) -> {
            System.out.println("函数式接口编程,如果Lambda体有多条执行语句的话,那必须要用大括号{}括起!!!");
            return Integer.compare(v1,v2);//返回值为-1时 v1>v2;当v1=v2时则是0;当v1<v2时是1
        };
        final Integer result = comparator.compare(1,25);//表示,返回值为-1时 v1>v2;当v1=v2时则是0;当v1<v2时是1
        System.out.println(result);
    }

    /*********************************************分隔线*********************************************/

    //语法格式5:有两个及两个以上的参数,有返回值,且Lambda体只有一条语句是,那return 和大括号{}都可以省略
    private static void paramMultiReturn(){
        final Comparator<Integer> comparator = (v1,v2) -> Integer.compare(v1,v2);
        final Integer result = comparator.compare(50,20);//表示,返回值为-1时 v1>v2;当v1=v2时则是0;当v1<v2时是1
        System.out.println("有两个及两个以上的参数,有返回值,且Lambda体只有一条语句是,那return 和大括号{}都可以省略");
        System.out.println(result);
    }

    /*********************************************分隔线*********************************************/

    //语法格式6:Lambda表达式参数列表的数据类型可以省略不写,因为JVM 编译器通过上下文推断出数据类型,即"类型推断"
    private static void paramList(){
        final Comparator<Integer> comparator = (Integer v1,Integer v2) -> Integer.compare(v1,v2);
        final Integer result = comparator.compare(10,5);//表示,返回值为-1时 v1>v2;当v1=v2时则是0;当v1<v2时是1
        System.out.println("Lambda表达式参数列表的数据类型可以省略不写,因为JVM 编译器通过上下文推断出数据类型,即\"类型推断\"");
        System.out.println(result);
    }

    /*********************************************分隔线*********************************************/

    //自定义一个函数式接口并使用演示
    private static Integer operation(final Integer number,final NumberFunction function){
        return function.getValue(number);
    }

    //调用函数式接口方法
    private static void funTest(){
        final Integer result = operation(100,(x) -> x * x);//实现1,第2个参数是函数;左边是参数列表,右边是方法体
        final Integer rows = operation(10,x -> x * x);//实现2,第2个参数是函数,括号可以省略;左边是参数列表,右边是方法体
        System.out.println(result);
        System.out.println(rows);
        System.out.println(operation(20,(number) -> number + 10));
        System.out.println(operation(8,(number) -> {
            return number + 30;
        }));//实现2,第2个参数是函数,括号可以省略;左边是参数列表,右边是方法体
    }

    /*********************************************分隔线*********************************************/
}