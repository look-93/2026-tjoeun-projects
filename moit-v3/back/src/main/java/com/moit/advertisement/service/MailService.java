package com.moit.advertisement.service;

import com.moit.advertisement.dto.AdvertisementDto;

public interface MailService {
	
	void sendAdvertisementReminderMail(AdvertisementDto ad, int remainDay);

}
