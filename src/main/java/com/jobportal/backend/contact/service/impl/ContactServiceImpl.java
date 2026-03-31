package com.jobportal.backend.contact.service.impl;

import com.jobportal.backend.contact.service.IContactService;
import com.jobportal.backend.dto.ContactRequestDto;
import com.jobportal.backend.entity.Contact;
import com.jobportal.backend.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    @Override
    public boolean saveContact(ContactRequestDto contactRequestDto) {
        Contact contact = contactRepository.save(transformEntity(contactRequestDto));
        if(contact != null && contact.getId() != null) return true;
        return false;
    }

    private Contact transformEntity(ContactRequestDto contactRequestDto){
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto, contact);
        contact.setStatus("NEW");
        Instant now = Instant.now();
        contact.setCreatedAt(now);
        contact.setUpdatedAt(now);

        contact.setCreatedBy("System");
        contact.setUpdatedBy("System");
        return contact;

    }
}
