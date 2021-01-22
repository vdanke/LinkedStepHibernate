package org.step.linked.step.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.step.linked.step.exceptions.NotFoundException;
import org.step.linked.step.model.Authority;
import org.step.linked.step.model.User;
import org.step.linked.step.repository.CRUDRepository;
import org.step.linked.step.repository.UserRepository;
import org.step.linked.step.service.IDGenerator;
import org.step.linked.step.service.UserService;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final IDGenerator<String> idGenerator;
    private final CRUDRepository<User> userCRUDRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("uuidGenerator") IDGenerator<String> idGenerator,
                           @Qualifier("userRepositoryImpl") CRUDRepository<User> userCRUDRepository,
                           @Qualifier("userRepositoryImpl") UserRepository userRepository) {
        this.idGenerator = idGenerator;
        this.userCRUDRepository = userCRUDRepository;
        this.userRepository = userRepository;
    }

    /*
    Propagation

    REQUIRED - подключается к текущей, если нет - создает новую
    REQUIRED_NEW - всегда создается новая транзакция
    SUPPORTS - если нет транзакции - без нее, если есть - с ней
    MANDATORY - если нет транзакции - бросает эксепшн
    NESTED - если нет транзакции - создает новую, если есть - завершается вместе с родительской
    NEVER - если транзакция есть - бросает эксепшн
    NOT_SUPPORTED - если транзакция есть, ее останавливает и выполняется без нее

    Isolation

    READ_UNCOMMITTED - не предотвращает ничего
    READ_COMMITTED - предотвращает dirty reads
    REPEATABLE_READ - предотвращает dirty reads and non-repeatable reads
    SERIALIZABLE - предотвращает все

    noRollbackFor(classes = {ArithmeticException.class})
    noRollbackForClassName

    rollbackFor(classes = {NullPointerException.class})
    rollbackForClassName

    timeout - время на траназакцию

    readOnly - флаг исключительно на чтение
     */

    @Override
    public List<User> findAllWithSorting(int age, String direction) {
        return userRepository.findAllWithSorting(age, direction);
    }

    @Override
    @Transactional(readOnly = true, timeout = 3000)
    public List<User> findAll() {
        return userCRUDRepository.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void save(User user) {
        user.setId(idGenerator.generate());
        user.setAuthorities(Collections.singleton(Authority.ROLE_USER));
        userCRUDRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public User update(User user) {
        return userCRUDRepository.update(user);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = NotFoundException.class)
    public User findById(String id) {
        return userCRUDRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with ID %s not found", id)));
    }

    @Override
    @Transactional
    public boolean deleteById(String id) {
        return userCRUDRepository.deleteById(id);
    }
}
