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

@Controller //��Ʈ�ѷ� ����
@RequestMapping("/board/*") //Board�� ��� ���� URI�� '/board/'�� �νĵǰ� ��
public class BoardController {

  private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

  @Inject
  private BoardService service;

  //http://localhost:8181/board/register
  //value: Ư�� URI�� �ǹ�, method: GET/ POST ���� ���۹�� ����
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
//   model.addAttribute("msg", "SUCCESS"); //URI�� ?msg=success�� ���� ������
//										   //http://localhost:8181/board/listAll?result=success
//  
//   //return "/board/success"; //��ó�� redirect ���ϸ� ����ڰ� ���ΰ�ħ �Ҷ����� �� �Խñ��� ��ϵǾ� ����Ǳ⿡ 
//   							  //�ϱ�� ���� redirect�Ͽ� �ش� ������ �ذ��ؾ���
//   return "redirect:/board/listAll";
//   }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String registPOST(BoardVO board, RedirectAttributes rttr) throws Exception {

    logger.info("regist post ...........");
    logger.info(board.toString());

    service.regist(board);

    rttr.addFlashAttribute("msg", "SUCCESS"); //�����̷�Ʈ ������ �� ���� ���Ǵ� �����͸� ���������� ����������,
                                              //URI �󿡴� ������ �ʴ� ������ ������ ���·� ������
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
  //@RequestParam: Servlet���� request.getParameter()�� ����
  @RequestMapping(value = "/read", method = RequestMethod.GET)
  public void read(@RequestParam("bno") int bno, Model model) throws Exception {

    model.addAttribute(service.read(bno)); //������ �̸� ���� ���� addAttribute ���� ��,
    									   //���� service.read�� ��ȯ���� BoardVO��ü�̱⿡
    									   //�ձ��ڸ� �ҹ��ڷ� �ٲ� boardVO�� jsp���� ���� ������
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
    rttr.addFlashAttribute("msg", "SUCCESS"); //������ URI�� ���ΰ�ħ ��, ���â�� ������ ������� ����

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

    model.addAttribute("list", service.listCriteria(cri)); //��� �����͸� Model�� ����
    PageMaker pageMaker = new PageMaker(); //PageMaker�� �����ؼ� Model�� ��� �۾�
    pageMaker.setCri(cri);
    //pageMaker.setTotalCount(131); //131�� totalCount�� �����߱⿡ 
    							  //���� �������� 14������������ ��µǾ��ϰ� 
    						      //11���������� �������� �� �� �ִ� ��ũ�� �����ؾ� ��

    pageMaker.setTotalCount(service.listCountCriteria(cri)); //130line�� �޸� ����� �� totalCount�� ����

    model.addAttribute("pageMaker", pageMaker);
  }

  //http://localhost:8181/board/readPage?bno=xxx&page=x&perPageNum=xxx
  @RequestMapping(value = "/readPage", method = RequestMethod.GET)
  public void read(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model) throws Exception {
	//page, perPageNum �Ķ������ ��� Criteria Ÿ���� ��ü�� ó��
	//Criteria�� ����¡ ó���� ���ؼ� �����ϴ� ��ü�̹Ƿ�, �ǹ̾��� bno�� ������ �ʿ䰡 ���⿡ �߰����� �Ӽ����� ��� ����
    model.addAttribute(service.read(bno));
  }

  @RequestMapping(value = "/removePage", method = RequestMethod.POST)
  public String remove(@RequestParam("bno") int bno, Criteria cri, RedirectAttributes rttr) throws Exception {

    service.remove(bno);

    rttr.addAttribute("page", cri.getPage());
    rttr.addAttribute("perPageNum", cri.getPerPageNum());
    rttr.addFlashAttribute("msg", "SUCCESS"); //���� ����� �ӽ� �������̱⿡ addFlashAttribute�� ó����

    return "redirect:/board/listPage"; //��� �������� �����̷�Ʈ
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