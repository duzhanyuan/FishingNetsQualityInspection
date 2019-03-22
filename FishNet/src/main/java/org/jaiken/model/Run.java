package org.jaiken.model;

import java.io.File;

public class Run {

    public static void main(String[] args){
        FishNetSingleClassification fishNetSingleClassification=new FishNetSingleClassification();
        try {
            fishNetSingleClassification.detectWrong(new File(System.getProperty("user.dir") + "/src/main/resources/FishNetTest"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
