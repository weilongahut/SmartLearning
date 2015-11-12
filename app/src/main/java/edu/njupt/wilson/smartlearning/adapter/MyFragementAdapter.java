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

package edu.njupt.wilson.smartlearning.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.njupt.wilson.smartlearning.fragment.CommunicationFragment;
import edu.njupt.wilson.smartlearning.fragment.CourseFragment;
import edu.njupt.wilson.smartlearning.fragment.PersonalFragment;

/**
 * <p> </p>
 * Created by wilson on 15/10/22 上午9:22.
 * Belongs to SmartLearning
 */
public class MyFragementAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> list = new ArrayList<Fragment>();

    public MyFragementAdapter(FragmentManager fm){
        super(fm);
        this.list.add(new CourseFragment());
        this.list.add(new CommunicationFragment());
        this.list.add(new PersonalFragment());

    }

    @Override
    public Fragment getItem(int position){
        return this.list.get(position);
    }

    @Override
    public int getCount(){
        return this.list.size();
    }

}
