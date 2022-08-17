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
//    @Test
//    void deleteBoxByIdHaveInDatabase(){
//        UUID id = UUID.fromString("2cf50b52-695f-4b1a-b4bf-abbdcec4fbab");
//        boolean deleteBox = boxService.deleteBox(id);
//        assertThat(deleteBox).isEqualTo(true);
//    }
//    @Test
//    void deleteBoxByIdNotHaveInDatabase(){
//        UUID id = UUID.fromString("2cf50b52-695f-4b1a-b4bf-abbdcec4fba1");
//        boolean deleteBox = boxService.deleteBox(id);
//        assertThat(deleteBox).isEqualTo(false);
//    }
//    @Test
//    void deleteBoxByIdIsEmpty(){
//        UUID id = null;
//        boolean deleteBox = boxService.deleteBox(id);
//        assertThat(deleteBox).isEqualTo(false);
//    }

//    @Test
//    public void testAddAnMemberOrCollaboratorSuccess2() {
//        List<User> users = Arrays.asList(createUser());
//        List<Semester> semesters = Arrays.asList(semester());
//        Iterable<User> iterable = users;
//        UserDto userDto = createUserDto();
//        userDto.setRoleId(10);
//        ResponseMessage responseMessage = new ResponseMessage();
//        responseMessage.setData(semesters);
//        when(userRepository.findAll()).thenReturn(iterable);
//        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(role()));
//        when(semesterService.getCurrentSemester()).thenReturn(responseMessage);
//        ResponseMessage response = userService.addAnMemberOrCollaborator(userDto);
//        assertEquals(response.getData().size(), 1);
//    }
//
//    private User createUser() {
//        User u = new User();
//        return u;
//    }
}
