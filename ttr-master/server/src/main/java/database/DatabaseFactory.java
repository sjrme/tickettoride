package database;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DatabaseFactory {

    public static IDatabase getDatabase(String databaseType, int commandsUntilSave){
        IDatabase iDatabase=null;
        String mURLPostfix=null;
        String className=null;

        HashMap<String, String> plugins;
        try {
            String filePath = "plugins.txt";
            plugins = new HashMap<String, String>();

            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(":", 2);
                if (parts.length >= 2)
                {
                    String key = parts[0];
                    String value = parts[1];
                    plugins.put(key, value);
                } else {

                }
            }

            for (String key : plugins.keySet())
            {
                System.out.println(key + ":" + plugins.get(key));
            }
            reader.close();
        } catch (IOException ex){
            return null;
        }


        if (plugins.get(databaseType) != null){
            mURLPostfix=File.separator + "server" + File.separator + "src" + File.separator + "main" +
                    File.separator + "java" + File.separator+ "PersistanceJar" + File.separator;
            mURLPostfix += databaseType;
            className = plugins.get(databaseType);
        }

        String pathToDB = Paths.get(".").toAbsolutePath().normalize().toString(); // get working directory
        pathToDB += (mURLPostfix);
        File file  = new File(pathToDB);

        URL url=null;
        URL[] urls=null;
        try {
            url = file.toURL();
            urls = new URL[]{url};
        }catch(MalformedURLException ex)
        {
            ex.printStackTrace();
            return null;
        }

        ClassLoader cl = new URLClassLoader(urls);
        // cl.g
        Class<?> cls=null;
        try {
            cls = cl.loadClass(className);
        }
        catch(ClassNotFoundException ex)
        {
            ex.printStackTrace();
            return null;
        }
        try {
            iDatabase = (IDatabase) cls.getDeclaredConstructor(new Class[] {int.class}).newInstance(commandsUntilSave);
            //iDatabase = (IDatabase) cls.newInstance();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }




        return iDatabase;

    }

}

