package com.evilgeniustechnologies.dclocator.models;

import com.parse.ParseObject;

public class RecieveMessage extends Object{
public ParseObject _type;
public ParseObject get_type() {
	return _type;
}
public void set_type(ParseObject _type) {
	this._type = _type;
}
public String getClassName() {
	return className;
}
public void setClassName(String className) {
	this.className = className;
}
public String getObjectId() {
	return objectId;
}
public void setObjectId(String objectId) {
	this.objectId = objectId;
}
public String className;
public String objectId;
}
