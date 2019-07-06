package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVO;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {
	@Value("${image.localDirPath}")
	private String localDirPath;
	@Value("${image.urlPath}")
	private String urlPath;
	/**
	 * 1.获取图片名称
	 * 2.校验是否为图片的类型
	 * 3.校验是否为恶意程序
	 * 4.分文件保存 按时间存储 yyyy/MM/dd
	 * 5.防止文件重名  UUID 32位16进制数+毫秒数
	 */
	
	@Override
	public ImageVO updateFile(MultipartFile uploadFile) {
		ImageVO imageVO =  new ImageVO();
		//1.获取图片名称
		String fileName = uploadFile.getOriginalFilename();
		fileName = fileName.toLowerCase();
		//2.校验是否为图片的类型   使用正则表达式判断字符串
		if(!fileName.matches("^.+\\.(jpg|png|gif)$")) {
			imageVO.setError(1);
			return imageVO;
		}
		//3.校验是否为恶意程序
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			if(width==0 || height == 0) {
				imageVO.setError(1);
				return imageVO;
				
			}
			//4.分文件保存 按时间存储 yyyy/MM/dd
			String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
			
			//5.准备文件夹
			String localDir = localDirPath + dateDir;
			File dirFile = new File(localDir);
			if(!dirFile.exists()) {
				dirFile.mkdirs();
			}
			//6.使用UUID定义文件名称uuid.jpg
			String uuid = UUID.randomUUID().toString().replace("-", "");
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			//拼接新的文件名称
			String realLocalPath = localDir + "/" + uuid + fileType;
			
			//7.完成文件上传
			uploadFile.transferTo(new File(realLocalPath));
			
			String realUrl = urlPath + dateDir + "/" + uuid + fileType;
			
			imageVO.setError(0).setHeight(height).setWidth(width).setUrl(realUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
			imageVO.setError(1);
			return imageVO;
		}
		
		return imageVO;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
