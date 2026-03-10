package org.quintilis.forum.service

import jakarta.transaction.Transactional
import org.quintilis.common.entities.auth.User
import org.quintilis.common.exception.BadRequestException
import org.quintilis.common.exception.ForbiddenException
import org.quintilis.common.repositories.UserRepository
import org.quintilis.common.service.BaseService
import org.quintilis.forum.controller.TopicController
import org.quintilis.forum.dto.TopicDTO
import org.quintilis.forum.entities.Topic
import org.quintilis.forum.repositories.CategoryRepository
import org.quintilis.forum.repositories.TopicRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.reflect.KProperty1

@Service
class TopicService(
    private val topicRepository: TopicRepository,
    private val categoryRepository: CategoryRepository,
) : BaseService<Topic, UUID, TopicDTO, TopicController.TopicReceiverDTO>(topicRepository) {

    // ========================================================================
    // 1. CONTRATOS DO BASE SERVICE
    // ========================================================================

    override fun getSearchFields(): List<KProperty1<Topic, *>> {
        return listOf(
            Topic::title,
            Topic::content,
            Topic::slug
        )
    }

    override fun updateEntityFromDTO(dto: TopicDTO, entity: Topic) {
        // Atualizamos apenas o que faz sentido mudar em um tópico
        entity.title = dto.title
        entity.content = dto.content
    }

    override fun newDTOToEntity(newDTO: TopicController.TopicReceiverDTO): Topic {
        // 🔥 Travamos a criação genérica porque Tópicos precisam de Autor!
        throw UnsupportedOperationException("A criação de tópicos via API genérica não é permitida. Use o fluxo com Usuário Autenticado.")
    }

    // ========================================================================
    // 2. MÉTODOS CUSTOMIZADOS
    // ========================================================================

    @Cacheable("topic_slug", key = "#slug")
    fun findBySlug(slug: String): TopicDTO? {
        return topicRepository.findBySlug(slug)?.toDTO()
    }

    // Mantemos a sua lógica brilhante intacta, apenas retornando o DTO no final
    @Transactional
    fun createTopicWithUser(topicDTO: TopicController.TopicReceiverDTO, user: User): TopicDTO {
        val category = categoryRepository.findById(topicDTO.categoryId).orElseThrow {
            BadRequestException("Category not found", "Category slug not found")
        }

        if (category.permissions.isNotEmpty()) {
            if (!user.hasPermission(category.permissions)) {
                throw ForbiddenException(
                    "You do not have the required permission (${category.permissions.joinToString(", ") { it.name ?: "" }}) to create a topic in this category."
                )
            }
        }

        val baseSlug = topicDTO.title
            .lowercase()
            .replace("[^a-z0-9\\s-]".toRegex(), "")
            .replace("\\s+".toRegex(), "-")
        val uniqueSuffix = UUID.randomUUID().toString().substring(0, 8)
        val generatedSlug = "$baseSlug-$uniqueSuffix"

        val topic = Topic().apply {
            this.title = topicDTO.title
            this.slug = generatedSlug
            this.content = topicDTO.content
            this.category = category
            this.author = user
        }

        return topicRepository.save(topic).toDTO()
    }
}