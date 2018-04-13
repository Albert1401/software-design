package ru.ifmo.ctd.pk1;


public class TestClass1 {
    public void f(){

    }

    public void f(int x){

    }

    public void f(double x, double y){

    }

    public void g(){

    }

    public void test(){
        f();
        f(4);
        f(5);
        f(2.0, 2.0);
        f(2.0, 2.0);
        f(2.0, 2.0);
        g();
    }
}
