package com.evilgeniustechnologies.dclocator.models;

import java.util.Date;

public class MessageObjectList {
	public String idContactWith;
	public Date interval;
	public String lastSender;
	public String getLastSender() {
		return lastSender;
	}
	public void setLastSender(String lastSender) {
		this.lastSender = lastSender;
	}
	public Date getInterval() {
		return interval;
	}
	public void setInterval(Date interval) {
		this.interval = interval;
	}
	public String getIdContactWith() {
		return idContactWith;
	}
	public void setIdContactWith(String idContactWith) {
		this.idContactWith = idContactWith;
	}
	public String Id;
	public boolean isLastSender;
	public boolean isLastSender() {
		return isLastSender;
	}
	public void setLastSender(boolean isLastSender) {
		this.isLastSender = isLastSender;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getNameMessage() {
		return NameMessage;
	}
	public void setNameMessage(String nameMessage) {
		NameMessage = nameMessage;
	}
	public String getName_sender() {
		return Name_sender;
	}
	public void setName_sender(String name_sender) {
		Name_sender = name_sender;
	}
	public String getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
	public Date getDateInterval() {
		return DateInterval;
	}
	public void setDateInterval(Date date) {
		DateInterval = date;
	}
	public String getAvarta() {
		return Avarta;
	}
	public void setAvarta(String avarta) {
		Avarta = avarta;
	}
	public String NameMessage;
	public String Name_sender;
	public String lastMessage;
	public Date DateInterval;
	public String Avarta;
	public Boolean isSender;
	public Boolean isGroup;
	public Boolean getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}
	public Boolean getIsSender() {
		return isSender;
	}
	public void setIsSender(Boolean isSender) {
		this.isSender = isSender;
	}

}
