package com.project.ecofurniture.controller.admin.code;

import com.project.ecofurniture.model.dto.admin.CodeDto;
import com.project.ecofurniture.model.entity.admin.code.Code;
import com.project.ecofurniture.service.admin.code.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * packageName : com.project.ecofurniture.controller.admin.code
 * fileName : CodeController
 * author : GB_Jo
 * date : 2023-11-22
 * description :
 * 요약 :
 * <p>
 * ===========================================
 */
@RestController
@Slf4j
@RequestMapping("/api/admin")
public class CodeController {
    @Autowired
    CodeService codeService;

    /**
     * like 검색 : 페이징
     */
    //    전체 조회 + like 검색
    @GetMapping("/code")
    public ResponseEntity<Object> findAllByCodeNameContaining(
            @RequestParam(defaultValue = "") String codeName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
//            페이지 변수 저장 (page:현재페이지번호, size:1페이지당개수)
//            함수 매개변수 : Pageable (위의 값을 넣기)
//        사용법 : Pageable pageable = PageRequest.of(현재페이지번호, 1페이지당개수);
            Pageable pageable = PageRequest.of(page, size);

            Page<CodeDto> codeDtoPage
                    = codeService.selectByCodeNameContaining(codeName, pageable);

//          리액트 전송 : 배열 , 페이징정보 [자료구조 : Map<키이름, 값>]
            Map<String, Object> response = new HashMap<>();
            response.put("code", codeDtoPage.getContent()); // 조인배열
            response.put("currentPage", codeDtoPage.getNumber()); // 현재페이지번호
            response.put("totalItems", codeDtoPage.getTotalElements()); // 총건수(개수)
            response.put("totalPages", codeDtoPage.getTotalPages()); // 총페이지수

            if (codeDtoPage.isEmpty() == false) {
//                성공
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/code")
    public ResponseEntity<Object> create(@RequestBody Code code) {

        try {
            Code code2 = codeService.save(code);

            return new ResponseEntity<>(code2, HttpStatus.OK);
        } catch (Exception e) {
//            DB 에러가 났을경우 : INTERNAL_SERVER_ERROR 프론트엔드로 전송
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/code/{codeId}")
    public ResponseEntity<Object> update(@PathVariable int codeId,
                                         @RequestBody Code code) {

        try {
            Code code2 = codeService.save(code); // db 수정

            return new ResponseEntity<>(code2, HttpStatus.OK);
        } catch (Exception e) {
//            DB 에러가 났을경우 : INTERNAL_SERVER_ERROR 프론트엔드로 전송
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/code/{codeId}")
    public ResponseEntity<Object> findById(@PathVariable int codeId) {

        try {
            Optional<Code> optionalCode = codeService.findById(codeId);

            if (optionalCode.isPresent()) {
//                성공
                return new ResponseEntity<>(optionalCode.get(), HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
//            서버 에러
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전체 검색 : 페이징 없음 + 조인
     */
    @GetMapping("/code/all")
    public ResponseEntity<Object> selectAllNoPage() {
        try {
            List<CodeDto> list
                    = codeService.selectAllNoPage();
            if (list.isEmpty() == false) {
//                성공
                return new ResponseEntity<>(list, HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
