/*
 *    Copyright @2015 wilson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package edu.njupt.wilson.smartlearning.domain;

import java.io.Serializable;

/**
 * REST请求结果
 * Created by wilson on 15/10/21.
 */
public class RestResult implements Serializable{

    private static final long serialVersionUID = -1809195782514717593L;

    public static final RestResult OK = new RestResult();

    public static final int SUCCESS_CODE = 1;

    public static final String SUCCESS_MSG = "success";

    private int code;

    private  String msg;

    private Object data;

    public RestResult(){
        this(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    public RestResult(Object data){
        this(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public RestResult(int code, String msg){
        this(code, msg, null);
    }

    public RestResult(int code, Object data){
        this(code, SUCCESS_MSG, data);
    }

    public RestResult(int code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
