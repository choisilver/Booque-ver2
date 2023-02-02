package site.book.project.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.book.project.domain.Book;
import site.book.project.domain.UsedBook;
import site.book.project.domain.UsedBookPost;
import site.book.project.domain.UsedBookWish;
import site.book.project.domain.User;
import site.book.project.dto.MarketCreateDto;
import site.book.project.dto.UserSecurityDto;
import site.book.project.repository.BookRepository;
import site.book.project.repository.SearchRepository;
import site.book.project.repository.UsedBookPostRepository;
import site.book.project.repository.UsedBookRepository;
import site.book.project.repository.UsedBookWishRepository;
import site.book.project.repository.UserRepository;
import site.book.project.service.SearchService;
import site.book.project.service.UsedBookService;

@Slf4j
@Controller
@RequestMapping("/market")
@RequiredArgsConstructor
public class MarketController {
    
    private final SearchRepository searchRepository;
    private final UsedBookService usedBookService;
    private final BookRepository bookRepository;
    private final UsedBookRepository usedBookRepository;
    private final UsedBookPostRepository usedBookPostRepository;
    private final UserRepository userRepository;
    private final UsedBookWishRepository usedBookWishRepository;
    
    @GetMapping("/main") // /market/main 부끄마켓 메인 페이지 이동
    public void main(@AuthenticationPrincipal UserSecurityDto userDto, Model model) {
        List<UsedBook> usedBookList = usedBookRepository.findAll();
        
        List<MarketCreateDto> list = new ArrayList<>();
        
        for (UsedBook ub : usedBookList) {
            User user = userRepository.findById(ub.getUserId()).get();
            Book book = bookRepository.findById(ub.getBookId()).get();
            MarketCreateDto dto = MarketCreateDto.builder()
                    .usedBookId(ub.getId())
                    .userId(user.getId()).username(user.getUsername())
                    .bookTitle(book.getBookName()).price(ub.getPrice())
                    .location(ub.getLocation()).level(ub.getBookLevel()).title(ub.getTitle()).modifiedTime(ub.getModifiedTime())
                    .build();
            list.add(dto);
        }
        if(userDto != null) {
            model.addAttribute("userNickname", userDto.getNickName());       
        }
        
        model.addAttribute("list", list);
        
    }
    

    @GetMapping("/create") // /market/create 중고판매글 작성 페이지 이동
    public void create(Model model) {

    }
    
    @PostMapping("/create")
    public String createPost( @AuthenticationPrincipal UserSecurityDto userDto, MarketCreateDto dto,
    							Integer usedBookId) {
    	// 총 세개의 테이블을 크리에이트 해야함
    	
    	dto.setUserId(userDto.getId());
    	
    	usedBookService.create( usedBookId, dto );
    	
    	return "redirect:/market/detail?usedBookId="+usedBookId;
    }
    
    

    
    
    
    
