package com.v3.furry_friend_chat_cud.ChatMessageTests;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.v3.furry_friend_chat_cud.entity.ChatMessage;
import com.v3.furry_friend_chat_cud.entity.ChatRoom;
import com.v3.furry_friend_chat_cud.repository.ChatMessageRepository;
import com.v3.furry_friend_chat_cud.repository.ChatRoomRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class ChatMessageRepositoryTests {

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @PersistenceContext
    EntityManager em;

    private void clear() {
        em.flush(); // 1
        em.clear(); // 2
    }

    private ChatRoom createChatRoom() {
        return new ChatRoom("chat_name", 1L, "박용수", false);
    }
    private ChatMessage createChatMessage(ChatRoom chatRoom){
        return new ChatMessage("Hi!", false, false, 1L, "soo", chatRoom);
    }

    @Test
    @DisplayName("JPA Save 테스트")
    void createTest() throws Exception{

        // given
        ChatRoom chatRoom = createChatRoom();
        chatRoomRepository.save(chatRoom);
        clear();
        ChatMessage chatMessage = createChatMessage(chatRoom);

        // when
        chatMessageRepository.save(chatMessage);
        clear();
        ChatMessage result = chatMessageRepository.findById(chatMessage.getChatMessageId())
            .orElseThrow(() -> new Exception("메시지 저장에 실패했습니다."));

        // then
        assertThat(result.getChatMessageId()).isEqualTo(chatMessage.getChatMessageId());
    }

    @Test
    @DisplayName("JPA Select 테스트")
    void findChatMessageTest() throws Exception{

        // given
        ChatRoom chatRoom = createChatRoom();
        chatRoomRepository.save(chatRoom);
        ChatMessage chatMessage = createChatMessage(chatRoom);
        chatMessageRepository.save(chatMessage);
        clear();

        // when
        ChatMessage result = chatMessageRepository.findById(chatMessage.getChatMessageId())
            .orElseThrow(() -> new Exception("메시지 검색에 실패했습니다."));

        // then
        assertThat(chatMessage.getChatMessageId()).isEqualTo(result.getChatMessageId());
    }
}
