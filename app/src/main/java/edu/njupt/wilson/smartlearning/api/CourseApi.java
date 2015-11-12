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

import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * <p>课程请求服务，根路径为URLUtil.root，可在URLUtil中修改配置</p>
 * Created by wilson on 15/10/22 上午9:47.
 * Belongs to SmartLearnning
 */
@Rest(rootUrl = URLUtil.root, converters = GsonHttpMessageConverter.class)
public interface CourseApi extends RestClientErrorHandling {

    @Get("course/getAllCourse?isAppLogin={isAppLogin}")
    RestResult getAllCourse(String isAppLogin);

    @Get("video/getVideoByCourse?courseId={courseId}&&isAppLogin={isAppLogin}")
    RestResult getVideoByCourse(int courseId, String isAppLogin);
}