    @GetMapping("/detail") // /market/detail 중고판매글 상세 페이지 이동
    public void detail(@AuthenticationPrincipal UserSecurityDto userDto ,Integer usedBookId, Model model) {
        // 책 정보 불러오기(bookId) -> postId로 bookId 찾기
        // 판매글 정보 불러오기
        // 판매글제목 & 가격 & 수정시간 & 지역 & 본문 & 판매여부 & 책상태 & 이미지
        UsedBook usedBook = usedBookRepository.findById(usedBookId).get();        
        UsedBookPost usedBookPost = usedBookPostRepository.findByUsedBookId(usedBookId);
        User user = userRepository.findById(usedBook.getUserId()).get(); // 작성자의 정보
        Book book = bookRepository.findById(usedBook.getBookId()).get();
        
        UsedBookWish wish = null;
        // 로그인 한 사람의 정보를 통해 내것도 하트 누를 수 있음!
        // userId, usedBookId가 있으니
        if(userDto != null) {
            log.info("여기 보이긴 하니~~~~~~~~~~~~~~~~~~~~~");
            wish = usedBookWishRepository.findByUserIdAndUsedBookId(userDto.getId(), usedBookId);
            log.info("찾을 수 있니?? 널이니?? {}", wish);
            
        }
        
        // 판매하는 책과 동일한 책(다른 중고책) 리스트
        List<UsedBook> otherUsedBookList = usedBookService.readOtherUsedBook(usedBook.getBookId());
        List<UsedBook> otherUsedBookListFinal = new ArrayList<>();
        
        for (UsedBook u : otherUsedBookList) {
            if(usedBookId != u.getId()) {
                otherUsedBookListFinal.add(u);
            }
        }
        // 로그인 한 사람이 하트를 누름, 하트가 저장됨. 근데 하트가 하트가,,! 
        model.addAttribute("wish", wish);
        model.addAttribute("book", book);
        model.addAttribute("user", user); // userName만 보낼 수 있게 수정(?)
        model.addAttribute("usedBookPost", usedBookPost);
        model.addAttribute("usedBook", usedBook);
        model.addAttribute("otherUsedBookListFinal", otherUsedBookListFinal);
            
    }
    
    
    
    /**
     * ajax를 이용해서 키워드 받고 검색하기
     * @param keyword 검색할 단어(isbn은 아직은 제외됨)
     * @return
     */
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<Book>> bookList(String keyword){
        log.info("확인 해야지 키워드가 잘 넘어갔는지{}   ",keyword);
        
        List<Book> searhList = searchRepository.unifiedSearchByKeyword(keyword);
        return ResponseEntity.ok(searhList);
    }
    
    /**
     * UsedBook 테이블에 userId, bookId 먼저 저장하기
     * @param u user 정보
     * @param bookId  선택한 책의 PK
     * @return  Map타입을 통해 Book과 usedBookId(PK)를 넘김
     */
    @GetMapping("/createUsed")
    @ResponseBody
    public Map<String, Object>  createUsedBook(@AuthenticationPrincipal UserSecurityDto u, Integer bookId) {
        // 저장
        
        Map<String, Object> maps = new HashMap<>();
        
        Integer usedBookId = usedBookService.create(bookId, u.getId());
        Book book = bookRepository.findById(bookId).get();
        
        maps.put("book", book);
        maps.put("usedBookId", usedBookId);
       return maps; 
    }
    

    /**
     * 
     * @param id 마이페이지에 표시될 user 
     * @param model
     */
    @GetMapping("/mypage") // /market/mypage 판매글작성자&마이페이지 이동
    public void mypage(String userNickname,Model model) {
        
        log.info("언제 나와{}", userNickname);
        model.addAttribute("user", userNickname);
        
    }
    
    @GetMapping("/modify")
    public void modify(Integer usedBookId, Model model) {
        UsedBook usedBook = usedBookRepository.findById(usedBookId).get();
        UsedBookPost usedBookPost = usedBookPostRepository.findByUsedBookId(usedBookId);   
        Book book = bookRepository.findById(usedBook.getBookId()).get();
        User user = userRepository.findById(usedBook.getUserId()).get();
        
        
        model.addAttribute("usedBook", usedBook);
        model.addAttribute("usedBookPost", usedBookPost);
        model.addAttribute("book", book);
        model.addAttribute("user", user);
        log.info("여기는 읽히지??");
    }
    

    
    
    @PostMapping("/modify")
    public String modify(MarketCreateDto dto, String originLocation) {
        log.info("수정창에서 읽어오는 dto , {}", dto);
        log.info("주소 값을 안줄때는 원래 값을 읽어야 해! {}", originLocation);
        
        // 책 제목 null이 됨.. 
        if(dto.getLocation().equals("")) {
            dto.setLocation(originLocation);
        }
        usedBookService.create(dto.getUsedBookId(), dto);
        
        return "redirect:/market/detail?usedBookId=" + dto.getUsedBookId();
    }
}
