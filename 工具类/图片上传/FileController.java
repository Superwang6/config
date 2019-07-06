package com.jt.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;

@Controller
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws Exception {
		//获取input的名称
		//String inputName = fileImage.getName();
		
		String fileName = fileImage.getOriginalFilename();
		File fileDir = new File("E:/image");
		if(!fileDir.exists()) {
			fileDir.mkdirs();
		}
		fileImage.transferTo(new File("E:/image/"+fileName));
		return "redirect:/file.jsp";
	}
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public ImageVO uploadFile(MultipartFile uploadFile) {
		return fileService.updateFile(uploadFile);
	}
	
	
	
	
	
	
	
	
}
