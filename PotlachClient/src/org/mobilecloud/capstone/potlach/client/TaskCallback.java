/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.mobilecloud.capstone.potlach.client;

public interface TaskCallback<T> {

    public void success(T result);

    public void error(Exception e);

}
