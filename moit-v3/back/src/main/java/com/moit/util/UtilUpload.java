package com.moit.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Component //내가 만든 부품이야
public class UtilUpload {
	@Value("${resource.path}") private String resourcePath;// C:/upload -> application.properties

	
	// 방식 A: 경로를 지정 안 하면 기본 경로(defaultPath) 사용
    public String fileUpload(MultipartFile file) throws IOException {	
        return fileUpload(file, resourcePath); 
    }
    
    // 방식 B: 경로를 지정하면 해당 경로 사용 (핵심 로직은 여기서 처리)
    public String fileUpload(MultipartFile file, String subPath) throws IOException {

        // C:/upload/meetup
        File directory = new File(resourcePath, subPath);

        // 폴더 없으면 생성
        if (!directory.exists()) {
            boolean created = directory.mkdirs();

//            System.out.println("🔥 폴더 생성 결과 = " + created);
        }

//        System.out.println("🔥 resourcePath = " + resourcePath);
//        System.out.println("🔥 저장 폴더 = " + directory.getAbsolutePath());

        UUID uid = UUID.randomUUID();

        String save = uid + "_" + file.getOriginalFilename();

        File target = new File(directory, save);

//        System.out.println("🔥 최종 파일 경로 = " + target.getAbsolutePath());
//        System.out.println("🔥 파일명 = " + file.getOriginalFilename());
//        System.out.println("🔥 파일 크기 = " + file.getSize());

        FileCopyUtils.copy(file.getBytes(), target);

//        System.out.println("🔥 파일 존재 여부 = " + target.exists());

        return save;
    }
    
//	public String fileUpload(MultipartFile file) throws IOException {	
//		//1. 파일 이름 중복안되게
//		UUID uid = UUID.randomUUID();
//		String save = uid.toString() + "_" + file.getOriginalFilename();
//		//2. 파일업로드
//		File target = new File(resourcePath , save);
//		FileCopyUtils.copy(file.getBytes(), target);
//		return save;		
//	}
	
	//1. 업로드 : 팀미션 : 확장자, 용량 사이즈 고려
	//2. 팀미션 : 글삭제시 파일도 같이 삭제
}
