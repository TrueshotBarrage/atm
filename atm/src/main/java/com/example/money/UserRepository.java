package com.example.money;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<AtmUser, Long> {

}
