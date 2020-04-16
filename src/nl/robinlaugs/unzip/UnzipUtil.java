package nl.robinlaugs.unzip;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtil {

    private static final Logger logger = Logger.getLogger(UnzipUtil.class.getName());

    private static final List<Integer> ZIP_FILE_SIGNATURES = Arrays.asList(0x504B0304, 0x504B0506, 0x504B0708);

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
                } else {
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
             DataInputStream dataIn = new DataInputStream(bufferedIn)) {

            return ZIP_FILE_SIGNATURES.contains(dataIn.readInt());
        } catch (IOException e) {
            return false;
        }
    }
}
