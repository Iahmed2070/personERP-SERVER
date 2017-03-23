package main.java.com.banfftech.personerp.peplatform.submail.lib.base;

import main.java.com.banfftech.personerp.peplatform.submail.entity.DataStore;

public abstract class SenderWapper {

	protected DataStore requestData = new DataStore();

	public abstract ISender getSender();
}
