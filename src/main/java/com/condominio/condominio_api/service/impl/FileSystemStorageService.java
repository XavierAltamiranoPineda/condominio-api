package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.service.interfaces.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(@Value("${app.storage.location:uploads}") String location) {
        this.rootLocation = Paths.get(location);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar la carpeta de almacenamiento", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new BusinessException("No se puede almacenar un archivo vacío.");
            }
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = "";
            if (filename.contains(".")) {
                extension = filename.substring(filename.lastIndexOf("."));
            }
            
            String mimeType = file.getContentType();
            if (mimeType == null || (!mimeType.equals("application/pdf") 
                    && !mimeType.equals("image/jpeg") 
                    && !mimeType.equals("image/png"))) {
                throw new BusinessException("Formato no soportado. Solo PDF, JPG y PNG.");
            }

            String newFilename = UUID.randomUUID().toString() + extension;
            Path destinationFile = this.rootLocation.resolve(Paths.get(newFilename)).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new BusinessException("No se puede almacenar el archivo fuera del directorio actual.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return newFilename;
        } catch (IOException e) {
            throw new BusinessException("Fallo al almacenar el archivo.", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Archivo", "nombre", filename);
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Archivo", "nombre", filename);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new BusinessException("No se pudo eliminar el archivo", e);
        }
    }
}
