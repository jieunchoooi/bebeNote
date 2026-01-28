package com.itwillbs.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.domain.ChildVaccineVO;
import com.itwillbs.domain.ChildrenVO;
import com.itwillbs.entity.ChildVaccine;
import com.itwillbs.entity.Children;
import com.itwillbs.service.ChildrenService;
import com.itwillbs.service.MainService;
import com.itwillbs.service.MyPageService;
import com.itwillbs.service.PaymentService;

import groovy.util.logging.Log;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/myPage/*")
public class MyPageController {
   
   private final MyPageService myPageService;
   private final ChildrenService childrenService;
   private final PaymentService paymentService; 
   private final MainService mainService; 
   
   @Value("${file.upload.path:${user.dir}/src/main/resources/static/img/child/}")
   private String uploadPath; 
   
   @GetMapping("/info")
   public String info(HttpSession session, Model model) {
      System.out.println("MyPageController info()");
      
      String userId = SecurityContextHolder.getContext().getAuthentication().getName();
      
      List<Children> children = childrenService.findAllByUserId(userId);
      if (children == null) {
            model.addAttribute("noChild", true);
            return "myPage/info";
      }
      
      Long childId = children.get(0).getChild_id();
      List<ChildVaccine> records = myPageService.findChildRecords(childId);
       
      // vaccine_id + "_" + dose_id를 key로 하는 Map 생성
      Map<String, ChildVaccine> vaccineRecords = new HashMap<>();
      for (ChildVaccine record : records) {
          String key = record.getVaccine_id() + "_" + record.getDose_id();
          vaccineRecords.put(key, record);
      }
      
      List<ChildrenVO> childInfo = mainService.ChildInformation(userId);
      
      model.addAttribute("childInfo", childInfo);
      model.addAttribute("children", children);
      model.addAttribute("vaccines", myPageService.findAllVaccines());
      model.addAttribute("doses", myPageService.findAllDoses());
      model.addAttribute("vaccineRecords", vaccineRecords);
      
         return "myPage/info";
   }
   
   @PostMapping("/saveVaccine")
   @ResponseBody
   public Map<String, Object> saveVaccine(@RequestBody ChildVaccineVO vo) {
      System.out.println("MyPageController saveVaccine()");
      
       Long childVaccineId = myPageService.saveVaccine(vo);
       Map<String, Object> result = new HashMap<>();
       result.put("childVaccineId", childVaccineId);
       
       return result;
       
   }
   
   @PostMapping("/deleteVaccine")
   @ResponseBody
   public void deleteVaccine(@RequestBody ChildVaccineVO vo) {
      System.out.println("MyPageController deleteVaccine()");
      
       myPageService.deleteVaccine(vo.getChild_vaccine_id());
       
   }
   
   @GetMapping("/myHospital")
   public String myHospital() {
      System.out.println("MyPageController myHospital()");
         return "myPage/myHospital";
   }
   
   @GetMapping("/payHistory")
   public String payHistory() {
      System.out.println("MyPageController payHistory()");
         return "myPage/payHistory";
   }
   
   @GetMapping("/payControl")
   public String payControl(Model model) {
      System.out.println("MyPageController payControl()");
      
      // 로그인한 사용자 ID 가져오기
      String user_id = SecurityContextHolder.getContext().getAuthentication().getName();
      
      // 베베페이 잔액 조회
      int balance = paymentService.getBebepayBalance(user_id);
      model.addAttribute("balance", balance);
         return "myPage/payControl";
   }

   @PostMapping("/uploadProfileImage")
   @ResponseBody
   public Map<String, Object> uploadProfileImage(@RequestParam("profileImage") MultipartFile file,
                                                @RequestParam("childId") Long childId) {
       System.out.println("MyPageController uploadProfileImage()");
       
       Map<String, Object> result = new HashMap<>();
       
       try {
           // 업로드 디렉토리 생성
           File dir = new File(uploadPath);
           
           if(!dir.exists()) {
               dir.mkdirs();
           }
           
           // 파일명 생성
           String originalFilename = file.getOriginalFilename();
           String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
           String savedFilename = "profile_" + childId + "_" + System.currentTimeMillis() + extension;

           // 파일 저장
           File savedFile = new File(uploadPath + savedFilename);
           file.transferTo(savedFile);
           
           // DB에 파일 경로 저장
           String filePath = "/img/child/" + savedFilename;
           childrenService.updateChildImg(childId, filePath);
           
           result.put("success", true);
           result.put("filePath", filePath);
           
       } catch (Exception e) {
           e.printStackTrace();
           result.put("success", false);
           result.put("message", "업로드 실패: " + e.getMessage());
       }
       
       return result;
   }
   
}
