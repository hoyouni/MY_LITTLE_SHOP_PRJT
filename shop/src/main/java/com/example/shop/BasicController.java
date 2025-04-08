package com.example.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.ZonedDateTime;

/**
 * 서버가 뭐임?
 *  >> 유저의 요청에 따라 응답해주는 프로그램이라고 보면 됨.
 * 웹서버가 뭐임?
 *  >> 유저가 웹페이지 달라고 하면 응답해서 웹페이지 보내주는 프로그램, 서버일 뿐 어렵게 생각할 필요 없음.
 * 서버기능은 어떻게 만드는데?
 *  >> 일단 Controller 를 만들어야 기본적인 서버 기능을 구현할 수 있음.
 *     클래스를 만들고 Controller 라는 어노테이션을 붙여서 얘는 서버 기능 구현하는 놈이다, 라고 명시 해주면 됨.
 *     GetMapping , PostMapping 등이 있겠지만 일단 GetMapping 어노테이션을 작성하고 경로를 파라미터로 넣어주면
 *     유저가 특정 경로 요청 시 컨트롤러에서 매핑된 url 을 찾아가게 됨.
 *     ResponseBody 어노테이션을 작성하게 되면 리턴값 오른쪽 데이터를 그대로 요청자에게 응답하게 됨.
 * ResponseBody 를 명시하지 않으면 어떻게 되는데?
 *  >> 그러면 유저에게 return 오른쪽에 명시해둔 경로의 파일을 전송하여 응답하게 됨.
 *     파일은 resources / static 폴더가 기본 경로임
 * html 파일을 만들어서 보내주고 싶은데 html 파일 만들고 기본 양식 채우기 귀찮은데 어떡하죠?
 *  >> 그러면 느낌표 + tab 키 누르면 알아서 기본 양식을 자동완성 해줌 , 다른 ide 들도 되는건지 인텔리제이만 되는건지는 확인해봐야함.
 *
 * 아래에서 실습을 해보자고.
 */
@Controller
public class BasicController {

    @GetMapping("/")
    @ResponseBody
    String mainPage() {
        return "main page !";
    }

    @GetMapping("/index")
    @ResponseBody
    String indexPage() {
        return "index page !";
    }

    @GetMapping("/myPageTest")
    @ResponseBody
    String myPage() {
        return "my page !";
    }

    @GetMapping("/oldHtmlPage")
    @ResponseBody
    String oldHtmlPage() {
        return "<h4>oldHtmlPage</h4>";
    }

    @GetMapping("/newHtmlPage")
    String newHtmlPage() {
        return "newHtmlPage.html";
    }

    /**
     * 신기한게 time 클래스의 ZonedDateTime 클래스가 있는데
     * 이 클래스 사용하면 알아서 현재 날짜 및 시간을 출력해줌.
     * 다만 다른 형식의 포맷이 필요하면 포맷팅 변환을 해주긴 해야할 듯.
     * @return 현재 날짜 및 시간
     */
    @GetMapping("/date")
    @ResponseBody
    String date() {
        return ZonedDateTime.now().toString();
    }


}
