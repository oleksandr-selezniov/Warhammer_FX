package Utils;

/**
 * Created by Glazyrin.D on 11/22/2016.
 */
import java.io.*;
import java.nio.file.NoSuchFileException;

public class ResourceLoader
{
    private String filePath;

    public ResourceLoader(String filePath)
    {
        this.filePath = filePath;

        if(filePath.startsWith("/"))
        {
            throw new IllegalArgumentException("Relative paths may not have a leading slash!");
        }
    }
    public InputStream getResource() throws NoSuchFileException
    {
        ClassLoader classLoader = this.getClass().getClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream(filePath);

        if(inputStream == null)
        {
            throw new NoSuchFileException("Resource file not found. Note that the current directory is the source folder!");
        }
        return inputStream;
    }


    public InputStream getResourceFile() throws NoSuchFileException
    {
        ClassLoader classLoader = this.getClass().getClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream(filePath);
        OutputStream outputStream = null;

        if(inputStream == null)
        {
            throw new NoSuchFileException("Resource file not found. Note that the current directory is the source folder!");
        }



        File file = null;
        try {
            inputStream = new FileInputStream(filePath);
            outputStream = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }catch (Exception e){e.printStackTrace();}


        return inputStream;
    }
}