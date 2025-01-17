package org.example.javaspringbootjpa1.repository;

import lombok.extern.log4j.Log4j2;
import org.example.javaspringbootjpa1.domain.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert(){
        for(int i = 1; i <= 100; i++){
            Board board = Board.builder()
                    .title("title....")
                    .content("content..")
                    .writer("user" + (i % 10))
                    .build();
            Board result = boardRepository.save(board);
            log.info("BNO: {}", result.getBno());
        }
    }
    @Test
    public void testSelect(){
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        log.info("findById bno 값 : {}", result);
        Board board = result.orElseThrow();
        log.info(board);
    }
    @Test
    public void testUpdate(){
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        board.change("update....title 100", "update content 100","update writet 1");
        boardRepository.save(board);
    }
    @Test
    public void testUpdate2(){
        Long bno = 111L;

        Board board = Board.builder()
                .bno(bno)
                .title("title..???")
                .content("content???")
                .writer("user1") // writer 값을 설정해줍니다.
                .build();
        boardRepository.save(board);
    }
    @Test
    public void testUpdate3(){
        // 없는 bno 지정한경우
        Long bno = 1000L;
        Board board = Board.builder()
                .bno(bno)
                .title("title..???")
                .content("content???")
                .writer("user..update")
                .build();
        boardRepository.save(board);
    }
    @Test
    public void testDlete(){
        Long bno = 2L;

        boardRepository.deleteById(bno);
    }
    @Test
    public void testPaging(){
        // 1 page order bt bno desc
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending()); //pageable이 import 이상한거 하면 빨개짐 자동완성이라도 빨개지면 항상의심하자
        // 첫페이지에 10개의 항목을 가져오도록 설정한다. bno 필드를 기준으로 내림차순으로 정렬

        Page<Board> result = boardRepository.findAll(pageable); // Board 엔티티의 페이징된 결과를 모두 가져온다.
        log.info("total count: " + result.getTotalElements());
        log.info("total page: " + result.getTotalPages());
        log.info("total number: " + result.getNumber());
        log.info("total size: " + result.getSize());
        //prev next
        log.info("{}: {}", result.hasPrevious(), result.hasNext());

        List<Board> boardList = result.getContent();

        boardList.forEach(board -> log.info(board));
    }
    @Test
    public void testSearch1(){
        // 2 page order by bno desc
        Pageable pageable = PageRequest.of(1,10,Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }
    @Test
    public void testSearchAll(){
        String[] types = {"t", "c", "w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        //total pages
        log.info(result.getTotalPages());

        // page size
        log.info(result.getSize());

        //pageNumber
        log.info(result.getNumber());

        //prev next
        log.info(result.hasPrevious() + ": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

}