package com.evilgeniustechnologies.dclocator.models;

import java.sql.Date;
import java.util.ArrayList;

import org.json.JSONObject;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
@ParseClassName("Messages")
public class Messages extends ParseObject {
	private String objectId;
	private String message;
	private Member objectIDOfClassUser;
	private ArrayList<JSONObject> receivers;
	private Date createdAt;
	private Date updatedAt;
	private MessageGroups group;
	private String fileURL;
	private Number type;
	private ParseGeoPoint gpsCoorDinates;
	private ArrayList<JSONObject> read_by;
	private ArrayList<JSONObject> liked_by;
	private Number likeCount;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Member getObjectIDOfClassUser() {
		return objectIDOfClassUser;
	}
	public void setObjectIDOfClassUser(Member objectIDOfClassUser) {
		this.objectIDOfClassUser = objectIDOfClassUser;
	}
	public ArrayList<JSONObject> getReceivers() {
		return receivers;
	}
	public void setReceivers(ArrayList<JSONObject> receivers) {
		this.receivers = receivers;
	}
	public MessageGroups getGroup() {
		return group;
	}
	public void setGroup(MessageGroups group) {
		this.group = group;
	}
	public String getFileURL() {
		return fileURL;
	}
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	public Number getType() {
		return type;
	}
	public void setType(Number type) {
		this.type = type;
	}
	public ParseGeoPoint getGpsCoorDinates() {
		return gpsCoorDinates;
	}
	public void setGpsCoorDinates(ParseGeoPoint gpsCoorDinates) {
		this.gpsCoorDinates = gpsCoorDinates;
	}
	public ArrayList<JSONObject> getRead_by() {
		return read_by;
	}
	public void setRead_by(ArrayList<JSONObject> read_by) {
		this.read_by = read_by;
	}
	public ArrayList<JSONObject> getLiked_by() {
		return liked_by;
	}
	public void setLiked_by(ArrayList<JSONObject> liked_by) {
		this.liked_by = liked_by;
	}
	public Number getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Number likeCount) {
		this.likeCount = likeCount;
	}
	public String getObjectId() {
		return objectId;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	
	
//	public String MessageId;
//	public static String Text;
//	public java.util.Date DateSend;
//	public static ParseObject MemberSend;
//	
//	public String getMessId() {
//		return getString("objectId");
//	}
//public String getMessage() {
//	return getString("message");
//}
//public void sendMesaage(String message) {
//	put("message", message);
//	
//}
//public ParseObject getUser() {
//	return getParseObject("objectIDOfClassUser");
//	
//}
	
}
