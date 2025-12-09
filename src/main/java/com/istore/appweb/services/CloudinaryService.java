package com.istore.appweb.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

  @Autowired
  private Cloudinary cloudinary;

  public String subirImagen(MultipartFile file) throws IOException {
    // 1. Convertir MultipartFile a File (Cloudinary lo prefiere así a veces, o
    // bytes)
    File archivoConvertido = convertir(file);

    // 2. Subir a Cloudinary
    // "folder" es opcional, sirve para organizar tus fotos en una carpeta en la
    // nube
    @SuppressWarnings("unchecked")
    Map<String, Object> resultado = cloudinary.uploader().upload(archivoConvertido,
        ObjectUtils.asMap("folder", "istore_productos"));

    // 3. Borrar el archivo temporal del servidor (limpieza)
    archivoConvertido.delete();

    // 4. Retornar la URL segura (https)
    return (String) resultado.get("secure_url");
  }

  // Método auxiliar para convertir el archivo
  private File convertir(MultipartFile file) throws IOException {
    File convFile = new File(file.getOriginalFilename());
    try (FileOutputStream fos = new FileOutputStream(convFile)) {
      fos.write(file.getBytes());
    }
    return convFile;
  }
}