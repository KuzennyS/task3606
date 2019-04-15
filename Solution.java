package com.javarush.task.task36.task3606;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/*
Осваиваем ClassLoader и Reflection
*/
public class Solution {
    private List<Class> hiddenClasses = new ArrayList<>();
    private String packageName;

    public Solution(String packageName) {
        this.packageName = packageName;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Solution solution = new Solution(Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "com/javarush/task/task36/task3606/data/second");
        solution.scanFileSystem();
        System.out.println(solution.getHiddenClassObjectByKey("secondhiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("firsthiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("packa"));
    }

    public void scanFileSystem() throws ClassNotFoundException {
        File[] files = new File(packageName).listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".class")) {
                Class clazz = new CreateClass().createClass(file.toPath());
                hiddenClasses.add(clazz);
            }
        }
    }

    public HiddenClass getHiddenClassObjectByKey(String key) {
        for (int i = 0; i < hiddenClasses.size(); i++) {
            if (hiddenClasses.get(i).getSimpleName().toLowerCase().contains(key.toLowerCase())){
                Constructor[] ctors = hiddenClasses.get(i).getDeclaredConstructors();
                Constructor ctor = null;
                for (int j = 0; j < ctors.length; j++) {
                    ctor = ctors[j];
                    if (ctor.getGenericParameterTypes().length == 0)
                        break;
                }
                try {
                    ctor.setAccessible(true);
                    HiddenClass c = (HiddenClass) ctor.newInstance();
                    return c;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}

