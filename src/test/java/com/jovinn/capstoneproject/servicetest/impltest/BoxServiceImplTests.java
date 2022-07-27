package com.jovinn.capstoneproject.servicetest.impltest;

import com.jovinn.capstoneproject.service.BoxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

@SpringBootTest
public class BoxServiceImplTests {
    @Autowired
    private BoxService boxService;
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
}
