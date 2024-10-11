package com.saferent.controller;

import com.saferent.dto.ContactMessageDTO;
import com.saferent.dto.request.ContactMessageRequest;
import com.saferent.dto.response.ResponseMessage;
import com.saferent.dto.response.SfResponse;
import com.saferent.entity.ContactMessage;
import com.saferent.mapper.ContactMessageMapper;
import com.saferent.service.ContactMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


        List<ContactMessage> contactMessageList = contactMessageService.getAll();

        List<ContactMessageDTO> contactMessageDTOList = contactMessageMapper.map(contactMessageList);

        return ResponseEntity.ok(contactMessageDTOList); // new ResponseEntity<>(contactMessageDTOList, HttpStatus.OK)


    }

    @GetMapping("/pages")
    public ResponseEntity<Page<ContactMessageDTO>> getAllContactMessageWithPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam(value = "direction", required = false, defaultValue = "DESC")Sort.Direction direction
            ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(prop));

        Page<ContactMessage> contactMessagePage = contactMessageService.getAll(pageable);

        Page<ContactMessageDTO> pageDTO = getPageDTO(contactMessagePage);

        return ResponseEntity.ok(pageDTO);


    }


    @GetMapping("/{id}")
    public ResponseEntity<ContactMessageDTO> getContactMessageWithPath(@PathVariable("id") Long id) {

        ContactMessage contactMessage = contactMessageService.getContactMessage(id);

        ContactMessageDTO contactMessageDTO = contactMessageMapper.contactMessageToDTO(contactMessage);

        return ResponseEntity.ok(contactMessageDTO);

    }



    @GetMapping("/request")
    public ResponseEntity<ContactMessageDTO> getContactMessageWithRequestParam(@RequestParam("id") Long id) {


       ContactMessage contactMessage = contactMessageService.getContactMessage(id);

       ContactMessageDTO contactMessageDTO = contactMessageMapper.contactMessageToDTO(contactMessage);

       return ResponseEntity.ok(contactMessageDTO);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<SfResponse> deleteContactMessage(@PathVariable("id") Long id) {

        contactMessageService.deleteContactMessage(id);

        SfResponse response = new SfResponse(ResponseMessage.CONTACTMESSAGE_DELETE_RESPONSE, true);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<SfResponse> updateContactMessage(@PathVariable("id") Long id,
                                                           @Valid @RequestBody ContactMessageRequest request) {



        ContactMessage contactMessage = contactMessageMapper.contactMessageRequestToContactMessage(request);


        contactMessageService.updateContactMessage(id, contactMessage);

        SfResponse response = new SfResponse(ResponseMessage.CONTACTMESSAGE_UPDATE_RESPONSE, true);


        return ResponseEntity.ok(response);

    }







    private Page<ContactMessageDTO> getPageDTO(Page<ContactMessage> contactMessagePage) {

        return  contactMessagePage
                .map(contactMessage -> contactMessageMapper.contactMessageToDTO(contactMessage));
    }

}
