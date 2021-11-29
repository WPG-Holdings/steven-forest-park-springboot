package com.wpg.stevenforestparkspringboot.iface.rest;

import com.wpg.stevenforestparkspringboot.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

/**
 * @author Samuel Luo
 */
@RestController
@RequestMapping(path = "upload")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @PostMapping("json")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile uploadFile) {
        try {
            if (uploadFile.isEmpty()) {
                throw new Exception("please select a file!");
            }

            List<MultipartFile> files = Collections.singletonList(uploadFile);
            List<String> tmpList = uploadService.saveUploadFiles(files);
            uploadService.saveMultiFiles(tmpList);

            return new ResponseEntity<>("Successfully uploaded", new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
