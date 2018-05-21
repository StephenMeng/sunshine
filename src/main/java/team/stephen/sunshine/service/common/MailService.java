package team.stephen.sunshine.service.common;

import team.stephen.sunshine.model.common.Email;

public interface MailService {
    int sendMail(Email email);
}
