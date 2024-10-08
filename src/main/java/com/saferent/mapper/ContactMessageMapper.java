package com.saferent.mapper;

import com.saferent.dto.ContactMessageDTO;
import com.saferent.dto.request.ContactMessageRequest;
import com.saferent.entity.ContactMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring") // herhangi bir sınıf enjekte edilip kullanılabilir
@Component
public interface ContactMessageMapper {


    // ContactMessage --> ContactMessageDTO
    ContactMessageDTO contactMessageToDTO(ContactMessage contactMessage);



    // ContactMessageRequest --> ContactMessage

    // TARGET  ------------------  SOURCE
    @Mapping(target = "id", ignore = true) // DTOda id olmadığı için map işlemini 'id' field için yapmamasını sağlıyoruz
    ContactMessage contactMessageRequestToContactMessage(ContactMessageRequest contactMessageRequest);



    // List<ContactMessage>  --> List<ContactMessageDTO>
    List<ContactMessageDTO> map(List<ContactMessage> contactMessageList); // getAllContactMessage()
}
