package com.example.backend.service.impl.user.account;

import com.example.backend.service.user.account.ShowphotoService;
import com.example.backend.utils.PhotoUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowphotoServiceImpl implements ShowphotoService {
    @Override
    public List<PhotoUtil> showphoto() {
        List<String> photos = new ArrayList<>();
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202212072159086.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211242052207.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211232308530.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211232308493.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211231920870.webp");
        //photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211231918014.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211231910960.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/45301314.png");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/typora-img/202209050905112.jpg");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/typora-img/202207191026188.webp");
        List<PhotoUtil> ans = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            PhotoUtil photoUtil = new PhotoUtil(i + 1, photos.get(i));
            ans.add(photoUtil);
        }
        return ans;
    }
}
