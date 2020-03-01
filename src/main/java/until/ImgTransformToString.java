package until;

import pojo.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
*用于把用,隔开的子字符串从otherimg提取出来
*/
public class ImgTransformToString {

    public List<String> imgTransform(Image img){

        List<String> imgList = new ArrayList<>(Arrays.asList(img.getOtherImg().split(",")));
        imgList.add(img.getMainImg());
        return imgList;
    }
}
