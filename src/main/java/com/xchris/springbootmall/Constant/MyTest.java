package com.xchris.springbootmall.Constant;

public class MyTest {
    public static void main(String[] args) {
        // 取得ProductCategory.FOOD enum 的值
        ProductCategory category = ProductCategory.FOOD;
        // 用String變數去存取category.name()
        String s = category.name();
        System.out.println(s); // FOOD
        System.out.println(category.name());


        String s2 = "CAR";
        // 這裡是用String 去尋找在ProductCategory 有沒有符合這個字串的
        ProductCategory category2 = ProductCategory.valueOf(s2);
        System.out.println(category2);

    }
}
