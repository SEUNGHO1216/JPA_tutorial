package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ItemServiceTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Test
    public void saveItem(){
        Item item = new Book();
        //given
        item.setName("맥북 프로 16인치");
        item.setPrice(30_000_000);
        //when
        Long itemId = itemService.saveItem(item);

        //then
        assertEquals(item, itemService.findOne(itemId));
        assertEquals(item, itemRepository.findOne(itemId));
        assertNotNull(item);
    }

}