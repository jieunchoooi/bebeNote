package com.itwillbs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.domain.ChildVaccineVO;
import com.itwillbs.entity.ChildVaccine;
import com.itwillbs.entity.Children;
import com.itwillbs.service.ChildrenService;
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
   

}
