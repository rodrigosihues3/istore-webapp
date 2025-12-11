package com.istore.appweb.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Service
public class CloudinaryService {

  @Autowired
  private Cloudinary cloudinary;

  public String subirImagen(MultipartFile file) throws IOException {
    File archivoConvertido = convertir(file);

    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> resultado = cloudinary.uploader().upload(archivoConvertido,
          ObjectUtils.asMap("folder", "istore_productos"));

      return (String) resultado.get("secure_url");
    } finally {
      // Borrar el archivo temporal siempre (incluso si falla la subida)
      if (archivoConvertido.exists()) {
        archivoConvertido.delete();
      }
    }
  }

  private File convertir(MultipartFile file) throws IOException {
    // Files.createTempFile crea el archivo en /tmp (Linux) o %TEMP% (Windows)
    File convFile = Files.createTempFile("temp", file.getOriginalFilename()).toFile();

    // transferTo es más eficiente que FileOutputStream
    file.transferTo(convFile);

    return convFile;
  }
}