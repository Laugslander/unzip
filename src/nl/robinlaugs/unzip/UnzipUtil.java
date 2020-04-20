package nl.robinlaugs.unzip;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtil {

    private static final Logger logger = Logger.getLogger(UnzipUtil.class.getName());

    public static List<FileData> unzip(FileData file) {
        List<FileData> unzippedFiles = new ArrayList<>();

        try (ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(file.getData());
             BufferedInputStream bufferedIn = new BufferedInputStream(byteArrayIn);
             ZipInputStream zipIn = new ZipInputStream(bufferedIn)) {

            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                FileData unzippedFile = new FileData();
                unzippedFile.setName(entry.getName());
                unzippedFile.setData(IOUtils.toByteArray(zipIn));

                if (isZipped(unzippedFile)) {
                    unzippedFiles.addAll(unzip(unzippedFile));
                } else if (!entry.isDirectory()) {
                    unzippedFiles.add(unzippedFile);
                }
            }

            return unzippedFiles;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while unzipping a file", e);

            return Collections.emptyList();
        }
    }

    private static boolean isZipped(FileData file) {
        try (ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(file.getData());
             BufferedInputStream bufferedIn = new BufferedInputStream(byteArrayIn);
             ZipInputStream zipIn = new ZipInputStream(bufferedIn)) {

            return zipIn.getNextEntry() != null;
        } catch (IOException e) {
            return false;
        }
    }
}
