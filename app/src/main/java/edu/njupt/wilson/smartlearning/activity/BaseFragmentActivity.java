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

package edu.njupt.wilson.smartlearning.activity;/**
 * <p> </p>
 * Created by wilson on 15/10/24 上午10:54.
 * Belongs to SmartLearnning
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Author: wilson
 * Date: 2015-10-24 
 * Time: 10:54 
 *
 */
public class BaseFragmentActivity  extends FragmentActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    /**
     * 在指定View内现实Fragment，如果Fragment未创建，则新建
     * @param viewId 指定的View
     * @param tag fragment标签
     * @param args savedInstanceState
     * @return
     */
    public Fragment showCreateFragment(int viewId, String tag, Bundle args){

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (null == fragment){
            fragment = createFragment(tag);
        }

        if (args != null){
            if (fragment.getArguments() == null){
                fragment.setArguments(args);
            } else {
                fragment.getArguments().clear();
                fragment.getArguments().putAll(args);
            }
        }

        //提交更改
        getSupportFragmentManager().beginTransaction().replace(viewId, fragment, tag).addToBackStack(null).commit();

        return fragment;
    }

    /**
     * 根据Tag创建Fragment
     * @param tag 新建fragment的标签
     * @return
     */
    public Fragment createFragment(String tag){

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (null == fragment){
            try {
                fragment = (Fragment) Class.forName(tag).newInstance();
            } catch (InstantiationException e){
                Log.e("BaseFragmentActivity", "创建fragment " + tag +" 初始化失败!", e);
            } catch (IllegalAccessException e){
                Log.e("BaseFragmentActivity", "创建fragment " + tag +" 失败！非法访问！", e);
            } catch (ClassNotFoundException e){
                Log.e("BaseFragmentActivity", "创建fragment " + tag +" 失败!找不到类！", e);
            }
        }
        return fragment;
    }
}
