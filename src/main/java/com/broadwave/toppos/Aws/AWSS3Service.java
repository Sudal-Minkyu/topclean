package com.broadwave.toppos.Aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Minkyu
 * Date : 2021-12-23
 * Remark :
 */
@Slf4j
@Service
public class AWSS3Service {

    @Value("${toppos.aws.s3.bucket}")
    private String AWSBUCKET;

    private final AmazonS3 s3Client;

    @Autowired
    public AWSS3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    // AWS 사진 파일 업로드
    public String imageFileUpload(MultipartFile multipartFile, String storedFileName,String uploadPath) throws IOException {

        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentType(multipartFile.getContentType());
        omd.setContentLength(multipartFile.getSize());
        omd.setHeader("filename", storedFileName);//한글명들어가면 오류남
        String awsFilePath =AWSBUCKET+ uploadPath;

        // Copy file to the target location (Replacing existing file with the same name)
        s3Client.putObject(new PutObjectRequest(awsFilePath, storedFileName, multipartFile.getInputStream(), omd));

        //이미지 화일이면 섬네일변환후 울리기 파일명앞에 "s_" 를 붙임
        if(Objects.requireNonNull(multipartFile.getContentType()).substring(0,5).equalsIgnoreCase("IMAGE")){
            BufferedImage originamImage = ImageIO.read(multipartFile.getInputStream());
            BufferedImage destImg = Scalr.resize(originamImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 200);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(destImg, getExtension(storedFileName), os);
            byte[] buffer = os.toByteArray();
            InputStream destImgInputStream = new ByteArrayInputStream(buffer);

            ObjectMetadata s_omd = new ObjectMetadata();
            s_omd.setContentType(multipartFile.getContentType());
            s_omd.setContentLength(buffer.length);
            s_omd.setHeader("filename", "s_" +storedFileName);//한글명들어가면 오류남

            // Copy file to the target location (Replacing existing file with the same name)
            s3Client.putObject(new PutObjectRequest(awsFilePath, "s_"+storedFileName, destImgInputStream, s_omd));

            return storedFileName;

        }else{
            return null;
        }
    }

    // AWS 일반 파일 업로드
    public void nomalFileUpload(MultipartFile multipartFile, String fileName,String uploadPath) throws IOException {

        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentType(multipartFile.getContentType());
        omd.setContentLength(multipartFile.getSize());
        omd.setHeader("filename", fileName);//한글명들어가면 오류남
        String awsFilePath =AWSBUCKET+ uploadPath;

        s3Client.putObject(new PutObjectRequest(awsFilePath, fileName, multipartFile.getInputStream(), omd));
    }

    // AWS 파일삭제
    public void deleteObject(String bucketPath, String fileName) throws AmazonServiceException {
        s3Client.deleteObject(new DeleteObjectRequest(AWSBUCKET +  bucketPath, fileName));
    }

    // 파일이름 UUID 길이체크
    private String getExtension(String fileName) {
        int dotPosition = fileName.lastIndexOf('.');

        if (-1 != dotPosition && fileName.length() - 1 > dotPosition) {
            return fileName.substring(dotPosition + 1);
        } else {
            return "";
        }
    }

//    // S3파일 다운로드 - 파일 key 로 해당 버킷에서 파일 찾아서 들고옴
//    public boolean download(String fileKey, String downloadFileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if (fileKey == null) {
//            return false;
//        }
//        S3Object fullObject = null;
//        try {
//            fullObject = s3Client.getObject(AWSBUCKET, fileKey);
//            if (fullObject == null) {
//                return false;
//            }
//        } catch (AmazonS3Exception e) {
//            log.info("e : "+e+ " 다운로드 파일이 존재하지 않습니다.");
//        }
//
//        OutputStream os = null;
//        FileInputStream fis = null;
//        boolean success = false;
//        try {
//            assert fullObject != null;
//            S3ObjectInputStream objectInputStream = fullObject.getObjectContent();
//            byte[] bytes = IOUtils.toByteArray(objectInputStream);
//
////            String fileName;
////            if(downloadFileName != null) {
////                //fileName= URLEncoder.encode(downloadFileName, "UTF-8").replaceAll("\\+", "%20");
////                fileName=  getEncodedFilename(request, downloadFileName);
////            } else {
////                fileName=  getEncodedFilename(request, fileKey); // URLEncoder.encode(fileKey, "UTF-8").replaceAll("\\+", "%20");
////            }
//
//            response.setContentType("application/octet-stream;charset=UTF-8");
//            response.setHeader("Content-Transfer-Encoding", "binary");
//            response.setHeader( "Content-Disposition", "attachment; filename=\"" + downloadFileName + "\";" );
//            response.setHeader("Content-Length", String.valueOf(fullObject.getObjectMetadata().getContentLength()));
//            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
//            FileCopyUtils.copy(bytes, response.getOutputStream());
//            success = true;
//        } catch (IOException e) {
//            log.info("에러 : "+e.getMessage(), e);
//        }
////        finally {
////            try {
////                if (fis != null) {
////                    fis.close();
////                }
////            } catch (IOException e) {
////                log.debug(e.getMessage(), e);
////            }
////            try {
////                if (os != null) {
////                    os.close();
////                }
////            } catch (IOException e) {
////                log.debug(e.getMessage(), e);
////            }
////        }
//        return success;
//    }
//
//    // 파일명이 한글인 경우 URL encode
//    private String getEncodedFilename(HttpServletRequest request, String displayFileName) throws UnsupportedEncodingException {
//        String header = request.getHeader("User-Agent");
//
//        String encodedFilename;
//        String encodedFile_8859_1 = "\"" + new String(displayFileName.getBytes(StandardCharsets.UTF_8), "8859_1") + "\"";
//
//        if (header.contains("MSIE") || header.contains("Trident")) {
//            encodedFilename = URLEncoder.encode(displayFileName, "UTF-8").replaceAll("\\+", "%20");
//        }
//        else if (header.contains("Chrome")) {
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < displayFileName.length(); i++) {
//                char c = displayFileName.charAt(i);
//                if (c > '~') {
//                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
//                } else {
//                    sb.append(c);
//                }
//            }
//            encodedFilename = sb.toString();
//        }
//        else if (header.contains("Opera")) {
//            encodedFilename =encodedFile_8859_1;
//        }
////        else if (header.contains("Safari")) {
////            encodedFilename = URLDecoder.decode(encodedFile_8859_1, "UTF-8");
////        }
//        else {
//            encodedFilename = URLDecoder.decode(encodedFile_8859_1, "UTF-8");
//        }
//        return encodedFilename;
//    }

}
