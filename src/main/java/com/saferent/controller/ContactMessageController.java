package com.saferent.controller;

import com.saferent.dto.ContactMessageDTO;
import com.saferent.dto.request.ContactMessageRequest;
import com.saferent.dto.response.SfResponse;
import com.saferent.entity.ContactMessage;
import com.saferent.mapper.ContactMessageMapper;
import com.saferent.service.ContactMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contactmessage")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    private final ContactMessageMapper contactMessageMapper;

    public ContactMessageController(ContactMessageService contactMessageService, ContactMessageMapper contactMessageMapper) {
        this.contactMessageService = contactMessageService;
        this.contactMessageMapper = contactMessageMapper;
    }


    @PostMapping("/visitors")
    public ResponseEntity<SfResponse> createMessage(@Valid @RequestBody ContactMessageRequest contactMessageRequest) {

        ContactMessage contactMessage = contactMessageMapper
                .contactMessageRequestToContactMessage(contactMessageRequest);


        contactMessageService.saveMessage(contactMessage);

        SfResponse response = new SfResponse();
        response.setMessage("ContactMessage successfully created!!");
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    @GetMapping
    public ResponseEntity<List<ContactMessageDTO>> getAllContactMessage() {


        List<ContactMessage> contactMessages = contactMessageService.getAll();

        contactMessageMapper.map(contactMessages);



    }











}
