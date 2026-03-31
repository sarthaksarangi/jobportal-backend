package com.jobportal.backend.contact.service;

import com.jobportal.backend.dto.ContactRequestDto;

public interface IContactService {
    public boolean saveContact(ContactRequestDto contactRequestDto);
}
