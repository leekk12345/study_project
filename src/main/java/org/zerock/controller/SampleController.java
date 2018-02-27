package org.zerock.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;


@RestController 
@RequestMapping("/sample")
public class SampleController {

	//http://localhost:8181/sample/hello
  @RequestMapping("/hello")
  public String sayHello() {
    return "Hello World "; 
  }

  //http://localhost:8181/sample/sendVO
  @RequestMapping("/sendVO")
  public SampleVO sendVO() {

    SampleVO vo = new SampleVO();
    vo.setFirstName("SeHee");
    vo.setLastName("Ahn");
    vo.setMno(123);

    return vo; 
  }

  //http://localhost:8181/sample/sendList
  @RequestMapping("/sendList")
  public List<SampleVO> sendList() {

    List<SampleVO> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      SampleVO vo = new SampleVO();
      vo.setFirstName("Lee");
      vo.setLastName("SeungHyung");
      vo.setMno(i);
      list.add(vo);
    }
    
    return list;
  }

  //http://localhost:8181/sample/sendMap
  @RequestMapping("/sendMap")
  public Map<Integer, SampleVO> sendMap() {

    Map<Integer, SampleVO> map = new HashMap<>();

    for (int i = 0; i < 10; i++) {
      SampleVO vo = new SampleVO();
      vo.setFirstName("SeHee");
      vo.setLastName("Ahn");
      vo.setMno(i);
      map.put(i, vo); //키(숫자), 값(SampleVO타입의 객체)
    }
    return map;
  }

  //http://localhost:8181/sample/sendErrorAuth
  @RequestMapping("/sendErrorAuth")
  public ResponseEntity<Void> sendListAuth() {

    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //HTTP Status Code가 400
    //ResponseEntity 타입은 개발자가 데이터+HTTP 상태코드를 직접 제어 가능한 클래스임
  }

  //http://localhost:8181/sample/sendErrorNot
  @RequestMapping("/sendErrorNot")
  public ResponseEntity<List<SampleVO>> sendListNot() {

    List<SampleVO> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      SampleVO vo = new SampleVO();
      vo.setFirstName("SeHee");
      vo.setLastName("Ahn");
      vo.setMno(i);
      list.add(vo);
    }

    return new ResponseEntity<>(list, HttpStatus.NOT_FOUND); //list 데이터, 404 HTTP Status Code
    
  }

}