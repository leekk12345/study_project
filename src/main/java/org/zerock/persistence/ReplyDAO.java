package org.zerock.persistence;

import java.util.List;

import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;



public interface ReplyDAO {

  public List<ReplyVO> list(Integer bno) throws Exception;

  public void create(ReplyVO vo) throws Exception;

  public void update(ReplyVO vo) throws Exception;

  public void delete(Integer rno) throws Exception;

  public List<ReplyVO> listPage(Integer bno, Criteria cri) throws Exception; 
  //게시물 번호에 맞는 데이터 검색 위한 bno, 페이징 처리에 사용하는 cri를 매개변수로 받음

  public int count(Integer bno) throws Exception;
  //페이징 처리 위해 해당 게시물의 댓글 수를 카운트

  public int getBno(Integer rno) throws Exception;

}