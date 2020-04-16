package nl.robinlaugs.unzip;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Main {

    private static final String ROOT_FILE_LOCATION = "archive.zip";

    public static void main(String[] args) throws IOException {
        FileData rootFile = readRootFile();

        List<FileData> unzippedFiles = UnzipUtil.unzip(rootFile);

        unzippedFiles.forEach(System.out::println);
    }

    private static FileData readRootFile() throws IOException {
        try (FileInputStream fis = new FileInputStream(ROOT_FILE_LOCATION)) {
            FileData rootFile = new FileData();
            rootFile.setName(ROOT_FILE_LOCATION);
            rootFile.setData(IOUtils.toByteArray(fis));

            return rootFile;
        }
    }

}
