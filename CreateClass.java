package com.javarush.task.task36.task3606;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateClass extends ClassLoader{
    public Class createClass(Path path) {
        Class result = null;
        try {
            // 1. свой ClassLoader - использовать обязательно.
            /* Для создания нового класса необходимо получить корректный байт-код класса (образ в памяти обычного
             .class-файла) в виде массива byte[] */
            byte[] classBytes = Files.readAllBytes(path);
            /* Затем его нужно передать специальному стандартному методу ClassLoader.defineClass, который «превратит»
            его в готовый класс – объект типа Class. */
            result = defineClass(null, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            System.out.println("Cannot load class " + e);
        } catch (ClassFormatError e) {
            System.out.println("Format of class file incorrect for class " + e);
        }
        return result;
    }
}
