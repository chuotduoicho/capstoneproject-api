package com.jovinn.capstoneproject.servicetest.impltest;

import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.repository.BoxRepository;
import com.jovinn.capstoneproject.service.BoxService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public class BoxServiceImplTests {
    @Autowired
    private BoxService boxService;
    @Autowired
    private BoxRepository boxRepository;
    @Test
    void deleteBoxByIdHaveInDatabase(){
        UUID id = UUID.fromString("2cf50b52-695f-4b1a-b4bf-abbdcec4fbab");
        boolean deleteBox = boxService.deleteBox(id);
        assertThat(deleteBox).isEqualTo(true);
    }
    @Test
    void deleteBoxByIdNotHaveInDatabase(){
        UUID id = UUID.fromString("2cf50b52-695f-4b1a-b4bf-abbdcec4fba1");
        boolean deleteBox = boxService.deleteBox(id);
        assertThat(deleteBox).isEqualTo(false);
    }
    @Test
    void deleteBoxByIdIsEmpty(){
        UUID id = null;
        boolean deleteBox = boxService.deleteBox(id);
        assertThat(deleteBox).isEqualTo(false);
    }
    @Test
    void getServiceByIdHaveInDatabase(){
        UUID id = UUID.fromString("2e26c0b6-9570-4b83-b73b-26ccc27f0b8f");
        Box box = boxService.getServiceByID(id);
        assertThat(box.getId()).isEqualTo(id);
    }
    @Test
    void getServiceByIdNotHaveInDatabase(){
        UUID id = UUID.fromString("2e26c0b6-9570-4b83-b73b-26ccc27f0b81");
        Box box = boxService.getServiceByID(id);
        assertThat(box).isEqualTo(null);
    }
    @Test
    void getListBox(){
        List<Box> boxes = boxService.getAllService();
        assertThat(boxes.size()).isGreaterThan(0);
    }
    @Test
    void updateBoxTest(){
        UUID id = UUID.fromString("2e26c0b6-9570-4b83-b73b-26ccc27f0b8f");
        Box box = boxService.getServiceByID(id);
        box.setDescription("Tôi làm mọi thứ với 100k");
        Box boxUpdate = boxRepository.save(box);
        assertThat(boxUpdate.getDescription()).isEqualTo("Tôi làm mọi thứ với 100k");
    }
}

