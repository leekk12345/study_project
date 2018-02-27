package org.zerock.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageMaker;
import org.zerock.domain.SearchCriteria;
import org.zerock.service.BoardService;

@Controller //컨트롤러 선언
@RequestMapping("/board/*") //Board의 모든 공통 URI를 '/board/'로 인식되게 함
public class BoardController {

  private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

  @Inject
  private BoardService service;

  //http://localhost:8181/board/register
  //value: 특정 URI를 의미, method: GET/ POST 등의 전송방식 결정
  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public void registerGET(BoardVO board, Model model) throws Exception {

    logger.info("register get ...........");
  }

//   @RequestMapping(value = "/register", method = RequestMethod.POST)
//   public String registPOST(BoardVO board, Model model) throws Exception {
//  
//   logger.info("regist post ...........");
//   logger.info(board.toString());
//  
//   service.regist(board);
//  
//   model.addAttribute("msg", "SUCCESS"); //URI에 ?msg=success와 같이 설정됨
//										   //http://localhost:8181/board/listAll?result=success
//  
//   //return "/board/success"; //이처럼 redirect 안하면 사용자가 새로고침 할때마다 새 게시글이 등록되어 도배되기에 
//   							  //하기와 같이 redirect하여 해당 문제를 해결해야함
//   return "redirect:/board/listAll";
//   }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String registPOST(BoardVO board, RedirectAttributes rttr) throws Exception {

    logger.info("regist post ...........");
    logger.info(board.toString());

    service.regist(board);

    rttr.addFlashAttribute("msg", "SUCCESS"); //리다이렉트 시점에 한 번만 사용되는 데이터를 브라우저까지 전송하지만,
                                              //URI 상에는 보이지 않는 숨겨진 데이터 형태로 전달함
    										  //http://localhost:8181/board/listAll

    return "redirect:/board/listAll";
  }
  
  //http://localhost:8181/board/listAll?result=success
  //http://localhost:8181/board/listAll
  @RequestMapping(value = "/listAll", method = RequestMethod.GET)
  public void listAll(Model model) throws Exception {

    logger.info("show all list......................");
    model.addAttribute("list", service.listAll()) ;
  }

  //http://localhost:8181/board/read?bno=1
  //@RequestParam: Servlet에서 request.getParameter()와 동일
  @RequestMapping(value = "/read", method = RequestMethod.GET)
  public void read(@RequestParam("bno") int bno, Model model) throws Exception {

    model.addAttribute(service.read(bno)); //별도의 이름 지정 없이 addAttribute 수행 시,
    									   //현재 service.read의 반환값이 BoardVO객체이기에
    									   //앞글자만 소문자로 바꾼 boardVO로 jsp에서 접근 가능함
  }

  @RequestMapping(value = "/remove", method = RequestMethod.POST)
  public String remove(@RequestParam("bno") int bno, RedirectAttributes rttr) throws Exception {

    service.remove(bno);

    rttr.addFlashAttribute("msg", "SUCCESS");

    return "redirect:/board/listAll";
  }

  @RequestMapping(value = "/modify", method = RequestMethod.GET)
  public void modifyGET(int bno, Model model) throws Exception {

    model.addAttribute(service.read(bno));
  }

  @RequestMapping(value = "/modify", method = RequestMethod.POST)
  public String modifyPOST(BoardVO board, RedirectAttributes rttr) throws Exception {

    logger.info("mod post............");

    service.modify(board);
    rttr.addFlashAttribute("msg", "SUCCESS"); //동일한 URI를 새로고침 시, 경고창이 여러번 노출되지 않음

    return "redirect:/board/listAll";
  }

  @RequestMapping(value = "/listCri", method = RequestMethod.GET)
  public void listAll(Criteria cri, Model model) throws Exception {

    logger.info("show list Page with Criteria......................");

    model.addAttribute("list", service.listCriteria(cri));
  }

  //http://localhost:8181/board/listPage?page=6
  @RequestMapping(value = "/listPage", method = RequestMethod.GET)
  public void listPage(@ModelAttribute("cri") Criteria cri, Model model) throws Exception {

    logger.info(cri.toString());

    model.addAttribute("list", service.listCriteria(cri)); //목록 데이터를 Model에 저장
    PageMaker pageMaker = new PageMaker(); //PageMaker를 구성해서 Model에 담는 작업
    pageMaker.setCri(cri);
    //pageMaker.setTotalCount(131); //131로 totalCount를 설정했기에 
    							  //실제 페이지는 14페이지까지만 출력되야하고 
    						      //11페이지부터 이전으로 갈 수 있는 링크를 제공해야 함

    pageMaker.setTotalCount(service.listCountCriteria(cri)); //130line과 달리 제대로 된 totalCount가 나옴

    model.addAttribute("pageMaker", pageMaker);
  }

  //http://localhost:8181/board/readPage?bno=xxx&page=x&perPageNum=xxx
  @RequestMapping(value = "/readPage", method = RequestMethod.GET)
  public void read(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model) throws Exception {
	//page, perPageNum 파라미터의 경우 Criteria 타입의 객체로 처리
	//Criteria는 페이징 처리를 위해서 존재하는 객체이므로, 의미없는 bno를 유지할 필요가 없기에 추가적인 속성으로 사용 안함
    model.addAttribute(service.read(bno));
  }

  @RequestMapping(value = "/removePage", method = RequestMethod.POST)
  public String remove(@RequestParam("bno") int bno, Criteria cri, RedirectAttributes rttr) throws Exception {

    service.remove(bno);

    rttr.addAttribute("page", cri.getPage());
    rttr.addAttribute("perPageNum", cri.getPerPageNum());
    rttr.addFlashAttribute("msg", "SUCCESS"); //삭제 결과는 임시 데이터이기에 addFlashAttribute로 처리함

    return "redirect:/board/listPage"; //목록 페이지로 리다이렉트
  }

  @RequestMapping(value = "/modifyPage", method = RequestMethod.GET)
  public void modifyPagingGET(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model)
      throws Exception {
	  model.addAttribute(service.read(bno));
  }

  @RequestMapping(value = "/modifyPage", method = RequestMethod.POST)
  public String modifyPagingPOST(BoardVO board, SearchCriteria cri, RedirectAttributes rttr) throws Exception {

    logger.info(cri.toString());
    service.modify(board);

    rttr.addAttribute("page", cri.getPage());
    rttr.addAttribute("perPageNum", cri.getPerPageNum());
    rttr.addFlashAttribute("msg", "SUCCESS");

    logger.info(rttr.toString());

    return "redirect:/board/list";
  }
}