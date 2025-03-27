package com.example.shop.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 공지사항 (Notice) 관련 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeRepository noticeRepo;

    @GetMapping("/noticeList")
    String noticeList(Model model) {
        List<Notice> list = noticeRepo.findAll();
        model.addAttribute("noticeList", list);

        return "notice/noticeList.html";
    }

}



















