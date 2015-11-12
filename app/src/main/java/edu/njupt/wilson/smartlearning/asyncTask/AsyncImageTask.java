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

package edu.njupt.wilson.smartlearning.asyncTask;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;

import edu.njupt.wilson.smartlearning.utils.ImageURIUtil;

/**
 * <p> </p>
 * Created by wilson on 15/10/21 下午9:57.
 * Belongs to SmartLearnning
 */
public class AsyncImageTask  extends AsyncTask <Void, Void, Uri>{

    private ImageView photo;

    private File cache;

    private String path;

    public AsyncImageTask(String path, File cache, ImageView photo){
        this.path = path;
        this.cache = cache;
        this.photo = photo;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }
    /**
     * 后台子线程
     * @param params
     * @return
     */
    @Override
    protected Uri doInBackground(Void... params){
        try {
            return ImageURIUtil.getImageURI(this.path, this.cache);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Uri result){
        super.onPostExecute(result);
        //完成图片的绑定
        if (result != null){
            this.photo.setImageURI(result);
        }
    }
}
