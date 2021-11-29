package com.wpg.stevenforestparkspringboot.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.wpg.stevenforestparkspringboot.model.Operation;
import com.wpg.stevenforestparkspringboot.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

/**
 * @author Samuel Luo
 */
@Service
public class UploadService {

    @Autowired
    OperationRepository operationRepository;

    @Value("${upload.file.store.path}")
    String storageRoot;

    public List<String> saveUploadFiles(List<MultipartFile> files) throws Exception {
        List<String> tmpList = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue;
            }

            File folder = new File(storageRoot);
            if (!folder.exists()) {
                boolean result = folder.mkdir();
                if (!result) {
                    throw new Exception("Failed to mkdir ::" + folder.getAbsolutePath());
                }
            }

            String saveFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File saveFile = new File(folder, saveFileName);
            tmpList.add(saveFile.toPath().toString());
            Files.write(saveFile.toPath(), file.getBytes());
        }

        return tmpList;
    }

    public void saveMultiFiles(List<String> tmpList) {
        for (String filename : tmpList) {
            saveFileContent(filename);
        }
    }

    private void saveFileContent(String path) {
        try {
            InputStream stream = new FileInputStream(path);
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<Operation> personIter = new CsvMapper().readerFor(Operation.class).with(schema).readValues(stream);
            operationRepository.saveAll(personIter.readAll());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
