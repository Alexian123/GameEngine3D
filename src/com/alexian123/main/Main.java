package com.alexian123.main;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.alexian123.engine.GameManager;

public class Main {

	public static void main(String[] args) {	
        try {
            // Specify the package name
            String packageName = "com.alexian123.play";
            // Convert package name to a path
            String path = packageName.replace('.', '/');
            // Get resources for the path
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
            List<File> dirs = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }

            // Find classes in the directories
            List<Class<?>> classes = new ArrayList<>();
            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }

            // Check if any class is derived from Game
            Class<?> baseClass = Class.forName("com.alexian123.game.Game");
            boolean found = false;
            for (Class<?> clazz : classes) {
                if (baseClass.isAssignableFrom(clazz) && !baseClass.equals(clazz)) {
                	found = true;
                    clazz.getDeclaredConstructor().newInstance();
            		GameManager.run();
                    break;
                }
            }
            
            if (!found) {
            	throw new Exception("No game class found in " + path);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }

}