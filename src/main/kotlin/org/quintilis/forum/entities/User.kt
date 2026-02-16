package org.quintilis.forum.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users", schema = "auth") // Mapeia a mesma tabela do Auth
class User {
    @Id
    @Column(name = "id", nullable = false)
    var id: UUID? = null

    @Column(name = "username")
    var username: String? = null

    @Column(name = "avatar_path")
    var avatarPath: String? = null
    
    // Sem relacionamento com Player/Minecraft!
}
