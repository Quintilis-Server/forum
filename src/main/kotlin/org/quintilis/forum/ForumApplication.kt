package org.quintilis.forum

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = [
    "org.quintilis.common", // Para utilitários e DTOs
    "org.quintilis.forum"
])
@EntityScan(basePackages = [
    "org.quintilis.forum.entities" // Apenas entidades do Fórum (incluindo o User simplificado)
])
@EnableJpaRepositories(basePackages = ["org.quintilis.forum.repositories"])
class ForumApplication

fun main(args: Array<String>) {
    runApplication<ForumApplication>(*args)
}
