package com.saferent.service;

import com.saferent.entity.ContactMessage;
import com.saferent.exception.ResourceNotFoundException;
import com.saferent.exception.message.ErrorMessage;
import com.saferent.repository.ContactMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    public void saveMessage(ContactMessage contactMessage) {
        contactMessageRepository.save(contactMessage);
    }

    public List<ContactMessage> getAll() {

        return contactMessageRepository.findAll();

    }

    public Page<ContactMessage> getAll(Pageable pageable) {

        return contactMessageRepository.findAll(pageable);

    }

    public ContactMessage getContactMessageWithId(Long id) {

        return
                contactMessageRepository
                        .findById(id)
                        //.orElseThrow(() -> new ResourceNotFoundException("ContactMessage Not Found!!" + id));

                        .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));
    }
}
