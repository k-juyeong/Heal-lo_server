package com.kh.heallo.web.uploadfile.controller;

import com.kh.heallo.domain.uploadfile.svc.UploadFileSVC;
import com.kh.heallo.web.FileSetting;
import com.kh.heallo.web.ResponseMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.nio.charset.Charset;

@RequiredArgsConstructor
@Controller
@Slf4j
public class UploadFileController {

    private final FileSetting fileSetting;
    private final UploadFileSVC uploadFileSVC;

    //이미지 연결
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileSetting.getFullPath(filename));
    }

    //이미지 삭제
    @ResponseBody
    @DeleteMapping("/images/{ufno}")
    public ResponseEntity<ResponseMsg> delete(@PathVariable("ufno") Long ufno) {
        Integer resultCount = uploadFileSVC.delete(ufno);
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setData(resultCount);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(responseMsg,headers,HttpStatus.OK);
    }
}
