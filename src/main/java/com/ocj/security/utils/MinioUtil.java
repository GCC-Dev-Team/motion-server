package com.ocj.security.utils;


import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MinioUtil {
    //必须使用注入的方式否则会出现空指针
    @Autowired
    MinioClient minioClient;
    /**
     * 储存桶的名称
     */
    private final static String BUCKET_NAME = "video";

    private final static String prefix = "https://guangxiaoqing.com:9000" + "/" + BUCKET_NAME;

    /**
     * 查看存储bucket是否存在
     * bucketName 需要传入桶名
     *
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     * bucketName 需要传入桶名
     *
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     * bucketName 需要传入桶名
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            return buckets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件下载
     *
     * @param fileName 文件名称
     *                 BucketName 需要传入桶名
     * @param res      response
     * @return Boolean
     */
    public void download(String fileName, HttpServletResponse res) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket("xiaoli")
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                // 设置强制下载不打开
                // res.setContentType("application/force-download");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 查看文件对象
     * BucketName 需要传入桶名
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(BUCKET_NAME).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }


//----------------------------------------------------------------------------------

    /**
     * 文件上传
     *
     * @param file 文件
     *             BucketName 需要传入桶名
     *             <p>
     *             prefixm目录
     * @return Boolean
     */
    public String upload(MultipartFile file, String prefix) {
        return upload(file, prefix, RandomUtil.generateRandomString(10));
    }

    public String upload(MultipartFile file, String prefix, String fileNewName) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                file.getName();

                originalFilename=file.getName();
            }

            // 生成随机文件名
            String fileName = prefix + "/" + fileNewName + originalFilename.substring(originalFilename.lastIndexOf("."));

            // 获取文件输入流
            try (InputStream inputStream = file.getInputStream()) {
                // 执行文件上传
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(fileName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            return getUrlByName(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            // 在实际应用中，您可以选择记录异常或者抛出自定义异常以进行更好的错误处理
            return null;
        }

    }

    /**
     * 预览
     *
     * @param fileName BucketName 需要传入桶名
     * @return
     */
    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder().bucket(BUCKET_NAME).object(fileName).method(Method.GET).build();
        try {
            return minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 删除
     *
     * @param fileName BucketName 需要传入桶名
     * @return
     * @throws Exception
     */
    public boolean remove(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(BUCKET_NAME).object(fileName).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public String getUrlByName(@NotNull String name) {

        return prefix + "/" + name;
    }

    public String removes(String[] fileNameList) {


        int temple = 0;
        List<String> errorNameList = new ArrayList<>();
        for (String s : fileNameList) {

            String s1 = s.substring(prefix.length());

            boolean remove = remove(s1);

            if (!remove) {
                temple = temple + 1;

                errorNameList.add(s);

                continue;
            }

        }
        if (temple > 0) {
            return errorNameList.toString();
        }

        return "成功删除!";
    }

}
