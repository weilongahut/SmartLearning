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
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * <p>用户信息相关请求接口</p>
 * Created by wilson on 15/10/22 上午9:55.
 * Belongs to SmartLearnning
 */
@Rest(rootUrl = URLUtil.root, converters = GsonHttpMessageConverter.class)
public interface UserApi extends RestClientErrorHandling{

    /**
     * 用户登录
     * @param userName 用户名
     * @param pwd 用户密码
     * @return
     */
    @Get("user/userLogin?userName={userName}&&pwd={pwd}")
    RestResult userLogin(String userName, String pwd);

    /**
     * 注册用户
     * @param userName 用户名
     * @param pwd 密码
     * @return
     */
    @Post("user/userRegister?userName={userName}&&pwd={pwd}")
    RestResult userRegister(String userName, String pwd);

    /**
     * 更新用户信息
     * @param userId 用户id
     * @param userName 用户昵称
     * @param userSex 用户性别
     * @param isAppLogin 登录标记
     * @return
     */
    @Get("user/updateUser?userId={userId}&&userName={userName}&&userSex={userSex}&&isAppLogin={isAppLogin}")
    RestResult updateUser(int userId, String userName, String userSex, String isAppLogin);

    /**
     * 修改用户名
     * @param userId 用户id
     * @param newName 新昵称
     * @param isAppLogin 登录标记
     * @return
     */
    @Get("user/modifyName?userId={userId}&&newName={newName}&&isAppLogin={isAppLogin}")
    RestResult modifyName(int userId, String newName, String isAppLogin);

    /**
     * 修改用户性别
     * @param userId 用户id
     * @param sex 要修改的值
     * @param isAppLogin 登录标记
     * @return
     */
    @Get("user/modifySex?userId={userId}&&userSex={sex}&&isAppLogin={isAppLogin}")
    RestResult modifyUserSex(int userId, String sex, String isAppLogin);
}
