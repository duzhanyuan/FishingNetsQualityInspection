package org.jaiken.model;

import java.util.Vector;

public class test {
    public static void main(String[] args){
        Vector<Integer> t =new Vector<Integer>();
        t.add(10);
        t.add(9);
        t.add(8);
        t.add(7);
        t.add(6);
        t.add(5);
        t.add(4);
        t.add(3);
        t.add(2);
        t.add(1);
        for(int i:t){
            System.out.println(i);
        }
    }
}
