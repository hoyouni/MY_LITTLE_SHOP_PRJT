package com.example.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 상품 관련 Controller
 * 매 번 다른 상품을 보여줘야 하는거 아닌가?
 *  >> 그럴 때는 서버 또는 데이터베이스에서 데이터를 가져와서 랜더링 하면 된다.
 *     첫 번째 배워볼 것은 템플릿 엔진 외부 라이브러리를 사용한 데이터 랜더링
 *     두 번째 배워볼 것은 데이터베이스 연결을 통한 데이터 랜더링이다.
 *     1. 템플릿 엔진을 사용한 데이터 랜더링
 *        1) Thymeleaf 템플릿 엔진 사용을 위해 build.gradle 파일에 타임리프 라이브러리를 추가하고 서버 재구동 한다. (dependencies 내부에)
 *        2) templates 폴더 내부에 html 파일을 생성한다.
 *        3) 컨트롤러단에서 Model 클래스를 import 해주고 사용할 api 함수를 만들고 함수 파라미터로 model 클래스를 넣어준다.
 *        4) api 함수 내부에서 model.addAttribute 함수를 사용하여 key value 구조로 데이터를 보내준다. (productList 함수 참고)
 *        5) 리턴 받은 html 파일 내부에서 th 어쩌고를 사용해서 컨트롤러에서 리턴 받은 데이터를 사용하면 된다.
 *     2. 데이터베이스 연결을 통한 데이터 랜더링
 *      >> 대량 데이터를 가지고 실제 개발을 위해서는 데이터베이스를 통한 데이터 요청 응답이 있어야 함.
 *         여기서는 관계형 데이터베이스인 MySQL 을 사용할 것임
 *         직접 하드디스크에 설치해도 되지만 클라우드 서버를 활용하기 위해 Azure 를 사용하여 호스팅 받도록 함.
 *         참고로 AWS 써도 되는데 월 3달러 정도 청구될 수 있어서 다른 설정들이 필요한데 복잡하기 때문에 생략.
 *         1) portal.azure.com 접속
 *         1) Azure 가입 후 카드 등록 ( 1년 무료 서비스 제공, 30일 체험 . 종량제 요금제로 업그레이드 해야 남은 기간 무료 사용 가능하니 참고)
 *         2) 홈화면에서 MySQL 유동서버 검색 후 진입
 *         3) 서버 구성하기 (지역은 Korea Central, 워크로드 유형은 개발 또는 취미 프로젝트용)
 *            참고로 컴퓨팅/스토리지 선택하는 곳에서 미리 프로비전된 iops + 최소사양으로 선택해야 추가비용 청구되지 않음
 *         4) DB 접속 아이디 / 패스워드 발번
 *         5) 접속 가능한 ip 주소 설정이 가능한데 현재는 모든 ip 에서 접근 가능하도록 설정함 (여러 환경에서 접근할 수도 있어서)
 *            추 후 변경 필요 시 내 ip 주소로 설정
 *         6) 배포 진행 하여 배포가 완료되면 내 서버에서 서버 매개 변수 설정이 가능한데
 *            require_secure_transport 설정을 OFF 로 설정 (로컬 개발이라 ssl 인증 굳이 사용하지 않기 때문에)
 *         7) 인텔리제이 확장 플러그인 db browser 설치 / dbeaver 설치 후 설정해주는데 서버 호스트는 azure 에서
 *            발급 받은 서버이름 복붙 해주면 되고, username 과 패스워드는 서버 구성할 때 했던 아이디 비번 설정해주면 됨
 *
 *       >> 여기까지 데이터베이스 설정을 마쳤으면 이제 데이터베이스 생성하고 개발해보자.
 *
 *         8) dbeaver 를 사용하든 인텔리제이 내장 db browser 를 이용하든 해서 명령어를 입력.
 *            create database 데이터베이스명
 *         9) 평상시 했던 sql 구문 입력하여 데이터 입출력 하는 것과는 다르게 서버에서 코드로 데이터를 입출력 할 것이기 때문에 라이브러리 설치
 *         10) build.gradle 파일에서 JPA 와 MySQL 접속용 라이브러리 설치를 위한 dependencies 추가
 *             >> JPA 가 뭐지?
 *                >> 기존에 관계형 데이터베이스에는 sql 이라는 언어를 사용하여 데이터 입출력을 해야하는데 그걸 최소화 해서 개발하려고
 *                   ORM 이라는 라이브러리를 설치해서 쓰는 경우가 많음.
 *                   ORM 을 설치하면 SQL 언어가 아니라 자바 코드로 입출력이 가능한데 테이블들을 클래스로 관리해서 타입 체크도 되고
 *                   코드 재사용성도 높은 장점들이 있음.
 *                   이번에 설치하는 라이브러리를 Hibernate 라고도 부르는데
 *                   JPA 는 자바에서의 ORM 표준 문법이라고 보면 되고
 *                   JPA 문법을 개발자들이 편하게 쓰게끔 만들어준게 Hibernate 라는 라이브러리라고 함.
 *                   JPA 를 설치하면 자동으로 Hibernate 가 함께 설치 되기 때문에 JPA 와 Hibernate 를 혼용해서 많이 부른다.
 *             >> build.gradle 파일 보면 runtimeOnly 'com.mysql:mysql-connector-j' 이런 의존성을 주입해놨는데
 *                얘는 왜 runtimeOnly 임?
 *                >> runtimeOnly 는 컴파일할 때는 필요없는 라이브러리이기 때문에 컴파일할 땐 쓰지 말자고 하는 것임.
 *                   그냥 implementation 을 사용해도 되지만 컴파일 되는 시간을 절약하기 위해서 해당 구문을 작성한 것임.
 *                   참고로 서버 구동 시 필요없는 라이브러리는 compileOnly 라는 구문을 작성하기도 함.
 *         11) application.properties 파일에서 db 접속을 위한 설정 진행 (자세한 설정 내용은 직접 찾아보자~~)
 *         12) 서버 구동 해서 HikariPool 연결까지 잘 되는지 확인할 것
 *             만약 안되면 properties 설정 가서 db 접속 주소 잘 적었는지, 아이디 비번 잘 적었는지 확인할 것
 *             간혹 db 접속 주소 작성 시 포트번호(3306) 까지 작성해야 하는 경우가 있긴 하니 참고
 *
 *       >> 여기까지가 호스팅 받고 데이터베이스 만들어서 설정 후 연결 테스트까지 한 것이고 이제 진짜 개발 고고
 *
 */
@Controller
public class ItemController {

    @GetMapping("/productList")
    String productList(Model model) {
        model.addAttribute("prodCd","A10000");
        return "productList.html";
    }

}



















