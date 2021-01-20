package org.step.linked.step.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.step.linked.step.exceptions.NotFoundException;
import org.step.linked.step.model.Authority;
import org.step.linked.step.model.User;
import org.step.linked.step.repository.CRUDRepository;
import org.step.linked.step.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    /*
    @Mock - Управляемая зависимость (контроль выполнения метода)
    @InjectMocks - Та зависимость, где используются моки
    @Spy - Шпионит за реальным объектом и проверяет его методы
           (@InjectMocks - только на реальный объект)
     */
    @Mock
    private IDGenerator<String> idGenerator;
    @Mock
    private CRUDRepository<User> userCRUDRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void userService_FindAll() {
        final String userTestID = UUID.randomUUID().toString();
        final int expectedSize = 1;
        final User user = User.builder().id(userTestID).build();

        Mockito.when(userCRUDRepository.findAll()).thenReturn(new ArrayList<>() {{
            add(user);
        }});

        List<User> all = userService.findAll();

        Mockito.verify(userCRUDRepository, Mockito.only()).findAll();

        Assertions.assertNotNull(all);
        Assertions.assertEquals(expectedSize, all.size());
        Assertions.assertTrue(all.contains(user));
    }

    @Test
    public void userService_SaveNewUser() {
        final String userTestID = UUID.randomUUID().toString();
        final User user = User.builder().build();

        Mockito.when(idGenerator.generate()).thenReturn(userTestID);
        Mockito.doNothing().when(userCRUDRepository).save(Mockito.any(User.class));

        userService.save(user);

        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(user.getAuthorities());
        Assertions.assertTrue(user.getAuthorities().contains(Authority.ROLE_USER));

        Mockito.verify(idGenerator, Mockito.times(1)).generate();
        Mockito.verify(userCRUDRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void userService_FindById() {
        final String userTestID = UUID.randomUUID().toString();

        Mockito.when(userCRUDRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(User.builder().id(userTestID).build()));

        User user = userService.findById(userTestID);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(userTestID, user.getId());

        Mockito.verify(userCRUDRepository, Mockito.times(1)).findById(Mockito.anyString());
    }

    @Test
    public void userService_FindById_ReturnEmptyOptional() {
        final String userTestID = UUID.randomUUID().toString();
        final String exceptionMessage = String.format("User with ID %s not found", userTestID);

        Mockito.when(userCRUDRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        NotFoundException ex = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.findById(userTestID)
        );
        Assertions.assertEquals(exceptionMessage, ex.getLocalizedMessage());
    }
}
