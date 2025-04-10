package com.example.shop.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
 *         13) 상품 / 공지사항 테이블 (엔티티) 만든 후 데이터 임의로 넣고 lombok 라이브러리 설치 (build.gradle 내 dependencies 추가)
 *             >> lombok 이 뭔데 ? 왜 설치하냐 ?
 *                >> lombok 은 필요한 코드를 자동으로 생성해주어서 코드 가독성을 높여주고 양을 줄여주는 편리한 라이브러리로 설치 권장
 *                   build.gradle 설정이 완료되면 추가로 lombok 플러그인 설치 및 Setting 메뉴 내 annotation processor 설정 필요
 *
 *         14) 데이터 출력 해보자
 *             >> 데이터 출력을 위해서는 3가지 단계가 있음
 *                1단계 : repository 라는 인터페이스를 만들어주고
 *                2단계 : DB입출력을 원하는 클래스에 만들어놓은 repository 인터페이스를 등록해주고
 *                3단계 : DB입출력 문법을 사용하면 됨. 아래 코드 작성 해놓은거 있으니 참고 (ProductRepository 인터페이스도 참고)
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    /**
     * lombok에서 제공하는 @RequiredArgsConstructor 어노테이션에 의해 자동으로 ProductRepository DI 주입
     * 이 때 private final 을 지양하는 이유는
     * 1. 한 번 초기화 된 repository 변수가 변경되지 않도록 하기 위함
     * 2. 실행 중에 객체가 변하는 것을 막아 오류를 방지하기 위함
     * 3. 필수적으로 사용하는 매개변수 없이는 인스턴스를 만들 수 없기에 반드시 객체의 주입이 필요한 경우에 강제하기 위함
     */
    private final ProductRepository prdRepo;        // Product 엔티티 사용을 위한 인터페이스

    /**
     * 서비스클래스 DI 주입
     */
    private final ProductService productService;    // 비즈니스 로직 활용을 위한 Servie 클래스

    /**
     * 파일 업로드를 위한 aws s3 DI 주입
     */
    private final S3Service s3Service;

    /* 만약 lombok 을 안 쓴다고 하면?
     @AutoWired
     public ItemController(ProductRepository prdRepo) {
        this.prdRepo = prdRepo;
     }
     이런 식으로 생성자에 인스턴스 할당해도 됨
    */

    /**
     * 상품 리스트 화면 호출
     * @param model
     * @return productList page
     */
    @GetMapping("/productList")
    String productList(Model model) {
        /**
         * 템플릿 엔진(Thymeleaf) + 데이터베이스 연동을 통한 데이터 응답
         * 테이블 내 모든 데이터 리스트 출력 시 findAll 함수 사용
         * findAll 함수로 호출하여 list 자료구조에 담고 해당 리스트를 모델의 속성에 key-value 로 담아서
         * html 페이지로 응답 해주면 됨
         */

        // 페이지에 노출될 데이터 리스트
        /*
        List<Product> list = prdRepo.findAll();
        model.addAttribute("products", list);

        // 페이지 총 갯수 출력
        Page<Product> pageTotalCnt = prdRepo.findPageBy(PageRequest.of(1, 5));
        int cnt = pageTotalCnt.getTotalPages();

        List<Integer> pages = new ArrayList<>();
        for(int i = 0; i < cnt; i++) {
            pages.add(i);
        }

        model.addAttribute("pages", pages);
        */

        /*
         *  데이터 출력 해보고 싶을 때는 System.out.println(list.get(0).prodNm); 입력해서 확인해보기
         *  (자바 자료구조 알면 이거 말고도 더 있긴한데 쓰기 귀찮음)
         *  참고로 특정 엔티티의 컬럼들 보고 싶으면 이렇게 출력해봐도 됨
         *  System.out.println(new Product().toString());
        */

        // 첫번째 페이지로 바로 리다이렉트
        return "redirect:/product/productList/page/1";
    }

    /**
     * 상품 추가 화면 호출
     * @return productAdd page
     */
    @GetMapping("/productAdd")
    String productAdd() {
        return "product/productAdd.html";
    }

    /**
     * 상품 추가 기능
     * @param prodNm 상품명
     * @param price  가격
     * @return  prodcutList page
     */
    @Transactional
    @PostMapping("/addPrd")
    String addPrd(@RequestParam String prodNm, @RequestParam String price) {

        /**
         * 폼에서 addPrd 호출 시 실행되는 함수인데 파라미터 넘길 때 인텔리제이 세팅에서
         * java compiler > additional 어쩌고 입력란에 -parameters 어쩌고 등록해놨으면 파라미터에 @RequestParam 안 써도됨.
         * 그리고 이렇게 개별로 받지 않고 폼에서 전송된 모든 데이터를 한 번에 담아서 받고 싶으면 Map 자료형 써서 받으면 됨
         * ex) String addPrd(@RequestParam HashMap<Object, Object> req) { System.out.println(req); }
         * 마지막으로 form 으로 전송하는 방식 말고 ajax 의 body 로 전송한 데이터는 @RequestBody 로 받아야 함.
         * 참고로 @ModelAttribute Product product 이런식으로 파라미터 작성하면
         * 알아서 product 엔티티 인스턴스를 만들어주고 네이밍이 같은 엔티티 내 속성에 집어 넣어주기는 하는데
         * 어차피 여기서는 내가 받은 값을 가지고 조작할거라서 안 썼음
         */

        /**
         * 입력값 유효성 체크 후 DB 저장
         */
        if(!prodNm.isBlank() && !price.isBlank()) {

            // 가격 숫자만 입력 되었는지 정규식 체크
            final String REGEX = "[0-9]+";
            if(price.matches(REGEX)) {
                System.out.println("숫자만 있습니다.");
                // Service 클래스에 비즈니스 로직(아이템 저장) 위임
                productService.saveProduct(prodNm, price);
                return "redirect:/product/productList";

            }
            // 숫자 외 값이 입력된 경우
            else {
                System.out.println("숫자외에 값이 존재합니다.");
                return "redirect:/product/productAdd";
            }
        }

        // 위에 if 문 못 타면 다시 입력 폼으로 돌아감
        return "redirect:/product/productAdd";
    }

    /**
     * 상품 상세 페이지 호출
     * @return productAdd page
     */
    @GetMapping("/{id}")
    String productDetail(Model model, @PathVariable Long id) {

        /**
         *  findById() 함수?
         *   >> JPA 에서 특정 row 찾아서 꺼내는데 쓰이는 함수
         *  Optional?
         *   >> 변수가 비어있을 수도 있고 설정된 제네릭 타입일 수도 있음 라는걸 명시해주고 get() 함수를 통해서 데이터 얻어옴
         *      다만 Optional 인스턴스 생성 후에 바로 get 하는건 데이터가 null 일 가능성이 있기 때문에
         *      생성한 인스턴스 변수에 isPresent() 함수를 호출해서 값이 비어있는지 체크한 뒤에 get() 함수를 쓰는게 일반적임
         *      Optional 관련해서 자세한 내용은 찾아봐야겠다..
         *  @PathVariable 어노테이션?
         *   >> 요청자가 url 파라미터에 입력한 값을 가져올 수 있음
         */
        Optional<Product> result = prdRepo.findById(id);
        if(result.isPresent()) {
            model.addAttribute("productDetail", result.get());
            return "product/productDetail.html";
        }

        return "redirect:/product/productList";
    }

    /**
     * [참고]
     * thymeleaf 안 쓰고 REST API 서버 예외 처리 시 해당 문법 사용 하면 편리
     * @ExceptionHandler(Exception.class)
     * public ResponseEntity<String> exceptionHandler() {
     *   return ResponseEntity.status("예외코드").body("내용기입");
     * }
     * 이거 말고도 따로 예외 처리용 클래스 파일 생성해서
     * @ControllerAdvice 붙이고 그 안에 @ExceptionHandler를 넣어주면
     * 모든 컨트롤러에서 에러가 나는 경우 여기 있는 코드가 실행됨.
     *
     * <예시>
     * @ControllerAdvice
     * public class MyExceptionHandler {
     *
     *   @ExceptionHandler(MethodArgumentTypeMismatchException.class)
     *   public ResponseEntity<String> handler() {
     *     return ResponseEntity.status(400).body("특정 에러시 발동");
     *   }
     *
     * }
     * </예시>
     */

    /**
     * 상품 수정 페이지 이동
     * @return
     */
    @GetMapping("/productEdit/{id}")
    String productEdit(Model model, @PathVariable Long id) {

        Optional<Product> result = prdRepo.findById(id);
        if(result.isPresent()) {
            model.addAttribute("data", result.get());
            return "product/productEdit.html";
        }
        else {
            return "redirect:/product/productList";
        }
    }


    /**
     * 상품 수정 기능
     * @param prodCd 상품코드
     * @param prodNm 상품명
     * @param price  가격
     * @return  prodcutList page
     */
    @Transactional
    @PostMapping("/editPrd")
    String editPrd(@RequestParam Long prodCd, @RequestParam String prodNm, @RequestParam String price) {

        /**
         * 입력값 유효성 체크 후 DB 저장
         */
        if(!prodNm.isBlank() && !price.isBlank()) {

            // 가격 숫자만 입력 되었는지 정규식 체크
            final String REGEX = "[0-9]+";
            if(price.matches(REGEX)) {
                System.out.println("숫자만 있습니다.");
                // Service 클래스에 비즈니스 로직(아이템 저장) 위임
                productService.editProduct(prodCd, prodNm, price);
            }
            // 숫자 외 값이 입력된 경우
            else {
                System.out.println("숫자외에 값이 존재합니다.");
                return "redirect:/product/productList";
            }
        }

        // 위에 if 문 못 타면 다시 입력 폼으로 돌아감
        return "redirect:/product/productList";

    }

    /**
     * 상품 삭제 기능 1
     * 쿼리 스트링으로 붙여서 호출할 때
     * 근데 얘 너무 데이터 보안에 취약해 보임
     * @param prodCd 상품코드
     * @return
     */
    @DeleteMapping("/deletePrdToQueryStr")
    ResponseEntity<String> deletePrdToQueryStr(@RequestParam Long prodCd) {
        prdRepo.deleteById(prodCd);
        return ResponseEntity.status(200).body("DELETE");
    }

    /**
     * 상품 삭제 기능2
     * POST 요청으로 특정 이벤트 발생 시 ajax 요청할 때 처리
     * @param body
     * @return
     */
    @PostMapping("/deletePrdToAjax")
    public ResponseEntity<Map<String, String>> deletePrdToAjax(@RequestBody Map<String, String> body) {
        Long prodCd = Long.parseLong(body.get("prodCd"));

        prdRepo.deleteById(prodCd);

        Map<String, String> response = new HashMap<>();
        response.put("result", "success");

        return ResponseEntity.ok(response); // 상태코드 200 + JSON 응답
    }


    /**
     * 상품 리스트 페이징 처리
     * @param model
     * @return 페이징 처리된 리스트 (페이지마다 5개씩 노출)
     */
    @GetMapping("/productList/page/{num}")
    String productList(Model model, @PathVariable Integer num) {

        /**
         * JPA 에서 제공하는 페이지 함수 사용하면 페이징 처리 편리함
         * 사용은 PageRequest.Of(현재 페이지, 현재 페이지에서 보여줄 리스트 수) 로 하면 됨
         */
        Page<Product> list = prdRepo.findPageBy(PageRequest.of(num - 1, 5));

        // 총 페이지 갯수 리턴을 위한 처리
        int cnt = list.getTotalPages();

        List<Integer> pages = new ArrayList<>();
        for(int i = 0; i < cnt; i++) {
            pages.add(i);
        }

        // System.out.println(list.hasNext());          > 다음 페이지 존재 여부
        model.addAttribute("products", list);   // 페이지 리스트
        model.addAttribute("pages", pages);     // 총 페이지 갯수 리스트
        return "product/productList.html";
    }

    /**
     * 파일 업로드 기능
     * 1. 서버에 presigned url 발급 요청
     * 2. 서버는 aws 에 presigned url 요청 받아 클라이언트에 응답
     * 3. 클라이언트는 서버에서 응답받은 presigned url 을 통해 put 요청
     * 4. aws s3 버킷에 파일 업로드
     * @param filename
     * @return
     */
    @GetMapping("/fileUpload")
    @ResponseBody
    String fileUpload(@RequestParam String filename){
        var result = s3Service.createPresignedUrl("uploadFolder/" + filename);
        return result;
    }

    /**
     * 댓글 작성 기능
     * @param content   댓글내용
     * @param parentId  상품코드
     * @param auth      로그인 사용자 정보
     * @return
     */
    @Transactional
    @PostMapping("/saveComment")
    String saveComment(@RequestParam String content, @RequestParam Long parentId, Authentication auth) {

        // 로그인 회원 아이디 가져옴
        String userId = auth.getName();

        // 로그인 하지 않은 사용자의 경우 임의처리
        if(userId.isBlank()) {
            userId = "undefinedUser";
        }

        productService.saveComment(content, parentId, userId);

        // 위에 if 문 못 타면 다시 입력 폼으로 돌아감
        return "redirect:/product/productList";
    }
}



















