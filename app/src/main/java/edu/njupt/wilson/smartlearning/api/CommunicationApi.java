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

package edu.njupt.wilson.smartlearning.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import edu.njupt.wilson.smartlearning.utils.URLUtil;
import edu.njupt.wilson.smartlearning.domain.RestResult;

/**
 * 服务请求接口
 * Created by wilson on 15/10/21.
 */

@Rest(rootUrl = URLUtil.root, converters = GsonHttpMessageConverter.class)
public interface CommunicationApi extends RestClientErrorHandling {

    @Get("communication/addCommunication?userId={userId}&&questionTheme={questionTheme}&&comContent={comContent}&&isAppLogin={isAppLogin}")
    RestResult addCommunication(int userId, String questionTheme, String comContent, String isAppLogin);

    @Get("communication/getAllCommunication?questionId={questionId}&&type={type}&&isAppLogin={isAppLogin}")
    RestResult getAllCommunication(int questionId, int type, String isAppLogin);  //List<AllQuestion>

    @Get("communication/getUserCommunication?userId={userId}&&questionId={questionId}&&isAppLogin={isAppLogin}")
    RestResult getUserCommunication(int userId, int questionId, String isAppLogin);

    @Get("communication/findCommentByQuestionId?questionId={questionId}&&commentId={commentId}&&type={type}&&isAppLogin={isAppLogin}")
    RestResult findCommentByQuestionId(int questionId, int commentId, Integer type, String isAppLogin);   //List<AllAnswerResult>

    @Get("communication/addComment?userId={userId}&&questionId={questionId}&&comContent={comContent}&&isAppLogin={isAppLogin}")
    RestResult addComment(int userId, int questionId, String comContent, String isAppLogin);
}
