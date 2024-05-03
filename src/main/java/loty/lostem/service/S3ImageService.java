package loty.lostem.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3ImageService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public String upload (MultipartFile image, String folderPath) { // 이미지 파일이 빈 파일인지 검증
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new AmazonS3Exception("Empty file exception");
        }
        return this.uploadImage(image, folderPath); // s3 에 저장된 이미지 url 반환
    }

    private String uploadImage (MultipartFile image, String folderPath) {
        this.validateImageFileExtension(image.getOriginalFilename());
        try {
            return this.uploadImageToS3(image, folderPath);
        } catch (IOException e) {
            throw new AmazonS3Exception("IO Exception on image upload");
        }
    }

    private String uploadImageToS3 (MultipartFile image, String folderPath) throws IOException {
        String originalFilename = folderPath + "/" + image.getOriginalFilename(); // 원본 파일 명
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); // 확장자 명

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);  // image를 byte[] 로 변환

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);  // = metaData.setContentType(image.getContentType());
        metadata.setContentLength(bytes.length);  // = image.getSize()
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes); // s3에 요청할 때 사용할 byteInputStream 생성  // = file.getInputStream()

        try {
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);  // s3에 putObject 할 때 사용할 요청 객체
            amazonS3.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new AmazonS3Exception("put object exception");
        } finally {
            byteArrayInputStream.close();
            is.close();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void deleteImageFromS3 (String imageAddress) {
        String key = getKeyFromImageAddress(imageAddress);

        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            throw new AmazonS3Exception("IOException on image delete");
        }
    }



    // 확장자가 올바른지 확인
    private void validateImageFileExtension (String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new AmazonS3Exception("No file extension");
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtensionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtensionList.contains(extension)) {
            throw new AmazonS3Exception("Invalid file extension");
        }
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1);  // 맨 앞의 '/' 제거
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new AmazonS3Exception("IO exception on image delete");
        }
    }
}
