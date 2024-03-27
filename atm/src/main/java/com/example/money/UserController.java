package com.example.money;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
class UserController {

    @Autowired
    UserRepository repository;

    @Autowired
    UserModelAssembler assembler;

    // Aggregate root
    // tag::get-aggregate-root[]

    @GetMapping("/users/{id}")
    EntityModel<AtmUser> one(@PathVariable Long id) {

        AtmUser user = repository.findById(id) //
                .orElseThrow(() -> new UserNotFoundException(id));

        return assembler.toModel(user);
    }

    @GetMapping("/users")
    CollectionModel<EntityModel<AtmUser>> all() {

        List<EntityModel<AtmUser>> users = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    AtmUser newUser(@RequestBody AtmUser newUser) {
        return repository.save(newUser);
    }

    @PutMapping("/users/{id}")
    AtmUser replaceUser(@RequestBody AtmUser newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setAccountBalance(newUser.getAccountBalance());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }

    // Transactions routes
    @PostMapping("/users/{id}/deposit")
    AtmUser depositToUser(@RequestBody double depositAmount, @PathVariable Long id) {
        return repository.findById(id)
                .map(user -> {
                    double accountBalance = user.getAccountBalance();
                    accountBalance += depositAmount;
                    user.setAccountBalance(accountBalance);
                    return repository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping("/users/{id}/withdraw")
    AtmUser withdrawFromUser(@RequestBody double withdrawAmount, @PathVariable Long id) {
        return repository.findById(id)
                .map(user -> {
                    double accountBalance = user.getAccountBalance();
                    if (withdrawAmount >= accountBalance) {
                        accountBalance -= withdrawAmount;
                        user.setAccountBalance(accountBalance);
                        return repository.save(user);
                    } else {
                        throw new InsufficientFundsException(accountBalance, withdrawAmount);
                    }

                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
