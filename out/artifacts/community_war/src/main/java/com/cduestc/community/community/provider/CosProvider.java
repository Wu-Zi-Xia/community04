package com.cduestc.community.community.provider;

import com.cduestc.community.community.exception.CustomizeErrorCode;
import com.cduestc.community.community.exception.CustomizeException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class CosProvider {

    public  COSClient getCosClient(){
        //1设置访问的秘钥
        String  secretId = "AKIDA7JH2ZitfBTbT5HWLuCXjmbCZgyMGibE";
        String secretKey = "XppnUosgDYcPvAG8JGvKSkna7OPAYhbI";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        Region region = new Region("ap-chengdu");
        //对客户端进行属性设置，可以设置很多的属性
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }
    public String upload(MultipartFile file, FileInputStream fileInputStream, COSClient cosClient) throws IOException {
        try {
            //从请求中去获取到文件的流
            fileInputStream =(FileInputStream)file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 设置输入流长度为500
            objectMetadata.setContentLength(500);
            // 设置 Content type, 默认是 application/octet-stream
            // 指定要上传到的存储桶
            String bucketName = "wuzixia-1300212146";
            // 指定要上传到 COS 上对象键（对象键其实就是你在 存储桶中的图片所在的路径/图片的名称）

            String originalFileName=file.getOriginalFilename();
            String key = "communityApplication/"+System.currentTimeMillis()+originalFileName;
            //通过创建的cos客户端，执行文件上传的请求。
            PutObjectResult putObjectResult = cosClient.putObject(bucketName,key,fileInputStream,objectMetadata);
            String etag = putObjectResult.getETag();
//            System.out.println("==========================="+putObjectResult.getDateStr());
            if (putObjectResult!=null&&putObjectResult.getETag()!=null)
            {
//                https://wuzixia-1300212146.cos.ap-chengdu.myqcloud.com/communityApplication/2019-10-19%2017%3A27%3A29280082-151001215G3.jpg
                return " https://wuzixia-1300212146.cos.ap-chengdu.myqcloud.com/"+key;
            }
            else
                {
                    throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
                }
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileInputStream.close();
        }
        return null;
    }

}
