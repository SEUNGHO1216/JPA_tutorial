package com.jpabook.jpashop.controller;

import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.dto.BookForm;
import com.jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String createFormSubmit(BookForm bookForm){
        Book book = new Book();
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }
    @GetMapping("/items")
    public String showItems(Model model){
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);

        return "items/itemList";
    }

    //수정 view
    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book book = (Book)itemService.findOne(itemId);

        //이렇게 꺼낸 엔티티를 직접 보내는 것은 지양
        //model.addAttribute("form", item);

        //dto형태로 보내는 것을 지향
        BookForm form = new BookForm();
        form.setId(book.getId());
        form.setName(book.getName());
        form.setPrice(book.getPrice());
        form.setStockQuantity(book.getStockQuantity());
        form.setAuthor(book.getAuthor());
        form.setIsbn(book.getIsbn());
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    @Transactional
    public String updateItemFormSubmit(@ModelAttribute("form") BookForm bookForm){

        // 아래와 같이 id값을 통해 그 book 자체를 변경하면 안 되나? = dirty checking(변경감지)
        // 아래와 같이 변경감지를 사용해서 수정기능 구현하면 된다
        Book book = (Book)itemService.findOne(bookForm.getId());
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        /*아래의 내용은 준영속 엔티티(jpa가 더이상 관리하지 않는 엔티티)의 변경에 관한 내용
        - 현재상태는 jpa가 관리하지 않기 때문에(새 객체 생성해서) 변경감지는 사용불가하며 병합만 가능
        - 병합 = merge()사용하는 것인데, 변경감지와 비슷하나 파라미터들을 그대로 set 시키는 것으로
            일부 변경만 하는 것이 안 됨(일부만 변경시 변경 안 된 부분은 null처리됨, 데이터 손실 우려)
        - 변경감지 = transaction안에 있어야하고, jpa 영속성 안에 있어야함. 일부 변경만이어도 jpa가
            감지해서 수정 처리함.
        * */
//        Book book = new Book();
//        book.setId(bookForm.getId()); //이것이 중요. 아이디 값 따로 안 넣으면 새로 생성됨
//        book.setName(bookForm.getName());
//        book.setPrice(bookForm.getPrice());
//        book.setStockQuantity(bookForm.getStockQuantity());
//        book.setAuthor(bookForm.getAuthor());
//        book.setIsbn(bookForm.getIsbn());
//
//        itemService.saveItem(book);
        return "redirect:/items";

    }

}
