package com.safelogisitics.gestionentreprisesusers.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class UploadFileHelper {

  public static String uploadFile(MultipartFile file, String url) {
    // Create to upload file if not exist
    final Path fileStorageLocation = Paths.get(url).toAbsolutePath().normalize();
    try {
      Files.createDirectories(fileStorageLocation);
    } catch (Exception ex) {
      throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
    }

    String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
    String fileName = "";

		try {
      if(originalFileName.contains("..")) {
        throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
      }

      String fileExtension = "";
      try {
        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
      } catch(Exception e) {
        throw new RuntimeException("Sorry! Filename is invalid. " + originalFileName);
      }

      fileName = UUID.randomUUID().toString() + "-teaser" + fileExtension;
      Path targetLocation = fileStorageLocation.resolve(fileName);

      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
    }

    return fileName;
  }

}
