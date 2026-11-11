package com.example.lec10network

import com.example.lec10network.data.mapper.toDomain
import com.example.lec10network.data.remote.dto.PostDto
import org.junit.Assert.assertEquals
import org.junit.Test

class PostMapperTest {
    @Test
    fun dto_is_mapped_to_domain_model() {
        val dto = PostDto(
            userId = 7,
            id = 42,
            title = "Network DTO",
            body = "JSON becomes a domain model"
        )

        val post = dto.toDomain()

        assertEquals(42, post.id)
        assertEquals(7, post.userId)
        assertEquals("Network DTO", post.title)
    }
}
