package inandout.backend.service.myroom;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private String location = "ap-northeast-2";

    private final AmazonS3 s3Client;

    public List<String> uploadFile(List<MultipartFile> multipartFile, String postId) {
        System.out.println("S3Service/uploadFile");
        System.out.println("uploadFile/postId: "+postId);
        List<String> fileNameList = new ArrayList<>();

        multipartFile.forEach(file -> {
            System.out.println(file.toString());
            String fileName = createFileName(file.getOriginalFilename()); // 파일 이름 가져옴
            fileName = postId+"-"+fileName;
            ObjectMetadata objectMetadata = new ObjectMetadata(); // s3에 업로드되는 객체 관련 정보
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            System.out.println("fileName: "+fileName);
            System.out.println("fileNametype: "+file.getContentType());

            String image_url = "https://"+bucket+".s3."+location+".amazonaws.com/"+fileName;

            try(InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new RuntimeException("uploadFile 오류");
            }
            fileNameList.add(image_url);
        });

        return fileNameList;
    }


    public void deleteFile(Integer postId, List<String> imageUrls){
        System.out.println("deleteFile from S3!");

        for (String imageUrl : imageUrls) {
            System.out.println("삭제: " + imageUrls);
            s3Client.deleteObject(bucket, imageUrl);
        }


    }

    // 파일명 중복 방지 (UUID)
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            return "파일 없음";
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            return "파일 잘못된 형식";
        }

        return fileName.substring(fileName.lastIndexOf("."));
    }




}
